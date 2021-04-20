import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageApiaries extends Sequence {

    public ManageApiaries(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        setLive(true);
        System.out.println("Begin ManageApiaries Sequence");
        String apiaryList = conversation.getHoneyBeeFarmer().getApiaryList();
        String prompt = apiaryList + "\nManage Apiaries\nSelect an option to do:\n1. Add apiary\n2. Edit existing apiary\n\nExample: 1";
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
        Pattern pattern = Pattern.compile("^1|2$");
        Matcher matcher = pattern.matcher(response);

        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());

            if(optionSelected == 1) {
                // add an apiary
                conversation.addSequence(new MainMenu(conversation));
                conversation.addSequence(new AddApiary(conversation));
            } else {
                // edit an existing apiary
                conversation.addSequence(new EditApiary(conversation));
            }
            endSequence();
            return;
        } else {
            String prompt = "Invalid option selected. Please choose from the following options. Include only the option number, and nothing else.\n\nExample: 1";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
        }
    }
}
