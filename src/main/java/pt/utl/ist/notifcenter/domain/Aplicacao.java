package pt.utl.ist.notifcenter.domain;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.oauth.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

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
    public Aplicacao updateAplicacao(@Nullable final String name, @Nullable final String redirectUrl, @Nullable final String description, @Nullable final String authorName, @Nullable final String siteUrl) {

        if (Utils.isValidString(name)) {
            this.setName(name);
        }

        if (Utils.isValidString(redirectUrl)) {
            this.setRedirectUrl(redirectUrl);
        }

        if (Utils.isValidString(description)) {
            this.setDescription(description);
        }

        if (Utils.isValidString(authorName)) {
            this.setAuthorName(authorName);
        }

        if (Utils.isValidString(siteUrl)) {
            this.setSiteUrl(siteUrl);
        }

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

    @Atomic
    public void delete() {
        map.remove(this.getName(), this);

        for (Remetente r : this.getRemetentesSet()) {
            r.delete();
        }

        //needed because of ExternalApplication (note: Aplicacao extends ExternalApplication)
        this.getBennu().removeApplications(this);
        this.setBennu(null);

        this.getSistemaNotificacoes().removeAplicacoes(this);
        this.setSistemaNotificacoes(null);

        this.deleteDomainObject();
    }

}
