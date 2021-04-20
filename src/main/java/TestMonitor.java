public class TestMonitor extends Monitor {

    public TestMonitor() {
        super();
        setSmsRetrievalBehavior(new TestSMS());
    }

}
