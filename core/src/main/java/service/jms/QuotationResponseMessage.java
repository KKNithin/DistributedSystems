package service.jms;

import java.io.Serializable;

public class QuotationResponseMessage implements Serializable {
    public long id;
    public Quotation quotation;
    public QuotationResponseMessage(long id, Quotation quotation) {
        this.id = id;
        this.quotation = quotation;
    }

    @Override
    public String toString() {
        return "QuotationResponseMessage{" +
                "id=" + id +
                ", quotation=" + quotation +
                '}';
    }
}
