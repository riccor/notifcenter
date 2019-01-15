package pt.utl.ist.notifcenter.domain;

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

    /*
    public String toString() {
        return "NumeroTelemovel: " + getNumeroTelemovel() + " TokenAcesso: " + getTokenAcesso();
    }*/

}
