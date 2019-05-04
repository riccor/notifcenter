package pt.utl.ist.notifcenter.domain;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.oauth.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aplicacao extends Aplicacao_Base {

    //Applications cache
    private static Map<String, Aplicacao> map = new ConcurrentHashMap<>();

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

    @Atomic
    public void delete() {
        map.remove(this.getName(), this);

        for (Remetente r : this.getRemetentesSet()) {
            r.delete();
        }
        this.getBennu().removeApplications(this);
        this.setBennu(null);

        this.getSistemaNotificacoes().removeAplicacoes(this);
        this.setSistemaNotificacoes(null);

        for (ApplicationUserAuthorization aua : this.getApplicationUserAuthorizationSet()) {
            aua.delete();
        }

        this.deleteDomainObject();
    }

}
