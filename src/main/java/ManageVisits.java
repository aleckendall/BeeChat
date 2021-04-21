import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageVisits extends Sequence {

    public ManageVisits(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        setLive(true);
        String prompt = "What would you like to do? Choose from the following options:" +
                "\n1. Record a visit\n2. Show most recent visits";
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
        Pattern pattern = Pattern.compile("^1|2|3$");
        Matcher matcher = pattern.matcher(response);

        if(matcher.find()) {
            Integer optionSelected = Integer.parseInt(matcher.group());
            switch(optionSelected) {
                case 0:
                    conversation.addSequence(new RecordVisit(conversation));
                    endSequence();
                    break;
                case 1:
                    String visits = "";
                    for(final Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
                        for(Visit visit : apiary.getVisits()) {
                            // format the date
                            // add the apiary name and date to the prompt to send to user
                        }
                    }
            }
        }
    }
}
