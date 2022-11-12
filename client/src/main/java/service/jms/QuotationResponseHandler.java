package service.jms;

import javax.jms.*;
import java.text.NumberFormat;

public class QuotationResponseHandler implements Runnable {

    public static MessageConsumer consumer;
    public static Connection connection;

    public QuotationResponseHandler(MessageConsumer consumer, Connection connection) {
        this.consumer = consumer;
        this.connection = connection;
    }

    @Override
    public void run() {
        while (true) {
            // Get the next message from the RESPONSES queue
            Message message = null;
            try {
                //Close the connection if the count-down is 0
                if (Main.COUNT_DOWN == 0) {
                    connection.close();
                    break;
                }
                message = consumer.receive();
                // Check it is the right type of message
                if (message instanceof ObjectMessage) {
                    // It’s an Object Message
                    Object content = ((ObjectMessage) message).getObject();
                    if (content instanceof ClientApplicationMessage) {
                        // It’s a Client Application Message from Broker
                        ClientApplicationMessage response = (ClientApplicationMessage) content;
                        //Verify the validity of the Client by comparing the license number
                        if (Main.cache.get(response.clientInfo.licenseNumber) != null) {
                            displayProfile(response.clientInfo);
                            for (Quotation quote : response.quotations) {
                                displayQuotation(quote);
                            }
                            message.acknowledge();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("\n");
                        }
                    }
                    Main.COUNT_DOWN--;
                } else {
                    System.out.println("Unknown message type: " +
                            message.getClass().getCanonicalName());
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender == ClientInfo.MALE ? "Male" : "Female")) +
                        " | Age: " + String.format("%1$-30s", info.age) + " |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.licenseNumber) +
                        " | No Claims: " + String.format("%1$-24s", info.noClaims + " years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.points) + " |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price)) + " |");
        System.out.println("|=================================================================================================================|");
    }
}
