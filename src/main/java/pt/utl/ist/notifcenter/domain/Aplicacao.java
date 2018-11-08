package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.oauth.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TEST:
//List<ExternalApplicationScope> mainList = new ArrayList<>(FenixFramework.getDomainRoot().getBennu().getScopesSet());
//app.updateAplicacaoScopes(mainList);

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
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static Aplicacao createAplicacao(final String name, final String redirectUrl, final String description, final String authorName, final String siteUrl) {

        if (findByAplicacaoName(name) != null) {
            throw BennuCoreDomainException.cannotCreateEntity();
        }

        Aplicacao app = new Aplicacao();
        app.setName(name);
        app.setRedirectUrl(redirectUrl);
        app.setDescription(description);
        app.setPermissoesAplicacao(AppPermissions.NONE);
        app.setAuthorName(authorName);
        app.setSiteUrl(siteUrl);

        cacheAplicacao(app);

        return app;
    }

    @Atomic
    public Aplicacao setAppName(final String nome) {
        this.setName(nome);
        return this;
    }

    @Atomic
    public Aplicacao setAppPermissions(final AppPermissions appPermissions) {
        this.setPermissoesAplicacao(appPermissions);
        return this;
    }

    @Atomic
    public Aplicacao setAppScopes(final List<ExternalApplicationScope> newScopes) {
        this.setScopeList(newScopes);
        return this;
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

    private static Aplicacao manualFind(final String aplicacaoName) {
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

    /*
    public Remetente doesHaveRemetente(final String remetente) {

        for (final Remetente r: this.getRemetentesSet()) {
            if(r.getNome().equals(remetente)) {
                return r;
            }
        }

        return null;
    }
    */


}
