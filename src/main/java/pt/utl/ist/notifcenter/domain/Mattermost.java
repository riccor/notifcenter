package pt.utl.ist.notifcenter.domain;

public class Mattermost extends Mattermost_Base {
    
    public Mattermost() {
        super();
    }

    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);
    }


}
