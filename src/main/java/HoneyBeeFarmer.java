import java.util.ArrayList;
import java.util.Date;

public class HoneyBeeFarmer {
    private String firstName;
    private String lastName;
    private PhoneNumber telephoneNumber;
    private String id;
    private ArrayList<Apiary> apiaries;
    private Integer apiaryCount;
    private Location location;
    protected SmsSendBehavior smsSendBehavior;
    private SimulateHoneyBeeFarmerDB db = new SimulateHoneyBeeFarmerDB();

    public HoneyBeeFarmer(String telephoneNumber) {
        this.telephoneNumber = new PhoneNumber(telephoneNumber);
        if(this.db.exists(this.telephoneNumber)) {
            HoneyBeeFarmer profile = db.getHoneyBeeFarmer(this.telephoneNumber);
            this.setProperties(profile);
        } else {

        }
    }

    public HoneyBeeFarmer(String firstName, String lastName, String telephoneNumber, String id, ArrayList<Apiary> apiaries, Location location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephoneNumber = new PhoneNumber(telephoneNumber);
        this.id = id;
        this.apiaries = apiaries;
        this.location = location;
    }

    /*
     * Save the honey bee farmer to the database.
     * Return:
     * - void
     */
    public void saveHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        db.updateHoneyBeeFarmer(hbf);
    }

    /*
     * Set the attributes of the honey bee farmer to the attributes of a provided honey bee farmer.
     * Return:
     *      - void
     */
    public void setProperties(HoneyBeeFarmer hbf) {
        setFirstName(hbf.getFirstName());
        setLastName(hbf.getLastName());
        setId(hbf.getId());
        setApiaries(hbf.getApiaries());
        setLocation(hbf.getLocation());
    }

    /*
     * Set the count of the apiaries operated by the honey bee farmer.
     * Return:
     *      - void
     */
    public void setApiaryCount(Integer count) {
        if(count > apiaries.size()) {
            int diff = apiaries.size() - count;
            // add new apiaries
            for(int i = 0; i < diff; i++) {
                apiaries.add(new Apiary("temp" + i, new Date(System.currentTimeMillis())));
            }
        }
    }


    /*
     * Set the first name of the honey bee farmer.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /*
     * Get the first name of the honey bee farmer.
     */
    public String getFirstName() {
        return this.firstName;
    }

    /*
     * Set the last name of the honey bee farmer.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /*
     * Get the last name of the honey bee farmer.
     */
    public String getLastName() {
        return this.lastName;
    }

    /*
     * Set the telephone number of the honey bee farmer.
     */
    public void setTelephoneNumber(PhoneNumber telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }


    /*
     * Get the telephone number for the honey bee farmer.
     */
    public PhoneNumber getTelephoneNumber() {
        return this.telephoneNumber;
    }


    /*
     * Set the id of the honey bee farmer.
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * Get the id of the honey bee farmer.
     */
    public String getId() {
        return this.id;
    }

    /*
     * Set the apiaries operated by the honey bee farmer.
     */
    public void setApiaries(ArrayList<Apiary> apiaries) {
        this.apiaries = apiaries;
    }

    /*
     * Get the apiaries ArrayList of the apiaries operated by the honey
     * bee farmer.
     */
    public ArrayList<Apiary> getApiaries() {
        return this.apiaries;
    }

    /*
     * Set the location of the honey bee farmer.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /*
     * Set the SMS Send Behavior.
     * Return:
     *      - void
     */
    public void setSmsSendBehavior(SmsSendBehavior ssb) {
        this.smsSendBehavior = ssb;
    }

    /*
     * Get the location of the honey bee farmer.
     * Return:
     *      - Location: The location corresponding to the honey bee farmer.
     */
    public Location getLocation() {
        return this.location;
    }

    /*
     * Check if the honey bee farmer is registered within the database.
     */
    public boolean exists() {
        // Simulate the retrieval from a database.
        return this.db.exists(this.telephoneNumber);
    }

    /*
     * Send an SMS message to the number corresponding to the honey bee farmer.
     */
    public boolean sendSMS(String body) {
        return this.smsSendBehavior.sendSMS(this.telephoneNumber.toString(), body);
    }
}
