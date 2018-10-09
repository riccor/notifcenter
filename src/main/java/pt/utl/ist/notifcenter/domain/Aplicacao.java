package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Set;
//import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class Aplicacao extends Aplicacao_Base {

    private Aplicacao() {
        super();
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());

        ///this.setAuthor(u1);

        //SistemaNotificacoes.getInstance().addAplicacoes(this); //NOTA: esta linha também não resolve o problema.
    }

    @Atomic
    public static Aplicacao createAplicacao(final String nome) {
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        //app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);

        //app.setBennu(FenixFramework.getDomainRoot().getBennu()); //também nao resolve
        //app.setAuthor(Utils.findUserByName("admin")); //também nao resolve
        return app;
    }

    @Atomic
    public Aplicacao updateAplicacaoNome(final String nome) {
        this.setName(nome);
        return this;
    }

    /*
    public void updatePermissions(AppPermissions permissions){
        this.setPermissoesAplicacao(permissions);
    }
    */

}
