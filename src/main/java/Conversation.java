import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.util.LinkedList;

public class Conversation {
    private HoneyBeeFarmer hbf;
    private LinkedList<Prompt> promptLinkedList;
    private Prompt currPrompt;

    public Conversation(HoneyBeeFarmer hbf) throws IOException {
        this.hbf = hbf;
        // populate the prompted linked list with the initial prompt or on-boarding promptLL.
        initPrompts();
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
     * Initialize promptLinkedList to the appropriate Prompt story.
     * Return:
     *      - void
     */
    public void initPrompts() throws IOException {
        // check if the client exists
        String initPromptFileName;
        if(hbf.exists()) {
            initPromptFileName = "MainMenu.json";
        } else {
            initPromptFileName = "Onboard.json";
        }
        promptLinkedList = importPrompt(initPromptFileName);
    }

    /*
     * Given a prompt title, parse the file with the name matching the prompt title, and transform it into a LinkedList
     * of prompts.
     * Return:
     *      - LinkedList<Prompt> promptStory.
     * Throw:
     *      - IOException
     */
    private LinkedList<Prompt> importPrompt(String filename) throws IOException {
        //load the prompt sequence file.
        Gson gson = new Gson();
        LinkedList<Prompt> promptStory = null;
        try (FileReader reader = new FileReader(filename)) {
            // Convert JSON file to a LinkedList of prompts
            Type listType = new TypeToken<LinkedList<Prompt>>(){}.getType();
            promptStory = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Prompt p : promptStory) {
            System.out.println(p);
        }
        return promptStory;
    }

    /*
     * Pass the response to the prompt for processing. Add new prompts to the promptLL if needed.
     * Return:
     * - void
     */
    public void handleResponse(String resp) throws IOException {

        // quit the conversation, save the data of the honey bee farmer.
        if(resp.compareTo("QUIT") == 0) {
            exitConv();
            return;
        }

        // send the response to the prompt
        this.currPrompt.setClientResponse(resp);

        // push new prompts to the beginning of the promptLL if a new prompt file is specified.
        if(this.currPrompt.getInsertPromptSeq().compareTo("") != 0) {
            // insert the new prompts
            LinkedList<Prompt> newPrompts = importPrompt(this.currPrompt.getInsertPromptSeq());
            newPrompts.addAll(promptLinkedList);
            promptLinkedList = newPrompts;
        }

        return;
    }

    /*
     * Send the next prompt to the honey bee farmer.
     * Return:
     * - boolean
     *      - true: there was a prompt to be sent and it was sent to the honey bee farmer.
     *      - false: there was not a prompt to be sent.
     */
    public boolean sendNext() {

        if(promptLinkedList != null) {
            currPrompt = promptLinkedList.pop();
            hbf.sendSMS(currPrompt.currPrompt());
            return true;
        }

        return false;
    }

    /*
     * Exit the conversation.
     * Return:
     * - void
     */
    public void exitConv() {
        // save the honey bee farmer data.
        hbf.saveHoneyBeeFarmer(hbf);
        promptLinkedList = null;
    }
}
