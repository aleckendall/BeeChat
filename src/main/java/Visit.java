import java.util.ArrayList;
import java.util.Date;

public class Visit {

    private Date visitDate;
    private String id;
    private ArrayList<Task> tasks;
    //Currently not used.
    private Hive hive;

    public Visit(Date visitDate) {
        this.visitDate = visitDate;
    }

    public Visit(Date visitDate, String id, ArrayList<Task> tasks) {
        this.visitDate = visitDate;
        this.id = id;
        this.tasks = tasks;
    }

    /*
     * Set the date the Hive was visited.
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    /*
     * Get the date of the visit.
     */
    public Date getVisitDate() {
        return this.visitDate;
    }

    /*
     * Get the id for the visit.
     */
    public String getId() {
        return this.id;
    }

    /*
     * Set the id for the visit.
     */
    public void setId(String id) {
        this.id = id;
    }

    /*
     * Set the hive for the visit.
     */
    public void setHive(Hive hive) {
        this.hive = hive;
    }

    /*
     * Get the hive for the visit.
     */
    public Hive getHive() {
        return this.hive;
    }

    /*
     * Set the Tasks ArrayList to an Tasks ArrayList.
     */
    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        return;
    }

    /*
     * Get the Tasks ArrayList.
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /*
     * Get a task from the Tasks ArrayList using the Task type.
     * Returns null if the task is not found.
     */
    public Task getTask(String type) {
        for(Task e : this.tasks) {
            if(e.getType().equals(type)) {
                return e;
            }
        }

        System.out.println("Task with type \"" + type + "\" does not exist.");
        return null;
    }

    /*
     * Set a task to a new Task object.
     */
    public boolean setTask(String type, Task task) {
        for(Task e : this.tasks) {
            if(e.getType().equals(type)) {
                e = task;
                return true;
            }
        }

        System.out.println("Task with type \"" + type + "\" does not exist.");
        return false;
    }


    /*
     * Add a task to the Tasks ArrayList.
     */
    public void addTask(Task e) {
        this.tasks.add(e);
    }

    /*
     *  Remove a task if it is found in the Tasks ArrayList.
     */
    public void removeTask(Task e) {
        if(this.tasks.contains(e)) {
            this.tasks.remove(e);
        }
    }
}
