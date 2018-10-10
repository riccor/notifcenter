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

    @Override
    public void contextInitialized(ServletContextEvent event) {

        logger.info( "Starting application..." );

        String id = SistemaNotificacoes.getInstance().getExternalId();
        System.out.println("###################################################### SistemaNotificacoes external id:" + id);

        //add app test:
        //Aplicacao app = Aplicacao.createAplicacao("app test nameeeeeeee");
        //System.out.println("#################################################### app id :" + app.getExternalId().toString());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event){

    }
}
