package pt.utl.ist.notifcenter.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        //A entidate SistemaNotificacoes tem a funcao main() que "gere" as aplicacoes e canais;
        //(Nota: os utilizadores já sao geridos pelo fenix framework)
        SistemaNotificacoes.main();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event){

    }
}
