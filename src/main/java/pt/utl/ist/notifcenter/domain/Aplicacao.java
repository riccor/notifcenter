package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;
//import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class Aplicacao extends Aplicacao_Base {
    
    public Aplicacao() {
        super();
    }

    /*
    public void updatePermissions(AppPermissions permissions){
        this.setPermissoesAplicacao(permissions);
    }
    */

    @Atomic
    public static Aplicacao createAplicacao(final String nome) {
        SistemaNotificacoes sist = new SistemaNotificacoes();
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);
        app.setSistema_notificacoes(sist);
        return app;
    }

}
