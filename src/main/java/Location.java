public class Location {

    private String country;
    private double latitude;
    private double longitude;

    public Location(String country, double latitude, double longitude) {
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setCountry(String country) {
        this.country = country;
        return;
    }

    public String getCountry() {
        return this.country;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}
