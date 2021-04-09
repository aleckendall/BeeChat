import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddApiary extends Sequence {
    private HoneyBeeFarmer hbf;
    private Apiary apiary;

    public AddApiary(Conversation conversation) {
        super(conversation);
        startSequence();
    }

    /*
     * Send the initial message for the sequence.
     *
     * @return void
     */
    public void startSequence() {
        hbf.sendSMS("What is the name of your apiary? Only include the name of the apiary.");
    }

    /*
     * Perform end of sequence operations.
     *
     * @return void
     */
    public void endSequence() {
        this.hbf.addApiary(apiary);
        conversation.getDatabase().updateHoneyBeeFarmer(hbf);
        conversation.getHoneyBeeFarmer().setProperties(hbf);
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
            default:
                // no more message so exit
                exit = true;
        }
    }

    /*
     * Handle the client response.
     *
     * @return void
     */
    public void handleResponse(String response) {
        this.setResponse(response);
        doCurrentMsg();
        return;
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

        Pattern pattern = Pattern.compile("[.]+[\\s.]*");
        Matcher matcher = pattern.matcher(response);
        if(matcher.matches()) {
            this.apiary = new Apiary(response, new Date(System.currentTimeMillis()));
            hbf.sendSMS("Your apiary has been added to your account");
            hbf.sendSMS("What is the latitude of your apiary? If you do not know, respond SKIP");
            currentMsg++;
        } else {
            hbf.sendSMS("Sorry but the apiary name must consist of at least one character or number");
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
            hbf.sendSMS("The latitude of the apiary has been recorded.");
            currentMsg++;
        } else if(matcherSkip.matches()) {
            hbf.sendSMS("Latitude skipped.");
            hbf.sendSMS("What is the longitude of the apiary? Only include the longitude. If you do not know, respond SKIP");
            currentMsg++;
        } else {
            hbf.sendSMS("The latitude is invalid. Only include the latitude. Example: 89.9");
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
            hbf.sendSMS("The longitude for the apiary has been recorded.");
            hbf.sendSMS("How many hives does the apiary have? Only include the number of hive(s). Example: 5");
            currentMsg++;
        } else if(matcherSkip.matches()) {
            hbf.sendSMS("Longitude skipped.");
            hbf.sendSMS("How many hives does the apiary have? Only include the number of hive(s). Example: 5");
            currentMsg++;
        } else {
            hbf.sendSMS("The longitude is invalid. Only include the longitude. Example: -77.0364");
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
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()) {
            Integer hiveCount = Integer.parseInt(matcher.group());
            apiary.setHiveCount(hiveCount);
            hbf.sendSMS("The number of hives at the apiary has been recorded!");
            endSequence();
        } else {
            hbf.sendSMS("The response was not able to be validated. Only include the number of hive(s). Example: 5");
        }
    }
}
