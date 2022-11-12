import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import quote.Broker;
import quote.BrokerService;
import quote.ClientInfo;

public class BrokerTest {

    @BeforeClass
    public static void before() {
        Endpoint.publish("http://0.0.0.0:9000/broker", new Broker());
    }

    @Test
    public void testServer() throws MalformedURLException {
        URL wsdlUrl = new
                URL("http://0.0.0.0:9000/broker?wsdl");
        QName serviceName =
                new QName("http://quote/", "BrokerService");
        Service service = Service.create(wsdlUrl, serviceName);
        QName portName = new QName("http://quote/", "BrokerPort");
        BrokerService brokerService =
                service.getPort(portName, BrokerService.class);
        Assert.assertNotNull(brokerService.getQuotations(new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")));
    }

    @Test
    public void testArgsToURLArray() {
        String[] args = {"-m", "default","-h","0.0.0.0","-p","9000"};
        Broker.argsToURLArray(args);
        Assert.assertTrue(!Broker.URLSet.isEmpty());
        Broker.URLSet.clear();
    }

}
