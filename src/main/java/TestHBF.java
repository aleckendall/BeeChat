import java.util.ArrayList;

public class TestHBF extends HoneyBeeFarmer {
    public TestHBF(String telephoneNumber) {
        super(telephoneNumber);
        setSmsSendBehavior(new TestSMS());
    }

    public TestHBF(String firstName, String lastName, String telephoneNumber, String id, ArrayList<Apiary> apiaries, Location location) {
        super(firstName, lastName, telephoneNumber, id, apiaries, location);
        setSmsSendBehavior(new TestSMS());
    }
}