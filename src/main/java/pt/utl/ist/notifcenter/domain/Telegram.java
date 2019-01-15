package pt.utl.ist.notifcenter.domain;

@AnotacaoCanal//(classFields = {"id", "numeroTelemovel", "tokenAutorizacao"})
public class Telegram extends Telegram_Base {
    
    public Telegram() {
        super();
    }

    public void checkIsMessageAdequateForChannel(Mensagem msg) {

    }

    @Override
    public void sendMessage(Mensagem msg) {

        checkIsMessageAdequateForChannel(msg);
    }

}
