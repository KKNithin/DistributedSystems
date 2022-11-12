import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
public class LocalBrokerService implements BrokerService {

    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
        List<Quotation> quotations = new LinkedList<Quotation>();
        Registry registry = LocateRegistry.getRegistry();
        try {
            for (String name : registry.list()) {
                if (name.startsWith("qs-")) {
                    QuotationService service = (QuotationService) registry.lookup(name);
                    quotations.add(service.generateQuotation(info));
                }
            }
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        return quotations;
    }

    public void registerService(String name, QuotationService quotationService) {
        try {
            LocateRegistry.getRegistry().bind(name, quotationService);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
