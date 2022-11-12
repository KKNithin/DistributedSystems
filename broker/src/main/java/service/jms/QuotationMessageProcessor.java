package service.jms;

import javax.jms.*;
import java.util.List;

public class QuotationMessageProcessor implements Runnable {
    public static Connection connection;
    public static Session session;
    public static MessageConsumer consumer;
    public static MessageProducer respProducer;

    public QuotationMessageProcessor(Connection connection, Session session, MessageConsumer consumer, MessageProducer respProducer) {
        this.connection = connection;
        this.session = session;
        this.consumer = consumer;

        this.respProducer = respProducer;
    }

    @Override
    public void run() {
        while (true) {
            // Get the next message from the QUOTATIONS queue
            Message message = null;
            try {
                message = consumer.receive();
                // Check it is the right type of message
                if (message instanceof ObjectMessage) {
                    // It’s an Object Message
                    Object content = ((ObjectMessage) message).getObject();
                    if (content instanceof QuotationResponseMessage) {
                        // It’s a Quotation from one of the Quotation Services
                        QuotationResponseMessage response = (QuotationResponseMessage) content;
                        ClientInfo respInfo = Broker.cache.get(response.id);
                        if (Broker.allQuotations.get(respInfo) != null) {
                            List<Quotation> respQuote = Broker.allQuotations.get(respInfo);
                            respQuote.add(response.quotation);
                            Broker.allQuotations.put(respInfo, respQuote);
                        }
                        if (Broker.allQuotations.get(respInfo).size() == 3) {
                            // Generate a client application message for client and send on RESPONSES Queue
                            ClientApplicationMessage clientMsg = new ClientApplicationMessage(respInfo, Broker.allQuotations.get(respInfo));
                            Message clientMsgResponse = session.createObjectMessage(clientMsg);
                            respProducer.send(clientMsgResponse);
                            Broker.allQuotations.remove(respInfo);
                        }
                    }
                    message.acknowledge();
                    Thread.sleep(500);
                } else {
                    System.out.println("Unknown message type: " +
                            message.getClass().getCanonicalName());
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
    }
}
