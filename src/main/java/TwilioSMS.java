import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Stack;

public class TwilioSMS implements SmsSendBehavior, SmsRetrievalBehavior {
    private final String Account_SID = "ACeeca045c291102da006adc6a38534c83";
    private final String Auth_Token = "ff19f6bb288e1a64467975134572f904";
    private final ZoneId zoneId = ZoneId.of(ZoneId.systemDefault().getId());
    private final PhoneNumber phoneNumber = new PhoneNumber("+12312412398");
    private ZonedDateTime lastReceived = ZonedDateTime.now();

    public TwilioSMS() {
        Twilio.init(Account_SID, Auth_Token);
    }

    /*
     * Send an sms message to a client given the number and the body of the message.
     * Return:
     *      - True: Message was successfully sent.
     *      - False: Message failed to send.
     */
    public boolean sendSMS(String to_, String body) {
        PhoneNumber to = new PhoneNumber(to_);
        try {
            Message message = Message.creator(to, phoneNumber, body).create();
        } catch (ApiException e) {
            return false;
        }

        return true;
    }


    /*
     * Get messages sent after a specified date and time.
     * Return:
     *      - Stack<AdapteeMessage>: a stack containing all new messages.
     */
    public Stack<AdapteeMessage> getNewMessages() {
        // read in all messages sent after the date of the last message read.
        ResourceSet<Message> messages = Message.reader().setDateSentAfter(lastReceived).setTo(phoneNumber).read();
        // Convert each message into an AdapteeMessage and add it to the stack.
        Stack<AdapteeMessage> newMessages = new Stack<>();
        LocalDateTime convDate;
        for(Message msg : messages) {
            if(msg.getDateSent().isAfter(lastReceived)) {
                newMessages.push(new AdapteeMessage(msg.getFrom().toString(), msg.getBody(), msg.getDateSent()));
                System.out.println(msg.getBody());
                lastReceived = msg.getDateSent();
            }
        }

        return newMessages;
    }
}
