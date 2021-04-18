public class EditApiaryName extends Sequence {
    private Apiary apiaryToEdit;

    public EditApiaryName(Conversation conversation, Apiary apiaryToEdit) {
        super(conversation);
        this.apiaryToEdit = apiaryToEdit;
    }

    public void startSequence() {
        conversation.getHoneyBeeFarmer().sendSMS("Enter the new name of the apiary. Include only the new name of the apiary.\nExample: Mountain Peak");
        this.setLive(true);
    }

    public void endSequence() {
        this.setExit(true);
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.addSequence(new MainMenu(conversation));
    }

    public void doCurrentMsg() {
        switch(currentMsg) {
            case 0:
                msg0();
                break;
            default:
                endSequence();
        }
    }


    public void msg0() {

        if(response.length() == 0) {
            conversation.getHoneyBeeFarmer().sendSMS("The name for the apiary cannot be blank. Include only the new name of the apiary.\nExample: Delta Apiary");
            return;
        }

        System.out.println("Name before: " + apiaryToEdit.getName());
        String orig = apiaryToEdit.getName();
        this.apiaryToEdit.setName(response);
        this.conversation.getHoneyBeeFarmer().sendSMS("Name change successful! Returning to the main menu.");
        this.endSequence();
        currentMsg++;
        System.out.println("Name after: " + apiaryToEdit.getName());
        return;
    }

}
