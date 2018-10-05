package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class Aplicacao extends Aplicacao_Base {
    
    public Aplicacao() {
        super();
    }

    /*public void updatePermissions(AppPermissions permissions){

        this.setPermissoesAplicacao(permissions);
    }
    */

    @Atomic
    public static Aplicacao createAplicacao(final String nome) {
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        return app;
    }

}
