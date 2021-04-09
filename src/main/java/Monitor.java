import com.twilio.twiml.voice.Sim;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Monitor {
    protected ZonedDateTime lastReceived;
    protected SmsRetrievalBehavior smsRetrievalBehavior;
    private HashMap<String, Conversation> conversations = new HashMap<>();

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
     * Add an active conversations to the monitor.
     */
    public void addConversation(Conversation conv) {
        conversations.put(conv.getHoneyBeeFarmer().getTelephoneNumber().toString(), conv);
    }

    /*
     * Remove an active conversations from the monitor.
     */
    public void removeConversation(String phoneNumber) {
        this.conversations.remove(phoneNumber);
    }

    /*
     * Get activeConv.
     * Return:
     * - HashMap<String, conversations> () activeConv: contains all active conversations
     */
    public HashMap<String, Conversation> getConversations() { return this.conversations; }

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

    public static void main(String[] args) {
        ArrayList<Monitor> monitors;
        MonitorFactory mc = new MonitorFactory();
        if(args.length > 0) {
            monitors = new ArrayList<>();
            for(String arg : args) {
                monitors.add(mc.createMonitor(arg));
            }
        }
        else {
            throw new IllegalArgumentException("No monitor(s) specified.");
        }

        HoneyBeeFarmer hbf = new USAHBF("+19198306807");
        hbf.sendSMS("Hello");

        // Poll APIs for inbound messages
        boolean started = false;
        while(!started || monitors.get(0).getConversations().size() > 0) {
            Stack<AdapteeMessage> newMessages = monitors.get(0).getNewMessages();
            for(AdapteeMessage msg : newMessages) {
                if(msg.getDateSent().isAfter(monitors.get(0).getLastReceived())) {
                    monitors.get(0).setLastReceived(msg.getDateSent());
                }
                started = true;

                Conversation conv;
                String phoneNumber = msg.getFrom();
                if(monitors.get(0).getConversations().containsKey(phoneNumber)) {
                    conv = monitors.get(0).getConversations().get(phoneNumber);
                    conv.handleResponse(msg.getContent());
                    if(conv.exit()) {
                        monitors.get(0).removeConversation(phoneNumber);
                    }
                } else {
                    conv = new Conversation(new USAHBF(phoneNumber));
                    monitors.get(0).getConversations().put(phoneNumber, conv);
                }
            }
        }
        System.out.println(SimulateHoneyBeeFarmerDB.getInstance().getHoneyBeeFarmer("+19198306807"));
    }
}
