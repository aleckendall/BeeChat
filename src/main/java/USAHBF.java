import java.util.ArrayList;

public class USAHBF extends HoneyBeeFarmer {
    public USAHBF(String telephoneNumber) {
        super(telephoneNumber);
        setSmsSendBehavior(new TwilioSMS());
    }
    public USAHBF(String firstName, String lastName, String telephoneNumber, String id, ArrayList<Apiary> apiaries, Location location) {
        super(firstName, lastName, telephoneNumber, id, apiaries, location);
        setSmsSendBehavior(new TwilioSMS());
    }
}