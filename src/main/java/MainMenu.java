import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainMenu extends Sequence {

    public MainMenu(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        System.out.println("Begin MainMenu Sequence\n");
        String prompt = "Main Menu\nPlease select an option:\n1. Manage visits\n2. Manage apiaries\n3. Manage account\n\nExample:\n2\n\nRespond DONE to end the conversation.";
        System.out.println(prompt);
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        this.setLive(true);
    }

    public void endSequence() {
        this.setExit(true);
        System.out.println("End MainMenu Sequence\n");
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }

    public void msg0() {
        Pattern pattern1 = Pattern.compile("^1$");
        Pattern pattern2 = Pattern.compile("^2$");
        Pattern pattern3 = Pattern.compile("^3$");
        Pattern patternDone = Pattern.compile("DONE");
        System.out.println("Message 1/1");

        Matcher matcher = patternDone.matcher(response);
        if(matcher.find()) {
            conversation.getHoneyBeeFarmer().sendSMS("Ending the conversation. Simply text this number again if you want to record a visit or edit your account!");
            endSequence();
            return;
        }

        matcher = pattern1.matcher(response);
        if(matcher.find()) {
            // record a visit
            conversation.addSequence(new ManageVisits(conversation));
            endSequence();
            return;
        }

        matcher = pattern2.matcher(response);
        if(matcher.find()) {
            conversation.addSequence(new ManageApiaries(conversation));
            endSequence();
            return;
        }

        matcher = pattern3.matcher(response);
        if(matcher.find()) {
            conversation.addSequence(new ManageAccount(conversation));
            endSequence();
            return;
        }
        String prompt = "Sorry, but your response is not valid.\nPlease choose from the following options:\n1. Manage visits\n2. Manage apiaries\n3. Manage account\n\nExample:\n2";
        System.out.println(prompt);
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
    }
}
