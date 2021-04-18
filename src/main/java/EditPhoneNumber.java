public class EditPhoneNumber extends Sequence {

    public EditPhoneNumber(Conversation conversation) {
        super(conversation);
    }

    public void startSequence() {
        conversation.getHoneyBeeFarmer().sendSMS("Enter the new phone number for your account. Include only the phone number. Format: " + conversation.getHoneyBeeFarmer().getTelephoneNumber());
    }
}
