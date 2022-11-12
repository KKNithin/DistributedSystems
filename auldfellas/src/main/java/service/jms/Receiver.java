package service.jms;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {

    public static AFQService service = new AFQService();

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory =
                new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection();
            connection.setClientID("auldfellas");
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue("QUOTATIONS");
            Topic topic = session.createTopic("APPLICATIONS");
            MessageConsumer consumer = session.createConsumer(topic);
            MessageProducer producer = session.createProducer(queue);
            connection.start();
            while (true) {
                // Get the next message from the APPLICATION topic
                Message message = consumer.receive();
                // Check it is the right type of message
                if (message instanceof ObjectMessage) {
                    // It’s an Object Message
                    Object content = ((ObjectMessage) message).getObject();
                    if (content instanceof QuotationRequestMessage) {
                        // It’s a Quotation Request Message
                        QuotationRequestMessage request = (QuotationRequestMessage) content;
                        // Generate a quotation and send a quotation response message…
                        Quotation quotation = service.generateQuotation(request.info);
                        Message response = session.createObjectMessage(
                                new QuotationResponseMessage(request.id, quotation));
                        producer.send(response);
                    }
                } else {
                    System.out.println("Unknown message type: " +
                            message.getClass().getCanonicalName());
                }
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }


    }
}
