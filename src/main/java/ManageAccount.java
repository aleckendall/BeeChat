import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageAccount extends Sequence {

    public ManageAccount(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        String options = "Select an option:\n1. Update phone number\n3. Update name\n4. Delete account";
        conversation.getHoneyBeeFarmer().sendSMS(options);
        setLive(true);
    }

    public void endSequence() {
        setExit(true);
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }

    public void msg0() {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);
        String options = "Select an option:\n1. Update phone number\n2. Update name\n3. Delete account";

        if (matcher.find()) {
            int option = Integer.parseInt(matcher.group(1));
            switch(option) {
                case 1:
                    conversation.addSequence(new EditPhoneNumber(conversation));
                    endSequence();
                    break;
                case 2:
                    conversation.addSequence(new EditName(conversation));
                    endSequence();
                    break;
                case 3:
                    conversation.addSequence(new DeleteAccount(conversation));
                    endSequence();
                default:
                    conversation.getHoneyBeeFarmer().sendSMS(option + " is not a valid option. Include only the option.\nExample: 1");
                    conversation.getHoneyBeeFarmer().sendSMS(options);
                    return;
            }
        }
    }
}
