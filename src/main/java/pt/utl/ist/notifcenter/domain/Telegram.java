package pt.utl.ist.notifcenter.domain;

@AnotacaoCanal(classFields = {"id", "authToken"})
public class Telegram extends Telegram_Base {
    
    public Telegram() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

}
