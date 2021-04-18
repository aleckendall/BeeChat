import java.io.IOException;
import java.util.ArrayList;

public class ETHHBF extends HoneyBeeFarmer {
    public ETHHBF(String telephoneNumber) throws IOException {
        super(telephoneNumber);
        setSmsSendBehavior(new AfricastalkingSMS());
        getLocation().setCountry("ETH");
    }

    public ETHHBF(String firstName, String lastName, String telephoneNumber, String id, ArrayList<Apiary> apiaries, Location location) throws IOException {
        super(firstName, lastName, telephoneNumber, id, apiaries, location);
        setSmsSendBehavior(new AfricastalkingSMS());
    }
}
