package quote;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.LinkedList;

@WebService
public interface BrokerService {

    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info);

}
