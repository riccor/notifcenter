package pt.utl.ist.notifcenter.domain;

public class Email extends Email_Base {
    
    public Email() {
        super();
    }

    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);
    }

}
