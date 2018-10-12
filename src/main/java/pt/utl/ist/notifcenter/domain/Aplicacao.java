package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//import pt.ist.fenixframework.consistencyPredicates.ConsistencyPredicate;

public class Aplicacao extends Aplicacao_Base {

    //cache de Aplicacoes
    private static Map<String, Aplicacao> map = new ConcurrentHashMap<>();

    /* DML:
    public class ExternalApplication  {
        public String name (REQUIRED);
        protected String secret (REQUIRED);
        public String redirectUrl (REQUIRED);
        public String description (REQUIRED);
        public String siteUrl;
        public bytearray logo;
        protected ExternalApplicationState state;
        public String authorName;
    }*/

    private Aplicacao() {
        super();
        this.setPermissoesAplicacao(AppPermissions.NONE);
        this.setAuthorName("app author name");
        this.setSiteUrl("app site url");
        this.setRedirectUrl("redirect url");
        this.setDescription("app description");
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

    // /oauth/register/{appname}
    public static Aplicacao registerAplicacao(final String nome){
        Aplicacao app = createAplicacao(nome);
        cacheAplicacao(app);
        return app;
    }

    // para otimizacao da pesquisa de determinada Aplicacao por nome (retirado de ../bennu/core/domain/User):
    public static Aplicacao findByAplicacaoName(final String aplicacaoName) {
        if (aplicacaoName == null) {
            return null;
        }
        Aplicacao match = (match = map.get(aplicacaoName)) == null ? manualFind(aplicacaoName) : match;
        if (match == null) {
            return null;
        }
        if (!FenixFramework.isDomainObjectValid(match) || !match.getName().equals(aplicacaoName)) {
            map.remove(aplicacaoName, match);
            return findByAplicacaoName(aplicacaoName);
        }
        return match;
    }

    private static Aplicacao manualFind(String aplicacaoName) {
        for (final Aplicacao app: SistemaNotificacoes.getInstance().getAplicacoesSet()) {
            cacheAplicacao(app);
            if (app.getName().equals(aplicacaoName)) {
                return app;
            }
        }
        return null;
    }

    private static void cacheAplicacao(Aplicacao app) {
        map.putIfAbsent(app.getName(), app);
    }

    @Atomic
    public static void loadCacheAplicacoes() {
        for (final Aplicacao app: SistemaNotificacoes.getInstance().getAplicacoesSet()) {
            cacheAplicacao(app);
        }
    }

}
