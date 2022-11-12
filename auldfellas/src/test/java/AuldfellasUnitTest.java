import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import static org.junit.Assert.assertNotNull;
import org.junit.*;

/**
 *
 * @Author 22200096
 */
public class AuldfellasUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        QuotationService afqService = new AFQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(afqService, 0);
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQutationTest() {
        QuotationService afqService = new AFQService();
        ClientInfo info = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1");
        Quotation quote;
        try {
            quote = afqService.generateQuotation(info);
            Assert.assertNotNull(quote);
            Assert.assertEquals(AFQService.COMPANY, quote.company);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}