import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import quote.ClientInfo;
import quote.Quotation;
import quote.Quoter;
import quote.QuoterService;

public class AuldfellasQuoterTest {

    public Quoter auldfellasQuoter = new Quoter();

    @BeforeClass
    public static void before() {
        Endpoint.publish("http://0.0.0.0:9000/getQuote", new Quoter());
        Quoter.main(null);
    }

    @Test
    public void testServer() throws MalformedURLException {
        URL wsdlUrl = new
                URL("http://0.0.0.0:9000/getQuote?wsdl");
        QName serviceName =
                new QName("http://quote/", "QuoterService");
        Service service = Service.create(wsdlUrl, serviceName);
        QName portName = new QName("http://quote/", "QuoterPort");
        QuoterService quotationService =
                service.getPort(portName, QuoterService.class);
        Assert.assertNotNull(quotationService.generateQuotation(new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")));
    }

    @Test
    public void testMain() throws MalformedURLException {
        URL wsdlUrl = new
                URL("http://localhost:9001/quotation?wsdl");
        QName serviceName =
                new QName("http://quote/", "QuoterService");
        Service service = Service.create(wsdlUrl, serviceName);
        QName portName = new QName("http://quote/", "QuoterPort");
        QuoterService quotationService =
                service.getPort(portName, QuoterService.class);
        Assert.assertNotNull(quotationService.generateQuotation(new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")));
    }

    @Test
    public void testGetQuotation() {
        Quotation quote = auldfellasQuoter.generateQuotation(new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9"));
        Assert.assertEquals(Quoter.COMPANY, quote.company);
    }
}
