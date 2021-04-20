import org.eclipse.jetty.security.PropertyUserStore;

import java.text.ParseException;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Conversation {
    private HoneyBeeFarmer hbf;
    private Stack<Sequence> sequences;
    private boolean exit = false;
    private SimulateHoneyBeeFarmerDB database = SimulateHoneyBeeFarmerDB.getInstance();

    public Conversation(HoneyBeeFarmer hbf) {
        this.hbf = hbf;
        init();
    }


    /*
     * Get the simulated database.
     * @return SimulateHoneyBeeFarmerDB
     */
    public SimulateHoneyBeeFarmerDB getDatabase() {
        return this.database;
    }

    /*
     * Set the honey bee farmer.
     * Return:
     *      - void
     */
    public void setHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        this.hbf = hbf;
    }

    /*
     * Get the honey bee farmer.
     * Return:
     *      - HoneyBeeFarmer: hbf
     */
    public HoneyBeeFarmer getHoneyBeeFarmer() {
        return this.hbf;
    }

    /*
     * Indicates whether or not the conversation should be exited.
     *
     */
    public boolean exit() {
        return exit;
    }

    /*
     * Add the appropriate Sequence to the Sequence Stack.
     * Onboard if their phone number is not in the database.
     * Main Menu if it is.
     *
     * @return void
     */
    public void init() {
        sequences = new Stack<>();
        sequences.trimToSize();
        if(hbf.exists()) {
            sequences.push(new MainMenu(this));
        } else {
            sequences.push(new Onboard(this));
        }
        sequences.peek().startSequence();
    }

    /*
     * Add a Sequence to the Sequence Stack.
     *
     * @return void
     * @param Sequence to add to the Sequence Stack
     */
    public void addSequence(Sequence sequence) {
        this.sequences.push(sequence);
    }

    /*
     * Pass the response to the active sequence within the conversation.
     *
     * @return void
     * @param resp The response from the honey bee farmer.
     */
    public void handleResponse(String resp) throws ParseException {

        if(sequences.size() == 0) {
            exit = true;
            return;
        }

        // start sequence if not already.
        if(!sequences.peek().getLive()) {
            sequences.peek().startSequence();
        } else {
            // continue with sequence if started
            sequences.peek().handleResponse(resp);

            while (sequences.peek().getExit() || !sequences.peek().getLive() || sequences.peek().getPaused() ) {
                if (sequences.peek().getExit()) {
                    sequences.pop();
                    if(sequences.size() == 0) {
                        exit = true;
                        return;
                    }
                } else if (!sequences.peek().getLive()) {
                    sequences.peek().startSequence();
                } else {
                    sequences.peek().handleResponse("");
                }
            }
        }
    }
}
