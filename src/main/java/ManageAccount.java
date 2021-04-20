import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageAccount extends Sequence {

    public ManageAccount(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        System.out.println("Begin ManageAccount Sequence\n");
        String options = "Select an option:\n1. Update name\n2. Delete account\n\nExample:\n1\n\nRespond MAIN to return to the main menu.";
        System.out.println(options);
        conversation.getHoneyBeeFarmer().sendSMS(options);
        setLive(true);
    }

    public void endSequence() {
        setExit(true);
        System.out.println("End ManageAccount Sequence\n");
    }

    public void doCurrentMsg() {

        if(response.compareTo("MAIN") == 0) {
            conversation.addSequence(new MainMenu(conversation));
            endSequence();
        }

        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }

    public void msg0() {
        Pattern pattern = Pattern.compile("^1|2$");
        Matcher matcher = pattern.matcher(response);
        String options = "Select an option:\n1. Update name\n2. Delete account";
        System.out.println("Message 1/1");

        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());
            switch(optionSelected) {
                case 1:
                    conversation.addSequence(new UpdateName(conversation));
                    endSequence();
                    break;
                case 2:
                    conversation.addSequence(new DeleteAccount(conversation));
                    endSequence();
                    break;
            }
        } else {
            String prompt = "Invalid option selected. Please choose from the following options. Include only the option number in your response.";
            prompt += "\n\n" + options + "\n\nExample:\n1";
            System.out.println(prompt);
            conversation.getHoneyBeeFarmer().sendSMS(prompt + "\n\n" + options + "\n\nExample:\n1");
        }
    }
}
