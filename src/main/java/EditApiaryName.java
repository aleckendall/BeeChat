public class EditApiaryName extends Sequence {
    private Apiary apiaryToEdit;

    public EditApiaryName(Conversation conversation, Apiary apiaryToEdit) {
        super(conversation);
        this.apiaryToEdit = apiaryToEdit;
    }

    public void startSequence() {
        System.out.println("Begin EditApiaryName Sequence");
        String prompt = "Enter the new name of the apiary. Include only the new name of the apiary.\n\nExample: Mountain Peak";
        System.out.println(prompt);
        conversation.getHoneyBeeFarmer().sendSMS(prompt);
        this.setLive(true);
    }

    public void endSequence() {
        this.setExit(true);
        conversation.getDatabase().updateHoneyBeeFarmer(conversation.getHoneyBeeFarmer());
        conversation.addSequence(new MainMenu(conversation));
        System.out.println("End EditApiaryName Sequence\n");
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
        String prompt;
        if(response.length() == 0) {
            prompt = "The name for the apiary cannot be blank. Include only the new name of the apiary.\n\nExample: Delta Apiary";
            conversation.getHoneyBeeFarmer().sendSMS(prompt);
            return;
        }

        String orig = apiaryToEdit.getName();
        this.apiaryToEdit.setName(response);
        prompt = "Name change successful!\n\nReturning to the main menu...";
        System.out.println(prompt);
        this.conversation.getHoneyBeeFarmer().sendSMS(prompt);
        this.endSequence();
        currentMsg++;
        return;
    }

}
