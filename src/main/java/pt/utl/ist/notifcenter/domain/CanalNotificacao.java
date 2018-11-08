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
        return canalNotificacao;
    }

    //3.1.4
    @Atomic
    public static CanalNotificacao createPedidoCriacaoCanalNotificacao(Canal canal, Remetente remetente) {
        CanalNotificacao canalNotificacao = new CanalNotificacao();
        canalNotificacao.setCanal(canal);
        canalNotificacao.setRemetente(remetente);

        canalNotificacao.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
        return canalNotificacao;
    }


}

