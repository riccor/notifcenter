package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class CanalNotificacao extends CanalNotificacao_Base {

    private CanalNotificacao() {
        super();
    }

    //3.1.6
    @Atomic
    public static CanalNotificacao createCanalNotificacao(Canal canal, Remetente remetente) {
        CanalNotificacao canalNotificacao = new CanalNotificacao();
        canalNotificacao.setCanal(canal);
        canalNotificacao.setRemetente(remetente);
        canalNotificacao.setAguardandoAprovacao(true); //default value //3.1.4
        return canalNotificacao;
    }

    //3.1.4
    /*@Atomic
    public static CanalNotificacao createPedidoCriacaoCanalNotificacao(Canal canal, Remetente remetente) {
        CanalNotificacao canalNotificacao = new CanalNotificacao();
        canalNotificacao.setCanal(canal);
        canalNotificacao.setRemetente(remetente);

        canalNotificacao.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
        return canalNotificacao;
    }
    */

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

        //NOTA: ao eliminar canal de notificacao todas as mensagens associadas a ele sao também eliminadas
        for (Mensagem m : this.getMensagemSet()) {
            m.delete();
        }

        this.deleteDomainObject();
    }


}