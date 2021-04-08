public class PhoneNumber {
    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    /*
     * Set the phone number.
     */
    public void setPhoneNumber(String pn) {
        this.phoneNumber = pn;
    }

    /*
     * Get the phone number.
     * Returns:
     *      - phoneNumber: String
     */
    public String getPhoneNumber() {return this.phoneNumber;}

    /*
     * The Phone Number as a string.
     * Return:
     *      - String: PhoneNumber
     */
    public String toString() { return this.phoneNumber; }
}
