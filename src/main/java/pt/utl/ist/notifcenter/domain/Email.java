package pt.utl.ist.notifcenter.domain;

import javax.servlet.http.HttpServletRequest;

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


    public EstadoDeEntregaDeMensagemEnviadaAContacto dealWithMessageDeliveryStatusCallback(HttpServletRequest request) {

        return null;
    }

}
