import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Onboard extends Sequence {

    public Onboard(Conversation conversation) {
        super(conversation);
        startSequence();
    }

    public void startSequence() {
        conversation.getHoneyBeeFarmer().sendSMS("Welcome to HiveTracks! We'll need to collect some information so we can set up your profile.");
        conversation.getHoneyBeeFarmer().sendSMS("What is your first name? Include only your first name. Example: John");
    }

    /*
     * Perform end of sequence tasks.
     */
    public void endSequence() {
        exit = true;
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.getHoneyBeeFarmer().sendSMS("Your profile is now complete!");
    }

    /*
     * Call the current message in the message sequence.
     * Return:
     *      - void
     */
    public void doCurrentMsg() {
        switch (currentMsg) {
            case 0:
                msg0();
                break;
            case 1:
                msg1();
                break;
            case 2:
                msg2();
                break;
            default:
                endSequence();
        }
    }

    /*
     * Call the next msg.
     */
    public void handleResponse(String response) {
        this.setResponse(response);
        doCurrentMsg();
    }

    /*
     * ------ Name Segment 1/2 ------
     *
     *     Get the first name of
     *     the conversation.getHoneyBeeFarmer().
     *
     * ------------------------------
     * Return:
     *      - void
     */
    public void msg0() {
        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.matches()) {
            //update msg
            conversation.getHoneyBeeFarmer().setFirstName(response);
            conversation.getHoneyBeeFarmer().sendSMS("Your first name has been added to your account.");
            conversation.getHoneyBeeFarmer().sendSMS("What is your last name? Include only your last name. Example: Smith");
            currentMsg++;
        } else {
            // Invalid response (name)
            conversation.getHoneyBeeFarmer().setFirstName("The name did not match the format we are expecting. Please use only letters.");
        }
    }

    /*
     * ------ Name Segment 2/2 ------
     *
     *     Get the last name of
     *     the conversation.getHoneyBeeFarmer().
     *
     * ------------------------------
     * Return:
     *      - void
     */
    public void msg1() {
        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.matches()) {
            conversation.getHoneyBeeFarmer().setLastName(response);
            conversation.getHoneyBeeFarmer().sendSMS("Okay, your last name has been added to your profile.");
            conversation.getHoneyBeeFarmer().sendSMS("What is the number of apiaries you operate? Use a number and do not include any additional information. Example: 8");
            currentMsg++;
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("Sorry, but your response was not recognized as a valid response. Include only your last name using letters.");
        }
    }

    /*
     * ---- Apiary Segment 1/1 ----
     *
     *     Add the apiaries to
     *     the conversation.getHoneyBeeFarmer() account.
     *
     * ----------------------------
     */
    public void msg2() {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()) {
            Integer apiaryCount = Integer.parseInt(matcher.group());
            conversation.getHoneyBeeFarmer().setApiaryCount(apiaryCount);
            for(int i = 0; i < apiaryCount; i++) {
                conversation.addSequence(new AddApiary(conversation));
            }
            conversation.getHoneyBeeFarmer().sendSMS("The number of apiaries you operate has been added to your profile.");
            exit = true;
            currentMsg++;
            conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("Sorry, but your response was not recognized as a valid response. Include only the number of apiaries you operate using a number. Example: 4");
        }
    }
}
