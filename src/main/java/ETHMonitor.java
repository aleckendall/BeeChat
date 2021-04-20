import java.io.IOException;
import java.util.Stack;

public class ETHMonitor extends Monitor {

    public ETHMonitor() throws IOException {
        super();
        setSmsRetrievalBehavior(new AfricastalkingSMS());
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
                conv = new Conversation(new ETHHBF(phoneNumber));
                getConversations().put(phoneNumber, conv);
                System.out.println("Conversation with " + phoneNumber.toString() + " created.");
            }
        }
    }
}
