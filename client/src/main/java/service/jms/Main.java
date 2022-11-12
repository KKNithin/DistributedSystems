package service.jms;

import javax.jms.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {


    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };

    public static ConcurrentMap<String, Integer> cache = new ConcurrentHashMap<>();
    static int SEED_ID = 1010100;
    public static int COUNT_DOWN = clients.length;

    /**
     * @param args
     */
    public static void main(String[] args) throws JMSException {

        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory =
                new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        Connection connection = factory.createConnection();
        connection.setClientID("client");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue reqQueue = session.createQueue("REQUESTS");
        Queue respQueue = session.createQueue("RESPONSES");

        MessageProducer producer = session.createProducer(reqQueue);
        MessageConsumer consumer = session.createConsumer(respQueue);

        connection.start();

        new Thread(new QuotationResponseHandler(consumer, connection)).start();

        for (ClientInfo client : clients) {
            QuotationRequestMessage quotationRequest = new QuotationRequestMessage(SEED_ID, client);
            Message request = session.createObjectMessage(quotationRequest);
            producer.send(request);
            cache.put(client.licenseNumber, SEED_ID++);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
