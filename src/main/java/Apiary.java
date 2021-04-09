import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Apiary {

    private String name;
    private Location location;
    private ArrayList<Hive> hives;
    private Integer hiveCount;
    private ArrayList<Visit> visits;
    private Date creationDate;

    public Apiary(String name, Date creationDate) {
        this.name = name;
        this.creationDate = creationDate;
    }

    public Apiary(String name, Location location, ArrayList<Hive> hives, ArrayList<Visit> visits, Date creationDate) {
        this.name = name;
        this.location = location;
        this.hives = hives;
        this.visits = visits;
        this.creationDate = creationDate;
    }

    /*
     * Get the date of the last visit to the apiary.
     * Return:
     *      - Date: lastVisit
     */
    public Date lastVisit() {
        return visits.get(visits.size() - 1).getVisitDate();
    }

    /*
     * Set the date the apiary was created.
     * Return:
     *      - void
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /*
     * Get the date the apiary was created.
     * Return:
     *      - Date creationDate
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /*
     * Set the name of the apiary.
     * Return:
     *      - void
     */
    public void setName(String name) {
        this.name = name;
        return;
    }


    /*
     * Get the name of the apiary.
     * Return:
     *      - String name: the name of the apiary
     */
    public String getName() {
        return this.name;
    }

    /*
     * Set the location of the apiary.
     * Return:
     *      - void
     */
    public void setLocation(Location location) {
        this.location = location;
    }


    /*
     * Get the location of the apiary.
     * Return:
     *      - Location location: the location of the apiary.
     */
    public Location getLocation() {
        return this.location;
    }

    /*
     * Get the number of hives at the apiary.
     * Return:
     *      - Integer: hiveCount
     */
    public Integer getHiveCount() {
        return this.hiveCount;
    }

    /*
     * Set the number of hives at the apiary.
     * Return:
     *      - void
     */
    public void setHiveCount(Integer hiveCount) {
        this.hiveCount = hiveCount;
    }

    /*
     * Set the hives ArrayList for the apiary.
     * Return:
     *      - void
     */
    public void setHives(ArrayList<Hive> hives) {
        this.hives = hives;
        return;
    }

    /*
     * Get the hives ArrayList for the apiary.
     * Return:
     *      - ArrayList<Hive> hives: the hives located at the apiary.
     */
    public ArrayList<Hive> getHives() {
        return this.hives;
    }


    /*
     * Set the visits ArrayList for the apiary.
     * Return:
     *      - void
     */
    public void setVisits(ArrayList<Visit> visits) {
        this.visits = visits;
        return;
    }


    /*
     * Get the visits ArrayList for the apiary.
     * Return:
     *      - ArrayList<Visit> visits: the visits to the apiary.
     */
    public ArrayList<Visit> getVisits() {
        return this.visits;
    }


    /*
     * Get all visits occurring on a date.
     * Return:
     *      - ArrayList<Visit> visitsOnDate: all visits occurring on the day specified.
     *      - null: There is not a visit that occurred on the specified day.
     */
    public ArrayList<Visit> getVisitsOnDate(Date visitDate) {

        ArrayList<Visit> visitsOnDate = new ArrayList<>();
        Calendar dateToFind = Calendar.getInstance();
        dateToFind.setTime(visitDate);
        Calendar dateToCompare = Calendar.getInstance();
        for(Visit v : visits) {
            dateToCompare.setTime(v.getVisitDate());
            if(dateToCompare.get(Calendar.DAY_OF_YEAR) == dateToFind.get(Calendar.DAY_OF_YEAR) &&
                dateToCompare.get(Calendar.YEAR) == dateToFind.get(Calendar.YEAR)) {
                visitsOnDate.add(v);
            }
        }
        visitsOnDate.trimToSize();
        if(visitsOnDate.size() == 0) {
            return null;
        }
        return visitsOnDate;
    }

    /*
     * Get all of the visits occurring after a given date.
     * Return:
     *      - ArrayList<Visit> visitsAfterDate: all visits occurring on the day after the specified date.
     *      - null: there is not a visit that occurs after the specified date.
     */
    public ArrayList<Visit> getVisitsAfterDate(Date afterDate) {
        ArrayList<Visit> visitsAfterDate = new ArrayList<>();
        Calendar dateToFindAfter = Calendar.getInstance();
        dateToFindAfter.setTime(afterDate);
        Calendar dateToCompare = Calendar.getInstance();
        for(int i = 0; i < visits.size(); i++) {
            dateToCompare.setTime(visits.get(i).getVisitDate());
            if(dateToCompare.get(Calendar.DAY_OF_YEAR) == dateToFindAfter.get(Calendar.DAY_OF_YEAR) &&
                    dateToCompare.get(Calendar.YEAR) == dateToFindAfter.get(Calendar.YEAR)) {
                visitsAfterDate.addAll(i, visits);
                return visitsAfterDate;
            }
        }
        return null;
    }

    /*
     * Set a visit to a visit object, given the id.
     * Return:
     *      - void
     */
    public void setVisit(String id, Visit v) {
        for(int i = 0; i < visits.size(); i++) {
            if(visits.get(i).getId().equals(id)) {
                visits.set(i, v);
                return;
            }
        }
    }


    /*
     * Remove a visit from the visits list, given the id of the visit.
     * Return:
     *      - void
     */
    public void removeVisit(String id) {
        int counter = 0;
        for(Visit v : visits) {
            if(v.getId().equals(id)) {
                visits.remove(counter);
                return;
            }
            counter++;
        }
    }
}
