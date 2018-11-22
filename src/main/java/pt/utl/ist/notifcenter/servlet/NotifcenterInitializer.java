package pt.utl.ist.notifcenter.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.utl.ist.notifcenter.domain.Aplicacao;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class NotifcenterInitializer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(NotifcenterInitializer.class);

    /*
    //My scopes:
    private static final Locale enUS = Locale.forLanguageTag("en-US");
    private static final String[] scopeKeys = {"scope1", "scope2", "scope3"};

    @Atomic
    private void bootUpMyScopes() {
        Set<ExternalApplicationScope> set = FenixFramework.getDomainRoot().getBennu().getScopesSet();

        for(String scopeKey : scopeKeys) {
            if (set.stream().noneMatch(n -> n.getScopeKey().equals(scopeKey))) {
                ExternalApplicationScope eas = new ExternalApplicationScope();
                eas.setScopeKey(scopeKey);
                eas.setName(new LocalizedString.Builder().with(enUS, scopeKey + " scope").build());
                eas.setDescription(new LocalizedString.Builder().with(enUS, scopeKey + " scope description").build());
                eas.setService(Boolean.FALSE);
                eas.setBennu(FenixFramework.getDomainRoot().getBennu());
            }
        }
    }*/

    @Override
    public void contextInitialized(ServletContextEvent event) {
        logger.info( "Starting application..." );
        String id = SistemaNotificacoes.getInstance().getExternalId();
        System.out.println("################################################# SistemaNotificacoes external id:" + id);

        //carregar cache de Aplicacoes (usada para pesquisa rápida de Aplicacoes por nome):
        Aplicacao.loadCacheAplicacoes();

        ///carregar scopes:
        ///bootUpMyScopes();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event){

    }
}
