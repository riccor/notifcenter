package pt.utl.ist.notifcenter.domain;

@AnotacaoCanal//(classFields = {"id", "numeroTelemovel", "tokenAutorizacao"})
public class Telegram extends Telegram_Base {
    
    public Telegram() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }


}
