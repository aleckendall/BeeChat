import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddApiary extends Sequence {
    private Apiary apiary = new Apiary();

    public AddApiary(Conversation conversation) {
        super(conversation);
    }

    /*
     * Send the initial message for the sequence.
     *
     * @return void
     */
    public void startSequence() {
        conversation.getHoneyBeeFarmer().sendSMS("What is the name of your apiary? Only include the name of the apiary.\n\nExample: John's Apiary");
        this.setLive(true);
    }

    /*
     * Perform end of sequence operations.
     *
     * @return void
     */
    public void endSequence() {
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.getHoneyBeeFarmer().setProperties(conversation.getHoneyBeeFarmer());
        exit = true;
    }

    /*
     * Call the current message within the message sequence.
     * Return:
     *      - void
     */
    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                message0();
                break;
            case 1:
                message1();
                break;
            case 2:
                message2();
                break;
            case 3:
                message3();
                break;
        }
    }


    /*
     * ---- Create Apiary Segment 1/1 ----
     *
     *     Get the name of the apiary.
     *
     * -----------------------------------
     * Return:
     *      - void
     */
    public void message0() {

        if(response.length() != 0) {
            if(conversation.getHoneyBeeFarmer().getApiaries() != null) {
                for (Apiary apiary : conversation.getHoneyBeeFarmer().getApiaries()) {
                    if (apiary.getName().compareTo(response) == 0) {
                        conversation.getHoneyBeeFarmer().sendSMS("An apiary with the name \"" + response + "\" already exists. Please choose a different name.");
                        return;
                    }
                }
            }
            this.apiary.setName(response);
            conversation.getHoneyBeeFarmer().sendSMS("What is the latitude of your apiary?\n\nExample: 89.9\n\nIf you do not know, respond SKIP.");
            currentMsg++;
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("Sorry but the apiary name must consist of at least one character or number");
        }
    }

    /*
     * ---- Location of Apiary Segment 1/2 ----
     *
     *     Get the latitude of the apiary.
     *
     * ----------------------------------------
     * Return:
     *      - void
     */
    public void message1() {
        Pattern pattern = Pattern.compile("[-]*[\\d]+.[\\d]+");
        Matcher matcher = pattern.matcher(response);
        Pattern patternSkip = Pattern.compile("^SKIP$");
        Matcher matcherSkip = patternSkip.matcher(response);

        if(matcher.matches()) {
            Location location = apiary.getLocation();
            location.setLatitude(Double.parseDouble(response));
            apiary.setLocation(location);
            conversation.getHoneyBeeFarmer().sendSMS("The latitude of the apiary has been recorded.");
            currentMsg++;
        } else if(matcherSkip.matches()) {
            conversation.getHoneyBeeFarmer().sendSMS("Latitude skipped.");
            conversation.getHoneyBeeFarmer().sendSMS("What is the longitude of the apiary? Only include the longitude.\n\nExample: 49.9\n\nIf you do not know, respond SKIP.");
            currentMsg++;
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("The latitude is invalid. Only include the latitude.\n\nExample: 89.45\n\nIf you do not know, respond SKIP.");
        }
    }

    /*
     * ---- Location of Apiary Segment 2/2 ----
     *
     *     Get the longitude of the apiary.
     *
     * ----------------------------------------
     * Return:
     *      - void
     */
    public void message2() {
        Pattern pattern = Pattern.compile("[-]*[\\d]+.[\\d]+");
        Matcher matcher = pattern.matcher(response);
        Pattern patternSkip = Pattern.compile("^SKIP$");
        Matcher matcherSkip = patternSkip.matcher(response);

        if(matcher.matches()) {
            Location location = apiary.getLocation();
            location.setLongitude(Double.parseDouble(response));
            apiary.setLocation(location);
            conversation.getHoneyBeeFarmer().sendSMS("The longitude for the apiary has been recorded.");
            conversation.getHoneyBeeFarmer().sendSMS("How many hives does the apiary have? Only include the number of hive(s).\n\nExample: 5");
            currentMsg++;
        } else if(matcherSkip.matches()) {
            conversation.getHoneyBeeFarmer().sendSMS("Longitude skipped.");
            conversation.getHoneyBeeFarmer().sendSMS("How many hives does the apiary have? Only include the number of hive(s).\n\nExample: 5");
            currentMsg++;
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("The longitude is invalid. Only include the longitude.\n\nExample: -77.0364\n\nIf you do not know, respond SKIP.");
        }
    }

    /*
     * ---- Hives at the Apiary 1/1 ----
     *
     *   Get the amount of hives at the
     *   apiary.
     *
     * ---------------------------------
     * Return:
     *      - void
     */
    public void message3() {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()) {
            Integer hiveCount = Integer.parseInt(matcher.group());
            apiary.setHiveCount(hiveCount);
            conversation.getHoneyBeeFarmer().addApiary(apiary);
            conversation.getHoneyBeeFarmer().sendSMS("The number of hives at the apiary has been recorded!");
            endSequence();
        } else {
            conversation.getHoneyBeeFarmer().sendSMS("The response was not able to be validated. Only include the number of hive(s).\n\nExample: 5");
        }
    }
}
