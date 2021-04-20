import java.util.Stack;

public class TestMonitor extends Monitor {

    public TestMonitor() {
        super();
        setSmsRetrievalBehavior(new TestSMS());
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
                System.out.println("Conversation with " + phoneNumber.toString() + " created.");
                conv = new Conversation(new TestHBF(phoneNumber));
                getConversations().put(phoneNumber, conv);
            }
        }
    }

}
