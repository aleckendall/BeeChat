import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Monitor {

    // move the honeybeefarmer to the conversation object and add the hashmap that keeps track of active conversations.
    private HoneyBeeFarmer hbf;
    protected static ZonedDateTime lastReceived;
    protected SmsRetrievalBehavior smsRetrievalBehavior;
    private HashMap<PhoneNumber, Conversation> activeConv = new HashMap<>();

    // hashmap that is keyed on the telephone number of the honeybeefarmer. the value is the active conversation corresponding to the honey bee farmer.

    //FUTURE DEV: change the string in the hashmap to a telephone object after I've implemented the class... decide on methods for the TelephoneNumber class.
    // private static HashMap<String, Conversation> activeConvos = new HashMap<String, Conversation>();

    public Monitor() {
        setLastReceived(ZonedDateTime.now());
    }

    /*
     * Get the lastReceived.
     */
    public ZonedDateTime getLastReceived() {
        return lastReceived;
    }

    /*
     * Set lastReceived.
     */
    protected void setLastReceived(ZonedDateTime lr) {
        this.lastReceived = lr;
    }

    /*
     * Get activeConv.
     * Return:
     * - HashMap<PhoneNumber, Conversation> () activeConv: contains all active conversations
     */
    public HashMap<PhoneNumber, Conversation> getActiveConv() { return this.activeConv; }

    /*
     * Set activeConv.
     * Return:
     * - void
     */
    public void setActiveConv(HashMap<PhoneNumber, Conversation> activeConv) {
        this.activeConv = activeConv;
    }

    /*
     * Set SmsRetrievalBehavior.
     * Return:
     *      - void
     */
    public void setSmsRetrievalBehavior(SmsRetrievalBehavior srb) {
        this.smsRetrievalBehavior = srb;
    }

    /*
     * Get all messages received after lastReceived.
     * Returns:
     *  - Stack<AdapteeMessage>: the messages that were sent to the API service after the specified date.
     */
    public Stack<AdapteeMessage> getNewMessages() {
        return smsRetrievalBehavior.getNewMessages(lastReceived);
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Monitor> monitors;
        MonitorFactory mc = new MonitorFactory();

        // create a monitor for each monitor that is specified within the command line arguments.
        if(args.length > 0) {
            // FUTURE DEV: create a thread for each monitor that is specified.
            monitors = new ArrayList<Monitor>();
            for(int i = 0; i < args.length; i++) {
                monitors.add(mc.createMonitor(args[i]));
            }
        }
        // throw an error is a monitor was not specified within the command line arguments.
        else {
            throw new IllegalArgumentException("No monitor(s) specified.");
        }

        // send myself an sms
        HoneyBeeFarmer hbf = new USAHBF("Alec", "Kendall", "+19198306807", "0", null, null);

        // continuously poll the monitor two-way sms service provider, checking for new messages that haven't been processed.
        while(true) {
            // check to see if a new message was received after the date of lastReceived.
            Stack<AdapteeMessage> newMessages = monitors.get(0).getNewMessages();
            for(AdapteeMessage msg : newMessages) {
                if(msg.getDateSent().isAfter(lastReceived)) {
                    monitors.get(0).setLastReceived(msg.getDateSent());
                    System.out.println(monitors.get(0).getLastReceived());
                }
                // check to see if the phone number is within the hashmap of active conversations
                PhoneNumber phoneNumber = new PhoneNumber(msg.getFrom());
                Conversation conv;
                if(monitors.get(0).getActiveConv().containsKey(phoneNumber)) {
                    conv = monitors.get(0).getActiveConv().get(phoneNumber);
                    conv.handleResponse(msg.getContent());
                } else {
                    conv = new Conversation(new USAHBF(phoneNumber.toString()));
                }
                boolean convNextResult = conv.sendNext();
                if(!convNextResult) {
                    conv.exitConv();
                    monitors.get(0).getActiveConv().remove(phoneNumber);
                }
            }
        }
    }
}
