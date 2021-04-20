import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class SimulateHoneyBeeFarmerDB {
    private HashMap<String, HoneyBeeFarmer> honeyBeeFarmers;
    private volatile static SimulateHoneyBeeFarmerDB uniqueInstance;
    private SimulateHoneyBeeFarmerDB(){
        honeyBeeFarmers = new HashMap<>();
        ArrayList<Apiary> apiaries = new ArrayList<>();
        apiaries.add(new Apiary("Big Hill"));
        apiaries.add(new Apiary("Small Rock"));
        HoneyBeeFarmer me = new TestHBF("Alec", "Kendall", "+19198306807", "0", apiaries, null);
        honeyBeeFarmers.put(me.getTelephoneNumber().toString(), me);
    }

    public static SimulateHoneyBeeFarmerDB getInstance() {
        if(uniqueInstance == null) {
            synchronized (SimulateHoneyBeeFarmerDB.class) {
                if(uniqueInstance == null) {
                    uniqueInstance = new SimulateHoneyBeeFarmerDB();
                }
            }
        }
        return uniqueInstance;
    }

    public boolean exists(String pn) {
        return honeyBeeFarmers.containsKey(pn);
    }

    public HoneyBeeFarmer getHoneyBeeFarmer(String pn) {
        return honeyBeeFarmers.get(pn);
    }

    /*
     * Update the honey bee farmer.
     */
    public void updateHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        honeyBeeFarmers.put(hbf.getTelephoneNumber().toString(), hbf);
    }

    public void addHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        this.honeyBeeFarmers.put(hbf.getTelephoneNumber().toString(), hbf);
    }

    public void removeHoneyBeeFarmer(HoneyBeeFarmer hbf) {
        this.honeyBeeFarmers.remove(hbf.getTelephoneNumber());
    }
}
