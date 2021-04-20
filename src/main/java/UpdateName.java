import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateName extends Sequence {
    private boolean lastName = false;

    public UpdateName(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        setLive(true);
        System.out.println("Begin UpdateName Sequence\n");
        String prompt = "Which name would you like to change?\n\nChoose an option:\n1. First name\n2. Last name" + "\n\nRespond MAIN to return to the main menu.";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println(prompt);
    }

    public void endSequence() {
        setExit(true);
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.addSequence(new MainMenu(conversation));
        System.out.println("End UpdateName Sequence\n");
    }

    public void doCurrentMsg() {

        if(response.compareTo("MAIN") == 0) {
            endSequence();
            return;
        }

        switch(currentMsg) {
            case 0:
                msg0();
                break;
            case 1:
                msg1();
                break;
        }
    }

    public void msg0() {
        Pattern pattern = Pattern.compile("^1|2$");
        Matcher matcher = pattern.matcher(response);
        System.out.println("Message 1/2");

        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());
            String prompt;
            if (optionSelected == 1) {
                prompt = "What is the new first name for the account? Include only the first name.\n\nExample:\nJohn";
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
                System.out.println(prompt);
            } else {
                lastName = true;
                prompt = "What is the new last name for the account? Include only the last name.\n\nExample:\nSmith";
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
                System.out.println(prompt);
            }
            currentMsg++;
        } else {
            String prompt = "The option you selected was not recognized as a valid option. Please choose from the following options, and include only the number for the option in your response." + "\n\nSelect an option:\n1. First name\n2. Last name";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
        }
    }

    public void msg1() {
        System.out.println("Message 2/2");

        String prompt;
        if(response.length() == 0) {
            if(lastName) {
                prompt = "Name must not be blank. Include only the first name in your response.\n\nExample:\nSmith";
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
            } else {
                prompt = "Name must not be blank. Include only the last name in your response.\n\nExample:\nJohn";
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
            }
            return;
        }

        if(lastName) {
            prompt = "Last name changed to \"" + response + "\"";
            System.out.println(prompt);
            conversation.getHoneyBeeFarmer().setLastName(prompt);
        } else {
            prompt = "First name changed to \"" + response + "\"";
            System.out.println(prompt);
            conversation.getHoneyBeeFarmer().setFirstName(response);
        }
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        endSequence();
    }
}
