package service.jms;

import javax.jms.*;
import java.util.ArrayList;

public class ClientMessageConsumer implements Runnable {
    public static Session session;
    public static MessageProducer producer;
    public static MessageConsumer reqConsumer;

    public ClientMessageConsumer(Session session, MessageProducer producer, MessageConsumer reqConsumer) {
        this.session = session;
        this.producer = producer;
        this.reqConsumer = reqConsumer;
    }

    @Override
    public void run() {
        while (true) {
            // Get the next message from the REQUESTS queue
            Message message = null;
            try {
                message = reqConsumer.receive();
                // Check it is the right type of message
                if (message instanceof ObjectMessage) {
                    // It’s an Object Message
                    Object content = ((ObjectMessage) message).getObject();
                    if (content instanceof QuotationRequestMessage) {
                        // It’s a Quotation Request Message from client
                        QuotationRequestMessage clientReqMessage = (QuotationRequestMessage) content;
                        Broker.allQuotations.put(clientReqMessage.info, new ArrayList<Quotation>());
                        // Generate a quotation request message for clients and send on APPLICATIONS Topic
                        QuotationRequestMessage quotationRequest =
                                new QuotationRequestMessage(Broker.SEED_ID, clientReqMessage.info);
                        Message msgRequest = session.createObjectMessage(quotationRequest);
                        Broker.cache.put(Broker.SEED_ID++, quotationRequest.info);
                        producer.send(msgRequest);

                        message.acknowledge();
                    }
                } else {
                    System.out.println("Unknown message type: " +
                            message.getClass().getCanonicalName());
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
        }
    }
}
