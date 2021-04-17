import com.twilio.twiml.voice.Sim;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Monitor {
    protected SmsRetrievalBehavior smsRetrievalBehavior;
    protected HashMap<String, Conversation> conversations = new HashMap<>();

    public Monitor() {
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
    public Stack<AdapteeMessage> getNewMessages() throws Exception {
        return smsRetrievalBehavior.getNewMessages();
    }

    /*
     * Handle the messages received from the client.
     * Return:
     *      - void
     */
    public void processMessages() throws Exception {
        Stack<AdapteeMessage> newMessages = getNewMessages();
        for(AdapteeMessage msg : newMessages) {

            Conversation conv;
            String phoneNumber = msg.getFrom();

            if(getConversations().containsKey(phoneNumber)) {
                conv = getConversations().get(phoneNumber);
                conv.handleResponse(msg.getContent());
                if(conv.exit()) {
                    removeConversation(phoneNumber);
                    System.out.println("Conversation exited.");
                }
            } else {
                conv = new Conversation(new USAHBF(phoneNumber));
                getConversations().put(phoneNumber, conv);
                System.out.println("Conversation with " + phoneNumber.toString() + " created.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
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

        while(true) {
            for(Monitor monitor : monitors) {
                monitor.processMessages();
            }
        }
    }
}
