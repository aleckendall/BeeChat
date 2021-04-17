import java.util.Stack;

public interface SmsRetrievalBehavior {
    Stack<AdapteeMessage> getNewMessages() throws Exception;
}
