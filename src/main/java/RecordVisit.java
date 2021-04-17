import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecordVisit extends Sequence {
    private ArrayList<Apiary> visited = new ArrayList<>();

    public RecordVisit(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        if(conversation.getHoneyBeeFarmer().getApiaries() == null) {
            conversation.getHoneyBeeFarmer().sendSMS("There are not any apiaries associated with your account. Therefore, a visit cannot be recorded.\n\nTo add an apiary to your account, choose \"Manage apiaries\" from within the main menu.");
            endSequence();
            return;
        }
        String apiaries = "Option(s):\n";
        int apiaryIndex = 1;
        for(Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
            apiaries += apiaryIndex++ + ". " + apiary.getName() + "\n";
        }
        conversation.getHoneyBeeFarmer().sendSMS("Which apiaries did you visit? The date of the visit will be the same for all of the apiaries you select. Select at least one; separate multiple apiaries using a comma.\nExamples:\n2\n1,2,3\n\nRespond MENU to be taken back to the main menu.");
        conversation.getHoneyBeeFarmer().sendSMS(apiaries);
        this.setLive(true);
    }

    public void endSequence() {
        this.setLive(false);
        // Save the honey bee farmer to persistent storage.
        this.conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.addSequence(new MainMenu(conversation));
    }

    public void doCurrentMsg() throws ParseException {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
            case 1:
                msg1();
                break;
            default:
                endSequence();
        }
    }

    public void msg0() {
        Pattern pattern = Pattern.compile("\\d");
        Pattern menuPatter = Pattern.compile("MENU");
        Matcher matcher = menuPatter.matcher(response);
        if(matcher.find()) {
            conversation.addSequence(new MainMenu(conversation));
            endSequence();
            return;
        }

        matcher = pattern.matcher(response);
        if(matcher.find()) {
            while(matcher.find()) {
                Integer index = Integer.parseInt(matcher.group(1))-1;
                if(index > conversation.getHoneyBeeFarmer().getApiaries().size()) {
                    String apiaries = "";
                    int apiaryIndex = 1;
                    for(Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
                        apiaries += apiaryIndex++ + ". " + apiary.getName() + "\n";
                    }
                    conversation.getHoneyBeeFarmer().sendSMS("Apiary with index " + index + " does not exist.\nPlease choose from the following options:\n" + apiaries + "\nRespond MENU to be taken back to the main menu.");
                    return;
                }
                visited.add(
                        conversation.getHoneyBeeFarmer()
                            .getApiaries()
                            .get(index)
                );
            }
            currentMsg++;
            conversation.getHoneyBeeFarmer().sendSMS("What is the date of the visit?\n Format: mm/dd/yyyy\nExample:\n12/21/2021");
        }
    }

    public void msg1() throws ParseException {
        Pattern pattern = Pattern.compile("/(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d\\d/");
        Matcher matcher = pattern.matcher(response);

        if(matcher.matches()) {
            Date visitDate = new SimpleDateFormat("MM/dd/yyyy").parse(response);
            System.out.println(visitDate);
            for(Apiary apiary : visited) {
                apiary.getVisits().add(new Visit(visitDate));
            }
            conversation.getHoneyBeeFarmer().sendSMS("Your visit(s) have been recorded. Taking you back to the main menu.");
            this.endSequence();
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("The date is invalid. Please use the format mm/dd/yyyy. Do not include anything else in the message.\nExample:\n06/22/2021");
        }
    }

}
