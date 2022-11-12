package service.jms;

import java.io.Serializable;
import java.util.List;

public class ClientApplicationMessage implements Serializable {
    public ClientInfo clientInfo;
    public List<Quotation> quotations;

    public ClientApplicationMessage(ClientInfo clientInfo, List<Quotation> quotations) {
        this.clientInfo = clientInfo;
        this.quotations = quotations;
    }

    @Override
    public String toString() {
        return "ClientApplicationMessage{" +
                "clientInfo=" + clientInfo +
                ", quotations=" + quotations +
                '}';
    }
}
