package pt.utl.ist.notifcenter.domain;

import javax.servlet.http.HttpServletRequest;

public class Mattermost extends Mattermost_Base {
    
    public Mattermost() {
        super();
    }

    @Override
    public String getUri() {
        return null;
    }

    @Override
    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);
    }

    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }


}
