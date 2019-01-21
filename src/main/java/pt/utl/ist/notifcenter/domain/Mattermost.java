package pt.utl.ist.notifcenter.domain;

import javax.servlet.http.HttpServletRequest;

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

    public EstadoDeEntregaDeMensagemEnviadaAContacto dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }


}
