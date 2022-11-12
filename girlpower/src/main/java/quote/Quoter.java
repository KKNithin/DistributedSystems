package quote;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.Endpoint;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Implementation of the Girl Power insurance quotation service.
 * 
 * @author Rem
 *
 */
@WebService
@SOAPBinding(style = Style.RPC, use = Use.LITERAL)
public class Quoter extends AbstractQuotationService implements QuoterService {
	// All references are to be prefixed with an GP (e.g. GP001000)
	public static final String PREFIX = "GP";
	public static final String COMPANY = "Girl Power Inc.";

	public static final String PATH = "/quotation";

	public static int port = 9003;

	public static String mode = "jmdns";

	public static void main(String[] args) {
		try {
			if ((args != null) && (args.length > 0)) {
				argsToURLArray(args);
			}
			Endpoint endpoint = Endpoint.create(new Quoter());
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 5);
			server.setExecutor(Executors.newFixedThreadPool(5));
			HttpContext context = server.createContext(PATH);
			endpoint.publish(context);
			server.start();

			if (mode.equalsIgnoreCase("jmdns")) {
				// Create a JmDNS instance
				JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

				ServiceInfo serviceInfo = ServiceInfo.create("_quote._tcp.local.", COMPANY, port, "path=/quotation?wsdl");
				jmdns.registerService(serviceInfo);

				System.out.println("-- SERVICE -- :: " + COMPANY + "  :: -- Running with JmDNS --");
			}

			System.out.println("-- SERVICE -- :: " + COMPANY + "  :: -- UP --");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void argsToURLArray(String[] args) {
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "-p":
					port = Integer.parseInt(args[++i]);
					break;
				case "-m":
					mode = args[++i];
					break;
				default:
					System.out.println("Unknown flag: " + args[i] + "\n");
					System.out.println("Valid flags are:");
					System.out.println("\t-p <port>\tSpecify the port number of the target service");
					System.out.println("\t-m <mode>\tSpecify the mode(jndns/default) number of the target service");
					System.exit(0);
			}
		}
	}

	/**
	 * Quote generation:
	 * 50% discount for being female
	 * 20% discount for no penalty points
	 * 15% discount for < 3 penalty points
	 * no discount for 3-5 penalty points
	 * 100% penalty for > 5 penalty points
	 * 5% discount per year no claims
	 */
	@WebMethod
	public Quotation generateQuotation(ClientInfo info) {
		// Create an initial quotation between 600 and 1000
		double price = generatePrice(600, 400);

		// Automatic 50% discount for being female
		int discount = (info.gender == ClientInfo.FEMALE) ? 50:0;

		// Add a points discount
		discount += getPointsDiscount(info);

		// Add a no claims discount
		discount += getNoClaimsDiscount(info);

		// Generate the quotation and send it back
		return new Quotation(COMPANY, generateReference(PREFIX), (price * (100-discount)) / 100);
	}

	private int getNoClaimsDiscount(ClientInfo info) {
		return 5*info.noClaims;
	}

	private int getPointsDiscount(ClientInfo info) {
		if (info.points == 0) return 20;
		if (info.points < 3) return 15;
		if (info.points < 6) return 0;
		return -100;

	}


}
