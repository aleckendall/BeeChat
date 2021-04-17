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
        String message = "Apiary: " + apiaryToEdit.getName() + "\nCurrent number of hive(s): " + apiaryToEdit.getHiveCount() + "\n\nWhat is the new number of hive(s) associated with \"" + apiaryToEdit.getName() + "\"? Include only the new number of hive(s).\nExample: 5";
        conversation.getHoneyBeeFarmer().sendSMS(message);
    }

    public void endSequence() {
        this.setExit(true);
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
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

        if(matcher.find()) {
            int hiveCount = Integer.parseInt(response);
            apiaryToEdit.setHiveCount(hiveCount);

            conversation.getHoneyBeeFarmer().sendSMS("The amount of hive(s) associated with the apiary \""
                    + apiaryToEdit.getName() + "\" has been changed to " + hiveCount + ".\n\nReturning to the main menu.");
            endSequence();
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("Invalid number of hive(s). Include only the number of hives.\nExample: 5");
        }
    }
}
