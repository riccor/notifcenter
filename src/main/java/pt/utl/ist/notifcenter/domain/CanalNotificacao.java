package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class CanalNotificacao extends CanalNotificacao_Base {

    private CanalNotificacao() {
        super();
    }

    @Atomic
    public static CanalNotificacao createCanalNotificacao(Canal canal, Remetente remetente) {
        CanalNotificacao canalNotificacao = new CanalNotificacao();
        canalNotificacao.setCanal(canal);
        canalNotificacao.setRemetente(remetente);
        canalNotificacao.setAguardandoAprovacao(true);
        return canalNotificacao;
    }

    @Atomic
    public void approveCanalNotificacao() {
        this.setAguardandoAprovacao(false);
    }

    @Atomic
    public void disapproveCanalNotificacao() {
        this.setAguardandoAprovacao(true);
    }

    public boolean isApproved() {
        return !this.getAguardandoAprovacao();
    }

    @Atomic
    public void delete() {
        this.getRemetente().removeCanaisNotificacao(this);
        this.setRemetente(null); ///

        this.getCanal().removeCanalNotificacao(this);
        this.setCanal(null); ///

        for (Mensagem m : this.getMensagemSet()) {
            m.delete();
        }

        this.deleteDomainObject();
    }

}
