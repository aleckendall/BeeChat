public interface SmsSendBehavior {
    boolean sendSMS(String telephoneNumber, String body);
}
