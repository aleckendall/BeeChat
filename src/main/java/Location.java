public class Location {

    private String country;
    private Double latitude;
    private Double longitude;

    public Location(String country, Double latitude, Double longitude) {
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

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }
}
