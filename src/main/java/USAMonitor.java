public class USAMonitor extends Monitor {

    public USAMonitor() {
        super();
        setSmsRetrievalBehavior(new TwilioSMS());
    }
}
