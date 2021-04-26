import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageVisits extends Sequence {

    public ManageVisits(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        setLive(true);
        String prompt = "What would you like to do? Choose from the following options:" +
                "\n1. Record a visit\n2. Show most recent visits\n\nRespond MAIN to be taken back to the main menu.";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println(prompt);
    }

    public void endSequence() {
        setExit(true);
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
        }
    }

    public void msg0() {
        Pattern patternMain = Pattern.compile("MAIN");
        Matcher matcher = patternMain.matcher(response);
        if(matcher.find()) {
            conversation.getHoneyBeeFarmer().sendSMS("Taking you back to the main menu...");
            conversation.addSequence(new MainMenu(conversation));
            endSequence();
            return;
        }

        Pattern pattern = Pattern.compile("^1|2|3$");
        matcher = pattern.matcher(response);
        HashMap<Date, String> dates = new HashMap<>();
        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());
            switch(optionSelected) {
                case 1:
                    conversation.addSequence(new RecordVisit(conversation));
                    endSequence();
                    break;
                case 2:
                    if(conversation.getHoneyBeeFarmer().getApiaries() == null) {
                        conversation.getHoneyBeeFarmer().sendSMS("Sorry, but it looks like you haven't add an apiary to your account yet. Visits are added to your apiaries so there isn't any visit(s) to show! Taking you back to the main menu...");
                        endSequence();
                        return;
                    }
                    for(final Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
                        if(apiary.getVisits() == null)
                            continue;
                        for(Visit visit : apiary.getVisits()) {
                            // format the date
                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                            // add the apiary name and date to the prompt to send to user
                            String visitOutput = apiary.getName() + ": " + df.format(visit.getVisitDate()) + "\n";
                            dates.put(visit.getVisitDate(), visitOutput);
                        }
                    }
                    List<Date> date_keys = new ArrayList<>(dates.keySet());
                    String visits = "";
                    if(date_keys.size() == 0) {
                        conversation.getHoneyBeeFarmer().sendSMS("Sorry, but it looks like you haven't recorded a visit yet. Record a visit, then try viewing your visits again! Taking you back to manage visits...");
                        conversation.addSequence(new ManageVisits(conversation));
                        endSequence();
                    } else if(date_keys.size() < 5) {
                        for(int i = 0; i < date_keys.size(); i++) {
                            Date max = Collections.max(date_keys);
                            visits += dates.get(max);
                        }
                    } else {
                        for(int i = 0; i < 5; i++) {
                            Date max = Collections.max(date_keys);
                            visits += dates.get(max);
                        }
                    }
                    conversation.getHoneyBeeFarmer().sendSMS(visits);
            }
        }
    }
}
