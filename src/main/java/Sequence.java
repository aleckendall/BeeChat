import java.util.Stack;

public abstract class Sequence {
    protected boolean exit;
    protected String response;
    protected Integer currentMsg = 0;
    protected Conversation conversation;

    public Sequence(Conversation conversation) {
        this.conversation = conversation;
    }

    abstract void handleResponse(String response);
    abstract void doCurrentMsg();
    abstract void startSequence();
    abstract void endSequence();

    /*
     * Get the response for the sequence.
     * Return:
     *      - String: response
     */
    public String getResponse() {
        return this.response;
    }

    /*
     * Set the response for the sequence.
     * Return:
     *      - void
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /*
     * Get the exit flag for the prompt.
     * Return:
     *      - boolean: exit
     */
    public boolean getExit() {
        return this.exit;
    }

    /*
     * Set the exit flag for the prompt.
     * Return:
     *      - void
     */
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    /*
     * Determine if the sequence should be exited.
     * Return:
     *      - boolean: exit
     *          - true: exit sequence
     *          - false: do not exit sequence
     */
    public boolean exitResponse() {
        return exit;
    }
}
