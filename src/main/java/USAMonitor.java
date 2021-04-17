import java.time.ZonedDateTime;
import java.util.Stack;

public class USAMonitor extends Monitor {

    public USAMonitor() {
        super();
        setSmsRetrievalBehavior(new TwilioSMS());
    }

}
