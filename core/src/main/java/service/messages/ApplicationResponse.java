package service.messages;

import java.util.ArrayList;

import service.core.ClientInfo;
import service.core.Quotation;

public class ApplicationResponse implements MySerializable{
    private ClientInfo info;
    private ArrayList<Quotation> quotes;

    public ApplicationResponse(ClientInfo info, ArrayList<Quotation> quotes) {
        this.info = info;
        this.quotes = quotes;
    }

    public ApplicationResponse() {
    }

    public ClientInfo getInfo() {
        return info;
    }

    public void setInfo(ClientInfo info) {
        this.info = info;
    }

    public ArrayList<Quotation> getQuotes() {
        return quotes;
    }

    public void setQuotes(ArrayList<Quotation> quotes) {
        this.quotes = quotes;
    }
}
