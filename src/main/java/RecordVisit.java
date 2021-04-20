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
        System.out.println("Begin RecordVisit Sequence\n");
        String apiaries = "\n\nOption(s):\n";
        int apiaryIndex = 1;
        for(Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
            apiaries += apiaryIndex++ + ". " + apiary.getName() + "\n";
        }
        String prompt = "Which apiaries did you visit? The date of the visit will be the same for all of the apiaries you select. Select at least one; separate multiple apiaries using a comma.\n\nExamples:\n2\n1,2,3\n\nRespond MENU to be taken back to the main menu." + apiaries;
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println(prompt);
        this.setLive(true);
    }

    public void endSequence() {
        this.setExit(true);
        // Save the honey bee farmer to persistent storage.
        this.conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.addSequence(new MainMenu(conversation));
        System.out.println("End RecordVisit Sequence\n");
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
        System.out.println("Message 1/2");

        Pattern pattern = Pattern.compile("\\d");
        Pattern menuPatter = Pattern.compile("MENU");
        Matcher matcher = menuPatter.matcher(response);
        if(matcher.find()) {
            String prompt = "Returning to the main menu...";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            conversation.addSequence(new MainMenu(conversation));
            endSequence();
            return;
        }

        matcher = pattern.matcher(response);
        if(matcher.find()) {
            while(matcher.find()) {
                Integer index = Integer.parseInt(matcher.group())-1;
                if(index >= conversation.getHoneyBeeFarmer().getApiaries().size()) {
                    String apiaries = "";
                    int apiaryIndex = 1;
                    for(Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
                        apiaries += apiaryIndex++ + ". " + apiary.getName() + "\n";
                    }
                    String prompt = "Apiary \"" + index + "\" does not exist.\nPlease choose from the following options:\n" + apiaries + "\nRespond MENU to be taken back to the main menu.";
                    conversation.getHoneyBeeFarmer().sendSMS(prompt);
                    return;
                }
                visited.add(
                        conversation.getHoneyBeeFarmer()
                            .getApiaries()
                            .get(index)
                );
            }
            currentMsg++;
            String prompt = "What is the date of the visit?\n\nFormat: mm/dd/yyyy\n\nExample:\n12/21/2021";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
        }
    }

    public void msg1() throws ParseException {
        Pattern pattern = Pattern.compile("^(0[1-9]|1[012])\\/(0[1-9]|[12][0-9]|3[01])\\/(19|20)\\d\\d$");
        Matcher matcher = pattern.matcher(response);
        System.out.println("Message 2/2");

        if(matcher.matches()) {
            Date visitDate = new SimpleDateFormat("MM/dd/yyyy").parse(response);
            for(Apiary apiary : visited) {
                if(apiary.getVisits() == null) {
                    ArrayList<Visit> visits = new ArrayList<>();
                    visits.add(new Visit(visitDate));
                    apiary.setVisits(visits);
                } else {
                    apiary.getVisits().add(new Visit(visitDate));
                }
            }
            String prompt = "Your visit(s) have been recorded.\n\nReturning to the main menu...";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            this.endSequence();
        } else {
            String prompt = "The date is invalid. Please use the format mm/dd/yyyy. Do not include anything else in the message.\n\nExample:\n06/22/2021";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
        }
    }

}
