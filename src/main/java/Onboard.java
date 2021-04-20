import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Onboard extends Sequence {

    public Onboard(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        System.out.println("Begin Onboard Sequence\n");
        String prompt = "Welcome to HiveTracks! We'll need to collect some information so we can set up your profile." + "\n\nWhat is your first name? Include only your first name.\n\nExample: John";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println(prompt);
        this.setLive(true);
    }

    /*
     * Perform end of sequence tasks.
     */
    public void endSequence() {
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        String prompt = "Your profile is now complete! Taking you back to the main menu...";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        conversation.addSequence(new MainMenu(conversation));
        paused = false;
        setExit(true);
        System.out.println(prompt);
        System.out.println("End Onboard Sequence");
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
        System.out.println("Message 1/3");

        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.matches()) {
            //update msg
            conversation.getHoneyBeeFarmer().setFirstName(response);
            String prompt = "Your first name has been added to your account." + "\n\n" + "What is your last name? Include only your last name.\n\nExample: Smith";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else {
            // Invalid response (name)
            String prompt = "The name did not match the format we are expecting.\nPlease use only letters.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
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
        System.out.println("Message 2/3");

        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.matches()) {
            conversation.getHoneyBeeFarmer().setLastName(response);
            String prompt = "Okay, your last name has been added to your profile." + "\n\nWhat is the number of apiaries you operate? Use a number and do not include any additional information.\n\nExample: 8";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else {
            String prompt = "Sorry, but your response was not recognized as a valid response.\nInclude only your last name using letters.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
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
        System.out.println("Message 3/3");

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()) {
            Integer apiaryCount = Integer.parseInt(matcher.group());
            conversation.getHoneyBeeFarmer().setApiaryCount(apiaryCount);
            if(apiaryCount == 0) {
                exit = true;
                currentMsg++;
                conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
                endSequence();
                return;
            }
            String prompt = "Okay, we'll now add " + apiaryCount + " apiaries to your account.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            for(int i = 0; i < apiaryCount; i++) {
                conversation.addSequence(new AddApiary(conversation));
            }
            currentMsg++;
            paused = true;
            conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        } else {
            String prompt = "Sorry, but your response was not recognized as a valid response.\nInclude only the number of apiaries you operate using a number.\n\nExample: 4";
            System.out.println(prompt);
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
        }
    }
}
