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
        // ask which apiary to edit.
        conversation.getHoneyBeeFarmer()
                .sendSMS("Select an apiary.\nInclude only the number associated with the apiary.\n\nExample: 1");
        conversation.getHoneyBeeFarmer()
                .sendSMS("Options:\n" + conversation.getHoneyBeeFarmer().getApiaryList());
    }

    public void endSequence() {
        setExit(true);
        conversation.addSequence(new MainMenu(conversation));
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }


    /*
     * Match the response id with the apiary within the hbf profile.
     * Return:
     *      - void
     */
    public void msg0() {
        // regex to match against digit
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);

        if(matcher.find()) {
            // ensure id selected is within the the range of apiary count.
            int apiaryCount = conversation.getHoneyBeeFarmer().getApiaries().size();
            int responseInt = Integer.parseInt(response);

            // decrement response by one since output is formatted from 1 up
            apiaryToEdit = conversation.getHoneyBeeFarmer().getApiaries().get(responseInt - 1);
            String options = "1. Change name\n2. Change number of hives\n3. Delete apiary";
            conversation.getHoneyBeeFarmer().sendSMS("What would you like to do to the apiary?\nOptions:\n" + options);
            currentMsg++;
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("\"" + response + "\" is not a valid apiary. Include only the number associated with the apiary.\n\nExample: 1");
            conversation.getHoneyBeeFarmer().sendSMS("Options:\n" + conversation.getHoneyBeeFarmer().getApiaryList());
        }
    }


    /*
     * Move to the message that carries out the action the hbf wants to do to the apiary.
     * Return:
     *      - void
     */
    public void msg1() {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);

        if(matcher.find()) {
            int optionSelected = Integer.parseInt(response);
            if(optionSelected > 3 || optionSelected < 0) {
                String options = "1. Change name\n2. Change number of hives\n3. Delete apiary";
                conversation.getHoneyBeeFarmer().sendSMS(optionSelected + " is not a valid option. Please choose from the following options and include only the option in your response." + "\nOptions:\n" + options);
                return;
            }
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
                    endSequence();
                    break;
                default:
                    endSequence();
            }
        }
    }
}
