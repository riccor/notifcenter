package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;
//import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class Aplicacao extends Aplicacao_Base {

    private Aplicacao() {
        super();
        this.setSistema_notificacoes(SistemaNotificacoes.getInstance());

        //SistemaNotificacoes.getInstance().addAplicacoes(this); //NOTA: esta linha também não resolve o problema.
    }

    @Atomic
    public static Aplicacao createAplicacao(final String nome) {
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        //app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);
        return app;
    }


    /*
    public void updatePermissions(AppPermissions permissions){
        this.setPermissoesAplicacao(permissions);
    }
    */

}
