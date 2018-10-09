package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Set;
//import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class Aplicacao extends Aplicacao_Base {

    private Aplicacao() {
        super();

        this.setAuthor(Utils.findUserByName("admin")); //findUserByName() retorna um objeto utilizador, neste caso o admin

        this.setPermissoesAplicacao(AppPermissions.ALLOW_ALL);

        //acrescentei isto mas também não resolve:
        this.setBennu(FenixFramework.getDomainRoot().getBennu());
        //Nota: Bennu.getInstance() é o mesmo que FenixFramework.getDomainRoot().getBennu()

        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static Aplicacao createAplicacao(final String nome) {
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        //app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);
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
