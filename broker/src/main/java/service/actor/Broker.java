package service.actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;
import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.*;

public class Broker extends AbstractActor {

    ArrayList<ActorRef> actorRefs = new ArrayList<>();
    HashMap<Integer, ArrayList<Quotation>> allQuoteResponses = new HashMap<>();
    HashMap<Integer, ClientInfo> allIDMap = new HashMap<>();

    HashMap<Integer, ActorRef> allClientReqMap = new HashMap<>();
    public static int SEED_ID = 111;

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Init.class, msg -> {
            System.out.println("###   Broker   ---   Started   ###");
        }).match(String.class, msg -> {
            if (!msg.equals("register")) {
                return;
            }
            actorRefs.add(getSender());
        }).match(ApplicationRequest.class, msg -> {
            allQuoteResponses.put(SEED_ID, new ArrayList<>());
            allIDMap.put(SEED_ID, msg.getClientInfo());
            allClientReqMap.put(SEED_ID, getSender());
            for (ActorRef ref : actorRefs) {
                ref.tell(new QuotationRequest(SEED_ID, msg.getClientInfo()), getSelf());
            }
            getContext().system().scheduler().scheduleOnce(
                    Duration.create(2, TimeUnit.SECONDS),
                    getSelf(),
                    new RequestDeadline(SEED_ID++),
                    getContext().dispatcher(), null);
        }).match(QuotationResponse.class, msg -> {
            System.out.println("Received Quotation Response (ID: " + msg.getId() + ")");
            allQuoteResponses.get(msg.getId()).add(msg.getQuotation());
        }).match(RequestDeadline.class, msg -> {
            System.out.println("Sending Quotation Response (ID: " + msg.getSeedId() + ")");
            int id = msg.getSeedId();
            allClientReqMap.get(id).tell(new ApplicationResponse(allIDMap.get(id),
                    allQuoteResponses.get(id)), getSelf());
        }).build();
    }
}
