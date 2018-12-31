package pt.utl.ist.notifcenter.domain;

import java.util.HashMap;

@AnotacaoCanal(classFields = {"numeroTelemovel", "tokenAcesso"})
public class Messenger extends Messenger_Base {
    
    public Messenger() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

    /*
    public String toString() {
        return "NumeroTelemovel: " + getNumeroTelemovel() + " TokenAcesso: " + getTokenAcesso();
    }*/

    public HashMap<String, String> getParams() {
        return new HashMap<String, String>();
    }

}
