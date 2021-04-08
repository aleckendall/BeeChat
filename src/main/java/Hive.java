public class Hive {
    private String name;
    private Location location;
    private Integer beeCount;
    private Integer age;

    public Hive(String name, Location location, Integer beeCount, Integer age) {
        this.name = name;
        this.location = location;
        this.beeCount = beeCount;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setBeeCount(Integer beeCount) {
        this.beeCount = beeCount;
    }

    public Integer getBeeCount() {
        return this.beeCount;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return this.age;
    }


}
