package quote;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Executors;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class Broker implements BrokerService {

    public static Set<String> URLSet = new HashSet<>();

    public static String mode = "jmdns";


    public static void main(String[] args) {
        try {
            //Passing command line arguments to convert them to URL and put them into Array
            argsToURLArray(args);
            Endpoint endpoint = Endpoint.create(new Broker());
            HttpServer server = HttpServer.create(new InetSocketAddress(9000), 5);
            server.setExecutor(Executors.newFixedThreadPool(5));
            HttpContext context = server.createContext("/broker");
            endpoint.publish(context);
            server.start();

            if (mode.equalsIgnoreCase("jmdns")) {
                //Clear the URL Set
                URLSet.clear();

                // Create a JmDNS instance
                JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

                // Register a service
                jmdns.addServiceListener("_quote._tcp.local.", new WSDLServiceListener());

                System.out.println("-- SERVICE -- :: Broker :: -- Running with JmDNS --");
            }

            System.out.println("-- SERVICE -- :: Broker :: -- UP --");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class WSDLServiceListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {

        }

        @Override
        public void serviceRemoved(ServiceEvent event) {

        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service Resolved: " + event.getInfo());
            String path = event.getInfo().getURLs()[0];
            if ((path != null) && (event.getType().equals("_quote._tcp.local.")) && (path.endsWith("/quotation?wsdl"))) {
                URLSet.add(path);
            }
        }
    }


    public static void argsToURLArray(String[] args) {
        String host = "localhost";
        int port = 9000;
        for (int i = 0; i < args.length; ) {

            if (args[i].equalsIgnoreCase("-h")) {
                host = args[++i];
                i++;
            }
            if (args[i].equalsIgnoreCase("-p")) {
                port = Integer.parseInt(args[++i]);
                i++;
            }
            if ((i < args.length) && (args[i].equalsIgnoreCase("-m"))) {
                mode = args[++i];
                i++;
                continue;

            }
            URLSet.add("http://" + host + ":" + port + "/quotation?wsdl");
        }
    }

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {
        LinkedList<Quotation> quotations = new LinkedList<>();
        for (String url : URLSet) {
            try {
                URL wsdlUrl = new
                        URL(url);
                QName serviceName =
                        new QName("http://quote/", "QuoterService");
                Service service = Service.create(wsdlUrl, serviceName);
                QName portName = new QName("http://quote/", "QuoterPort");
                QuoterService quotationService =
                        service.getPort(portName, QuoterService.class);
                quotations.add(quotationService.generateQuotation(info));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return quotations;
    }
}
