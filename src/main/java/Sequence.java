import java.text.ParseException;

public abstract class Sequence {
    protected boolean exit;
    protected String response;
    protected Integer currentMsg = 0;
    protected Conversation conversation;
    protected boolean live = false;
    protected boolean paused = false;

    public Sequence(Conversation conversation) {
        this.conversation = conversation;
    }

    abstract void doCurrentMsg() throws ParseException;
    abstract void startSequence();
    abstract void endSequence();

    /*
     * Handle the client response.
     *
     * @return void
     */
    public void handleResponse(String response) throws ParseException {
        setResponse(response);
        System.out.println(conversation.getHoneyBeeFarmer().getTelephoneNumber().getPhoneNumber() + ": " + response);
        doCurrentMsg();
        return;
    }

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

    /*
     * Getter for ongoing
     * @return ongoing Indicates if the prompt has been initialized.
     */
    public boolean getLive() {
        return this.live;
    }

    /*
     * Setter for ongoing.
     * @return void
     * @param ongoing
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    /*
     * Set the paused field for the sequence class.
     * Return:
     *      - void
     */
    public void setPaused(boolean paused) { this.paused = paused; }

    /*
     * Get the paused field for the sequence class.
     * Return:
     *      - boolean: paused
     */
     public boolean getPaused() { return this.paused; }
}
