package quote;

import javax.xml.ws.Endpoint;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class ClientTest {

    @BeforeClass
    @Ignore
    public static void before() {
        Endpoint.publish("http://0.0.0.0:9000/broker",  new BrokerServiceTest());
    }

    @Test
    @Ignore
    public void mainTest() {
        String[] args = {"-h","0.0.0.0","-p","9000"};
        Client.main(args);
    }

    @Test
    public void argsToValuesBlankTest() {
        String[] blankArgs = {};
        Assert.assertFalse(Client.argsToValues(blankArgs));
    }

    @Test
    public void argsToValuesTest() {
        String[] args = {"-h","localhost","-p","1111"};
        Assert.assertTrue(Client.argsToValues(args));
    }
}
