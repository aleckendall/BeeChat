import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditApiary extends Sequence {
    private Apiary apiaryToEdit;

    public EditApiary(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        // no apiaries that be edited.
        if(conversation.getHoneyBeeFarmer().getApiaries() == null) {
            conversation.getHoneyBeeFarmer()
                    .sendSMS("There are not any apiaries associated with your account. Therefore, an apiary cannot be edited.\n\n" +
                            "To add an apiary to your account, choose \"Manage apiaries\" from within the main menu.");
            endSequence();
            return;
        }
        setLive(true);
        // ask which apiary to edit.
        String prompt = "Choose an apiary:\n" + conversation.getHoneyBeeFarmer().getApiaryList() + "\nExample: 1";
        conversation.getHoneyBeeFarmer()
                .sendSMS(prompt);
        System.out.println("Begin EditApiary Sequence\n");
        System.out.println(prompt + "\n");
    }

    public void endSequence() {
        setExit(true);
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
            case 1:
                msg1();
                break;
        }
    }


    /*
     * Match the response id with the apiary within the hbf profile.
     * Return:
     *      - void
     */
    public void msg0() {
        System.out.println("Message 1/2");
        // regex to match against digit
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);

        if(matcher.find()) {
            // ensure id selected is within the the range of apiary count.
            int apiaryCount = conversation.getHoneyBeeFarmer().getApiaries().size();
            int responseInt = Integer.parseInt(response);

            // decrement response by one since output is formatted from 1 up
            apiaryToEdit = conversation.getHoneyBeeFarmer().getApiaries().get(responseInt - 1);
            String prompt = "What would you like to do to the apiary?\n\nOptions:\n" + "1. Change name\n2. Change number of hives\n3. Delete apiary\n4. Return to Main Menu\n\nExample:\n3";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else {
            String prompt = "\"" + response + "\" is not a valid apiary. Include only the number associated with the apiary." + "\n\nExample:\n1" + "\n\nOptions:\n" + conversation.getHoneyBeeFarmer().getApiaryList();
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
        }
    }

    /*
     * Move to the message that carries out the action the hbf wants to do to the apiary.
     * Return:
     *      - void
     */
    public void msg1() {
        Pattern pattern = Pattern.compile("^1|2|3$");
        Matcher matcher = pattern.matcher(response);
        System.out.println("Message 2/2");

        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());

            switch(optionSelected) {
                case 1:
                    conversation.addSequence(new EditApiaryName(conversation, apiaryToEdit));
                    endSequence();
                    break;
                case 2:
                    conversation.addSequence(new EditApiaryHiveCount(conversation, apiaryToEdit));
                    endSequence();
                    break;
                case 3:
                    conversation.getHoneyBeeFarmer().removeApiary(apiaryToEdit);
                    String prompt = "Apiary has successfully been removed.\n\nReturning to the main menu...";
                    conversation.getHoneyBeeFarmer().sendSMS(prompt);
                    System.out.println(prompt);
                    conversation.addSequence(new MainMenu(conversation));
                    endSequence();
                    break;
                case 4:
                    conversation.addSequence(new MainMenu(conversation));
                    endSequence();
                    break;
                default:
                    endSequence();
            }
        }
    }
}
