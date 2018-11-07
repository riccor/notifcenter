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
        return canalNotificacao;
    }


}
