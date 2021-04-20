import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditApiaryHiveCount extends Sequence {
    private Apiary apiaryToEdit;

    public EditApiaryHiveCount(Conversation conversation, Apiary apiaryToEdit) {
        super(conversation);
        this.apiaryToEdit = apiaryToEdit;
    }

    public void startSequence() {
        this.setLive(true);
        String prompt = "Apiary: " + apiaryToEdit.getName() + "\nCurrent number of hive(s): " + apiaryToEdit.getHiveCount() + "\n\nWhat is the new number of hive(s) associated with \"" + apiaryToEdit.getName() + "\"? Include only the new number of hive(s).\n\nExample:\n5";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println("Begin EditApiaryHiveCount Sequence\n");
        System.out.println(prompt);
    }

    public void endSequence() {
        this.setExit(true);
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.addSequence(new MainMenu(conversation));
        System.out.println("End EditApiaryHiveCount Sequence\n");
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
            default:
                endSequence();
        }
    }

    public void msg0() {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);
        System.out.println("Message 1/1");

        if(matcher.find()) {
            int hiveCount = Integer.parseInt(response);
            apiaryToEdit.setHiveCount(hiveCount);
            String prompt = "The amount of hive(s) associated with the apiary \""
                    + apiaryToEdit.getName() + "\" has been changed to " + hiveCount + ".\n\nReturning to the main menu...";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            endSequence();
        } else {
            String prompt = "Invalid number of hive(s). Include only the number of hives.\n\nExample:\n5";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
        }
    }
}
