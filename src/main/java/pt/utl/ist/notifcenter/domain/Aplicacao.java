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
        //SistemaNotificacoes sist = new SistemaNotificacoes(); //NOTA: com ou sem isto tambem não dá
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);
        //app.setSistema_notificacoes(sist); //NOTA: com ou sem isto tambem não dá
        //sist.addAplicacoes(app); //NOTA: com ou sem isto tambem não dá
        return app;
    }

    @Atomic
    public static Aplicacao createAplicacao(final String nome, SistemaNotificacoes sistem) {
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);
        //app.setSistema_notificacoes(sistem);
        //sistem.addAplicacoes(app);
        return app;
    }

}
