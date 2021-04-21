import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteAccount extends Sequence {

    public DeleteAccount(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        setLive(true);
        System.out.println("Begin DeleteAccount Sequence\n");
        String prompt = "Are you sure you want to delete your account? This action can not be undone. Respond Y for yes or N for no.\n\nExample:\nN";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println(prompt);
    }

    public void endSequence() {
        setExit(true);
        System.out.println("End DeleteAccount Sequence\n");
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }

    public void msg0() {
        Pattern pattern = Pattern.compile("^N|Y$");
        Matcher matcher = pattern.matcher(response);
        System.out.println("Message 1/1");
        if(matcher.find()) {
            if(matcher.group().compareTo("N") == 0) {
                String prompt = "Account will not be removed.\n\nReturning to the main menu...";
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
                System.out.println(prompt);
                conversation.addSequence(new MainMenu(conversation));
            } else {
                String prompt = "Your account has been deleted.";
                System.out.println(prompt);
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
                conversation.getDatabase().removeHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
                conversation.clearSequences();
                return;
            }
            endSequence();
        } else {
            String prompt = "Your response was not recognized as a valid response. Respond N to abort account deletion, or Y to continue with the deletion of your account";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
        }
    }
}
