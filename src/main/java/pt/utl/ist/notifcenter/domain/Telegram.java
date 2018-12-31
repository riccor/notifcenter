package pt.utl.ist.notifcenter.domain;

import java.util.HashMap;

@AnotacaoCanal(classFields = {"id", "numeroTelemovel", "tokenAutorizacao"})
public class Telegram extends Telegram_Base {
    
    public Telegram() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

    /*
    public String toString() {
        return "Id: " + getID() + " NumeroTelemovel: " + getNumeroTelemovel() + " TokenAutorizacao: " + getTokenAutorizacao();
    }*/

    public HashMap<String, String> getParams() {
        return new HashMap<String, String>();
    }

}
