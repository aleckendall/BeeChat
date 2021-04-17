import com.africastalking.AfricasTalking;
import com.africastalking.SmsService;
import com.africastalking.sms.Message;
import com.africastalking.sms.Recipient;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Stack;

public class AfricastalkingSMS implements SmsSendBehavior, SmsRetrievalBehavior {
    private final String username = "HiveTracks";
    private final String apiKey = "2380d5d7221599abc834ce907425f19a2374a3e36dcfaeddb19965347a1da003";
    private final String shortCode = "28161";
    private long lastReceivedId = 0;
    private ZonedDateTime lastReceived = ZonedDateTime.now();
    private SmsService sms;

    public AfricastalkingSMS() throws IOException {
        AfricasTalking.initialize(username, apiKey);
        sms = AfricasTalking.getService(AfricasTalking.SERVICE_SMS);
        setLastReceivedId();
    }


    /*
     * Set lastReceivedId to the the id of the first message that comes at or after the date / time
     * that this class was instantiated.
     * Return:
     *      - void
     */
    private void setLastReceivedId() throws IOException {
        List<Message> messageList = sms.fetchMessages(lastReceivedId);
        for(Message msg : messageList) {
            ZonedDateTime dateTime = ZonedDateTime.parse(msg.date);
            if(!dateTime.isBefore(lastReceived)) {
                lastReceivedId = msg.id;
                break;
            }
        }

        System.out.println("Last received message ID " + lastReceivedId);
    }

    /*
     * Get all messages received after the last known received message Id
     * Return:
     *      - Stack<AdapteeMessage> newMessages The new messages pulled down from AfricasTalking
     */
    public Stack<AdapteeMessage> getNewMessages() throws IOException {
        Stack<AdapteeMessage> newMessages = new Stack<>();
        newMessages.trimToSize();

        // no new messages have been received
        if(lastReceivedId == 0)
            return newMessages;

        List<Message> messageList = sms.fetchMessages(lastReceivedId);
        for(Message msg : messageList) {
            ZonedDateTime dateTime = ZonedDateTime.parse(msg.date);
            newMessages.push(new AdapteeMessage(msg.from, msg.text, dateTime));
            System.out.println(dateTime);
        }

        return newMessages;
    }

    @Override
    /*
     * Send an sms message to a phone number in a region supported by AfricasTalking.
     * Return:
     *      - boolean: true Sent successfully
     *                 false Not successfully sent
     */
    public boolean sendSMS(String telephoneNumber, String body) {

        String[] recipients = new String[] {
                telephoneNumber, telephoneNumber
        };

        try {
            List<Recipient> response = sms.send(body, shortCode, recipients, true);
            for(Recipient recipient : response) {
                return recipient.status.compareTo("Success") == 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
