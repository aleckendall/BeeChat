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
        System.out.println("Begin AddApiary Sequence\n");
        String prompt = "What is the name of your apiary? Only include the name of the apiary.\n\nExample: John's Apiary";
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        System.out.println(prompt);
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
            String prompt = "What is the latitude of your apiary?\n\nExample: 89.9\n\nIf you do not know, respond SKIP.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else {
            String prompt = "Sorry but the apiary name must consist of at least one character or number";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
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
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);
        Pattern patternSkip = Pattern.compile("^SKIP$");
        Matcher matcherSkip = patternSkip.matcher(response);

        if(matcher.find()) {
            Location location = apiary.getLocation();
            if(location == null) {
                location = new Location();
            }
            Double latitude = Double.parseDouble(matcher.group());
            if (latitude < -90 || latitude > 90) {
                String prompt = "The latitude is invalid. Only include the latitude.\n\nExample: 89.45\n\nIf you do not know, respond SKIP.";
                System.out.println(prompt);
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
                return;
            }
            location.setLatitude(latitude);
            apiary.setLocation(location);
            String prompt = "The latitude of the apiary has been recorded." + "\n\nWhat is the longitude of the apiary? Include only the longitude.\n\nExample:\n49.9\n\nIf you do not know, respond SKIP.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else if(matcherSkip.find()) {
            String prompt = "Latitude skipped." + "\n\nWhat is the longitude of the apiary? Only include the longitude.\n\nExample: 49.9\n\nIf you do not know, respond SKIP.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else {
            String prompt = "The latitude is invalid. Only include the latitude.\n\nExample: 89.45\n\nIf you do not know, respond SKIP.";
            System.out.println(prompt);
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
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
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(response);
        Pattern patternSkip = Pattern.compile("^SKIP$");
        Matcher matcherSkip = patternSkip.matcher(response);

        if(matcher.find()) {
            Location location = apiary.getLocation();
            if(location == null) {
                location = new Location();
            }
            Double longitude = Double.parseDouble(matcher.group());
            if (longitude < -180 || longitude > 180) {
                String prompt = "The longitude is invalid. Only include the longitude.\n\nExample: 89.45\n\nIf you do not know, respond SKIP.";
                System.out.println(prompt);
                conversation.getHoneyBeeFarmer().sendSMS(prompt);
                return;
            }
            location.setLatitude(longitude);
            String prompt = "The longitude for the apiary has been recorded." + "\n\nHow many hives does the apiary have? Only include the number of hive(s).\n\nExample: 5";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else if(matcherSkip.find()) {
            String prompt = "Longitude skipped." + "\n\nHow many hives does the apiary have? Only include the number of hive(s).\n\nExample: 5";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            currentMsg++;
        } else {
            String prompt = "The longitude is invalid. Only include the longitude.\n\nExample: -77.0364\n\nIf you do not know, respond SKIP.";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
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
            String prompt = "The number of hives at the apiary has been recorded!";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            System.out.println(prompt);
            endSequence();
        } else {
            String prompt = "The response was not able to be validated. Only include the number of hive(s).\n\nExample: 5";
            System.out.println(prompt);
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
        }
    }
}
