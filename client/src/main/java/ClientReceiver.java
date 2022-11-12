import akka.actor.AbstractActor;
import service.core.Quotation;
import service.messages.ApplicationResponse;

public class ClientReceiver extends AbstractActor{
    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(ApplicationResponse.class,
                        msg -> {
                                Client.displayProfile(msg.getInfo());
                                for(Quotation quotation : msg.getQuotes()) {
                                    Client.displayQuotation(quotation);
                                }
                                System.out.println("\n");
                        }).build();
    }
}
