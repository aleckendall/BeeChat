import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageApiaries extends Sequence {

    public ManageApiaries(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        setLive(true);
        System.out.println("Begin ManageApiaries Sequence\n");
        String apiaryList = conversation.getHoneyBeeFarmer().getApiaryList();
        String prompt = "Manage Apiaries\n\nApiaries:\n" + apiaryList + "\nChoose an option:\n1. Add apiary\n2. Edit existing apiary\n3. Return to Main Menu\n\nExample:\n1";
        System.out.println(prompt);
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
    }

    public void endSequence() {
        setExit(true);
        System.out.println("End ManageApiaries Sequence\n");
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }


    public void msg0() {
        Pattern pattern = Pattern.compile("^1|2|3$");
        Matcher matcher = pattern.matcher(response);
        System.out.println("Message 1/1");

        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());

            if(optionSelected == 1) {
                // add an apiary
                conversation.addSequence(new MainMenu(conversation));
                conversation.addSequence(new AddApiary(conversation));
            } else if(optionSelected == 2) {
                // edit an existing apiary
                conversation.addSequence(new EditApiary(conversation));
            } else {
                conversation.addSequence(new MainMenu(conversation));
            }
            endSequence();
            return;
        } else {
            String prompt = "Invalid option selected. Please choose from the following options. Include only the option number, and nothing else.\n" +
                    "\nChoose an option:\n1. Add apiary\n2. Edit existing apiary\n\nExample:\n1";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
        }
    }
}
