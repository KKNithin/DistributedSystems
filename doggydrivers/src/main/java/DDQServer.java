import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class DDQServer {
    public static void main(String[] args) {
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            Registry registry = null;
            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                registry = LocateRegistry.getRegistry(args[0], 1099);
            }
            // Create the Remote Object
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(new DDQService(), 0);
            // Register the object with the RMI Registry
            BrokerService broker = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
            broker.registerService(Constants.DODGY_DRIVERS_SERVICE, quotationService);
            System.out.println("STOPPING SERVER SHUTDOWN for : " + DDQService.COMPANY);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
