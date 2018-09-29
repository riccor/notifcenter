package pt.utl.ist.notifcenter.domain;

public class Aplicacao extends Aplicacao_Base {
    
    public Aplicacao() {
        super();
    }

    public void updatePermissions(AppPermissions permissions){

        this.setPermissoesAplicacao(permissions);
    }

}
