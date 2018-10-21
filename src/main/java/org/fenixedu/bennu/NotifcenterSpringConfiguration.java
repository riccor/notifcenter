package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringConfiguration;
import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;
//import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pt.utl.ist.notifcenter.security.AccessTokenVerificationInterceptor;
//import org.fenixedu.commons.configuration.ConfigurationProperty;

//Este procura recursos em ../WEB-INF/resources/[notifcenter e mytest]/Resources.properties
//@Configuration - nao é necessário pôr isto para o addInterceptors() funcionar, tal como visto na internet (pois o @BennuSpringModule já deve fazer a mesma coisa)
@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources", "MyTestResources"})
public class NotifcenterSpringConfiguration extends WebMvcConfigurationSupport { //para o addInterceptors funcionar, adicionei "extends WebMvcConfigurerAdapter"

    // Email LC 3-10-2018:
    public static final String BUNDLE = "resources.NotifcenterResources";

    //@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessTokenVerificationInterceptor());
    }

    @ConfigurationManager(description = "Notifcenter Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "notifcenter.url", defaultValue = "http://localhost:8080/notifcenter")
        public String notifcenterUrl();

        //@ConfigurationProperty(key = "access.token.required", defaultValue = "true")
        //public String accessTokenRequired();

        /*
        Para aceder a estas configuracoes noutra classe:
        import org.fenixedu.bennu.NotifcenterSpringConfiguration;
        NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl();
        */
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
