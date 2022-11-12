import static org.junit.Assert.assertNotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * @Author 22200096
 */
public class BrokerUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup() {
        try {
            registry = LocateRegistry.createRegistry(1099);
            BrokerService brokerService = (BrokerService)
                    UnicastRemoteObject.exportObject(new LocalBrokerService(), 0);
            registry.bind(Constants.BROKER_SERVICE, brokerService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        BrokerService service = (BrokerService)
                registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQutationWithoutServicesTest() {
        BrokerService brokerService = new LocalBrokerService();
        ClientInfo info = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1");
        List<Quotation> quote;
        try {
            quote = brokerService.getQuotations(info);
            Assert.assertEquals(0, quote.size());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void registerServiceTest() {
        try {
            BrokerService brokerService = new LocalBrokerService();
            QuotationService quotation = (QuotationService)
                    UnicastRemoteObject.exportObject(new AbstractQuotationService() {
                        public Quotation generateQuotation(ClientInfo info) throws RemoteException {
                            return null;
                        }
                    }, 0);
            brokerService.registerService(Constants.AULD_FELLAS_SERVICE, quotation);
            Assert.assertNotNull(registry.lookup(Constants.AULD_FELLAS_SERVICE));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }

    }
}