import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class AdapteeMessage {
    private final PhoneNumber from;
    private final String content;
    private final ZonedDateTime dateSent;

    public AdapteeMessage(String from, String content, ZonedDateTime dateSent) {
        this.from = new PhoneNumber(from);
        this.content = content;
        this.dateSent = dateSent;
    }

    /*
     * Get the phone number the message was sent from.
     * Returns:
     *  - String: the telephone number
     */
    public String getFrom() {return this.from.getPhoneNumber();}

    /*
     * Get the content of the message.
     * Returns:
     *  - String: content
     */
    public String getContent() {return this.content;}

    /*
     * Get the date that the message was sent.
     * Returns:
     *  - LocalDateTime: dateSent
     */
    public ZonedDateTime getDateSent() { return this.dateSent; }

}
