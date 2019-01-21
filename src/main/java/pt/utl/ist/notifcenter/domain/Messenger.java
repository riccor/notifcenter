package pt.utl.ist.notifcenter.domain;

import javax.servlet.http.HttpServletRequest;

@AnotacaoCanal//(classFields = {"numeroTelemovel", "tokenAcesso"})
public class Messenger extends Messenger_Base {
    
    public Messenger() {
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
