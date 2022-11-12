import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BrokerServer {
    public static void main(String[] args) {
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            BrokerService brokerService = new LocalBrokerService();
            Registry registry = null;
            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                registry = LocateRegistry.getRegistry(args[0], 1099);
            }
            // Create the Remote Object
            BrokerService broker = (BrokerService)
                    UnicastRemoteObject.exportObject(brokerService, 0);
            // Register the object with the RMI Registryd
            registry.bind(Constants.BROKER_SERVICE, broker);
            System.out.println("STOPPING SERVER SHUTDOWN for : " + Constants.BROKER_SERVICE);
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
}
