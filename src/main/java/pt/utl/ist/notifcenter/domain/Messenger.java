package pt.utl.ist.notifcenter.domain;

@AnotacaoCanal(classFields = {"numeroTelemovel", "tokenAcesso"})
public class Messenger extends Messenger_Base {
    
    public Messenger() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

}
