import java.time.ZonedDateTime;
import java.util.Stack;

public interface SmsRetrievalBehavior {
    Stack<AdapteeMessage> getNewMessages(ZonedDateTime lastReceived);
}
