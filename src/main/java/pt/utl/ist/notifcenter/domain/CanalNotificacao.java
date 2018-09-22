package pt.utl.ist.notifcenter.domain;

/*
import org.springframework.util.CollectionUtils;
*/

public class CanalNotificacao extends CanalNotificacao_Base {

    /*
    private java.util.Set<Mensagem> mensagem;
    */

    private Canal canal;
    private Remetente remetente;

    // Constructors
    public CanalNotificacao() {
        super();
    }

    public CanalNotificacao(Canal canal, Remetente remetente) {
        super();

        this.setCanal(canal);
        this.setRemetente(remetente);
    }


    // Getters and Setters

    // Role Methods

    @Override
    public pt.utl.ist.notifcenter.domain.Canal getCanal() {
        return this.canal;
    }

    @Override
    public void setCanal(pt.utl.ist.notifcenter.domain.Canal canal) {
        this.canal = canal;
    }

    @Override
    public pt.utl.ist.notifcenter.domain.Remetente getRemetente() {
        return this.remetente;
    }

    @Override
    public void setRemetente(pt.utl.ist.notifcenter.domain.Remetente remetente) {
        this.remetente = remetente;
    }

    /*
    @Override
    public void addMensagem(pt.utl.ist.notifcenter.domain.Mensagem mensagem) {
        if (CollectionUtils.isEmpty(this.mensagem)) {
            this.mensagem = new java.util.HashSet<>();
        }
        this.mensagem.add(mensagem);
    }

    @Override
    public void removeMensagem(pt.utl.ist.notifcenter.domain.Mensagem mensagem) {
        
        Utils.removeElementFromSet(this.mensagem, mensagem);
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.Mensagem> getMensagemSet() {
        return this.mensagem;
    }
    */
}
