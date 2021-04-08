import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prompt {
    private final String prompt;
    private String invalidResponsePrompt;
    private String insertPromptSeq = "";
    private String clientResponse = null;
    private boolean invalidResponse = false;
    private HashMap<Pattern, String> responsePaths;


    public Prompt(String prompt, String invalidResponsePrompt, HashMap<Pattern, String> responsePaths) {
        this.prompt = prompt;
        this.invalidResponsePrompt = invalidResponsePrompt;
        this.responsePaths = responsePaths;
    }

    /*
     * Get invalidResponse.
     * Return:
     * - String invalidResponse
     */
    public boolean getInvalidResponse() { return this.invalidResponse; }

    /*
     * Set invalidResponse.
     * Return:
     * - void
     */
    public void setInvalidResponse(boolean invalidResponse) { this.invalidResponse = invalidResponse; }

    /*
     * Get insertPromptSeq.
     * Return:
     *      - String insertPromptSeq:
     */
    public String getInsertPromptSeq() {
        return insertPromptSeq;
    }

    /*
     * Set insertPromptSeq
     * Return:
     *      - void
     */
    public void setInsertPromptSeq(String ip) {
        this.insertPromptSeq = ip;
    }

    /*
     * Get prompt.
     * Return:
     * - String prompt: the prompt to send to the user.
     */
    public String getPrompt() {
        return this.prompt;
    }

    /*
     * Get invalidResponsePrompt.
     * Return:
     * - String invalidResponsePrompt: the prompt to send to the client if their response is invalid.
     */
    public String getInvalidResponsePrompt() {
        return this.invalidResponsePrompt;
    }

    /*
     * Set the response of the client.
     * Return:
     * - void
     * Throw:
     * - FileNotFoundException: error will originally be thrown from within processClientResponse.
     * Call:
     * - processClientResponse(): with the client response now known, the prompt
     *                            performs certain actions based on whether the response
     *                            is valid or not.
     */
    public void setClientResponse(String clientResponse) throws IOException {
        this.clientResponse = clientResponse;
        processClientResponse();
    }

    /*
     * Get the response sent from the client.
     * Return:
     *      - String clientResponse: the response of the client.
     */
    public String getClientResponse() {
        return this.clientResponse;
    }

    /*
     * Wrapper for performing the validation of the client response, and adding new prompts based
     * on the client response.
     * Return:
     * - void
     * Throw:
     * - FileNotFoundException
     */
    private void processClientResponse() throws IOException {
        // validate the response sent from the client.
        this.validateResponse();
        // check to see if new prompts needs to be set.
        if(validateResponse()) {
            Matcher matcher;
            for(Pattern p : responsePaths.keySet()) {
                matcher = p.matcher(clientResponse);

                // client response matches this response option.
                if(matcher.find())
                    setInsertPromptSeq(responsePaths.get(p));
            }
        }
    }


    /*
     * Validate the response sent by the client. This is only called when the response
     * of the client is set, within setClientResponse.
     * Return:
     *      - Pattern p: The regex pattern the response of the client validates against.
     *                     Use this key to get the name of the prompt story that should
     *                     be loaded in.
     */
    private boolean validateResponse() {

        // An empty response is always invalid.
        if(clientResponse == null || clientResponse.length() < 1) {
            setInvalidResponse(true);
            return false;
        }

        // check to see if the client's response validates
        // against any single, regex amongst all valid response regexes.
        Matcher matcher;
        for(Pattern p : responsePaths.keySet()) {
            matcher = p.matcher(clientResponse);

            // client response matches against the current regex.
            if (matcher.find())
                return true;
        }
        setInvalidResponse(true);
        return false;
    }


    /*
     * Get the current prompt for the prompt. Invalid prompt if an invalid response has been handled, else valid prompt.
     * Return:
     *  - void
     */
    public String currPrompt() {
        if(!invalidResponse) {
            return getPrompt();
        } else {
            return getInvalidResponsePrompt();
        }

    }
}
