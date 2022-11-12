package quote;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.LinkedList;

@WebService
public class BrokerServiceTest implements BrokerService{
    @Override
    @WebMethod
    public LinkedList<Quotation> getQuotations(ClientInfo info) {
        return null;
    }
}
