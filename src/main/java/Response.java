import java.util.Date;

public class Response {
    private Date timestamp;
    private String body;

    public Response() {

    }

    public Response(Date timestamp, String body) {
        this.body = body;
        this.timestamp = timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }
}
