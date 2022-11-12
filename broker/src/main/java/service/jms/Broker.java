package service.jms;

import javax.jms.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Broker {
    public static Map<Long, ClientInfo> cache = new HashMap<>();
    static long SEED_ID = 901010100;
    public static ConcurrentMap<ClientInfo, List<Quotation>> allQuotations = new ConcurrentHashMap<>();

    public static void main(String[] args) throws JMSException {
        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        Connection connection = factory.createConnection();
        connection.setClientID("broker");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        Queue queue = session.createQueue("QUOTATIONS");
        Topic topic = session.createTopic("APPLICATIONS");
        Queue requestQueue = session.createQueue("REQUESTS");
        Queue responseQueue = session.createQueue("RESPONSES");

        MessageProducer producer = session.createProducer(topic);
        MessageConsumer consumer = session.createConsumer(queue);
        MessageConsumer reqConsumer = session.createConsumer(requestQueue);
        MessageProducer respProducer = session.createProducer(responseQueue);

        connection.start();

        //Start the consumer thread to listen to the client messages on REQUESTS Queue
        Thread clientMessageConsumer = new Thread(new ClientMessageConsumer(session, producer, reqConsumer));
        try {
            clientMessageConsumer.start();
        } catch (IllegalThreadStateException e) {
            System.out.println(e);
        }

        //Start the processor thread to listen to the quotation service messages on QUOTATIONS Queue and send to client
        Thread quotationMessageProcessor = new Thread(new QuotationMessageProcessor(connection, session, consumer, respProducer));
        try {
            quotationMessageProcessor.start();
        } catch (IllegalThreadStateException e) {
            System.out.println(e);
        }
    }
}

