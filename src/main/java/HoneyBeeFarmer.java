import java.util.ArrayList;
import java.util.Date;

public class HoneyBeeFarmer {
    protected String firstName;
    protected String lastName;
    protected PhoneNumber telephoneNumber;
    protected String id;
    protected ArrayList<Apiary> apiaries;
    protected Integer apiaryCount;
    protected Location location;
    protected SmsSendBehavior smsSendBehavior;
    protected SimulateHoneyBeeFarmerDB db;

    public HoneyBeeFarmer(String telephoneNumber) {
        this.setTelephoneNumber(new PhoneNumber(telephoneNumber));
        db = SimulateHoneyBeeFarmerDB.getInstance();
        if(db.exists(this.telephoneNumber.toString())) {
            HoneyBeeFarmer profile = db.getHoneyBeeFarmer(this.telephoneNumber.toString());
            this.setProperties(profile);
        } else {
            setTelephoneNumber(new PhoneNumber(telephoneNumber));
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
        apiaryCount = count;
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
     * Remove the apiary from the honey bee farmer account.
     * Return:
     *      - void
     */
    public void removeApiary(Apiary apiary) {
        for(int i = 0; i < apiaries.size(); i++) {
            if (apiaries.get(i).getName().compareTo(apiary.getName()) == 0) {
                apiaries.remove(i);
                return;
            }
        }
        // apiary already removed from the apiaries for the hbf.
        return;
    }

    /*
     * Add an apiary to the list of apiaries.
     * Return:
     *      - void.
     */
    public void addApiary(Apiary apiary) {
        if(this.apiaries == null) {
           apiaries = new ArrayList<>();
        }
        this.apiaries.add(apiary);
    }

    /*
     * Get an apiary given its name.
     * Return:
     *      - Apiary apiary The apiary with the match name
     *      - null No apiaries with a name matching the argument
     */
    public Apiary getApiary(String name) {
        for(final Apiary apiary : apiaries) {
            if(apiary.getName().compareTo(name) == 0) {
                return apiary;
            }
        }
        return null;
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
     * Get the formatted string list of apiaries:
     * Return:
     *      - String apiaries The string representation of the apiaries
     */
    public String getApiaryList() {
        String apiaryString = "";
        int apiaryIndex = 1;
        for(final Apiary apiary : this.apiaries) {
            apiaryString += apiaryIndex++ + ". " + apiary.getName() + "\n";
        }
        return apiaryString;
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
        return this.db.exists(this.telephoneNumber.toString());
    }

    /*
     * Send an SMS message to the number corresponding to the honey bee farmer.
     */
    public boolean sendSMS(String body) {
        return this.smsSendBehavior.sendSMS(this.telephoneNumber.toString(), body);
    }

    /*
     * @return String The honey bee farmer represented as a string
     */
    public String toString() {
        return "Name: " + firstName + " " + lastName + ", PhoneNumber: " + telephoneNumber.toString() + ", Apiary Count: " + apiaryCount;
    }
}
