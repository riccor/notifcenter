package pt.utl.ist.notifcenter.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/*

Interface WebApplicationInitializer
In version 3.1, Spring introduced WebApplicationInitializer. It is an interface, which you can implement. If you do so, Spring will detect your class and execute its method  onStartup(ServletContext container), inside which you can define Configuration of your Dispatcher Servlet and do all the other config such as registering and mapping of other servlets, filters or lifecycle listeners. Most importantly, inside the method, you'll need to create your application context. This usually means having root application context and web application context. The specific class you'll need to use for your app context will depend whether you still use XML configuration or Java configuration.

*/


@WebListener //Precisamos disto para ter pagina web.
public class MyTestInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event){
    }
}
