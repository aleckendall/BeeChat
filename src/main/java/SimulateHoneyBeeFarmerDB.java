import java.util.HashMap;

public class SimulateHoneyBeeFarmerDB {
    private HashMap<PhoneNumber, HoneyBeeFarmer> honeyBeeFarmers;

    public SimulateHoneyBeeFarmerDB(){
        honeyBeeFarmers = new HashMap<>();
        honeyBeeFarmers.put(new PhoneNumber("+19198306807"), new USAHBF("Alec", "Kendall", "+19198306807", "0", null, null));
    }

    public boolean exists(PhoneNumber pn) {
        return honeyBeeFarmers.containsKey(pn);
    }

    public HoneyBeeFarmer getHoneyBeeFarmer(PhoneNumber pn) {
        return honeyBeeFarmers.get(pn);
    }

    /*
     * Update the honey bee farmer.
     */
    public void updateHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        honeyBeeFarmers.put(hbf.getTelephoneNumber(), hbf);
    }

    public void addHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        this.honeyBeeFarmers.put(hbf.getTelephoneNumber(), hbf);
    }
}
