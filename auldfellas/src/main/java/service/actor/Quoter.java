package service.actor;

import akka.actor.*;
import service.core.Quotation;
import service.core.QuotationService;
import service.messages.Init;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class Quoter extends AbstractActor {
    private QuotationService service;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Init.class,
                        msg -> {
                            service = msg.getService();
                            System.out.println("###   Auldfellas   ---   Started   ###");
                        })
                .match(QuotationRequest.class,
                        msg -> {
                            System.out.println("Quotation Request Received (ID: " + msg.getId() + ")");
                            Quotation quotation =
                                    service.generateQuotation(msg.getClientInfo());
                            getSender().tell(
                                    new QuotationResponse(msg.getId(), quotation), getSelf());
                        }).build();
    }
}
