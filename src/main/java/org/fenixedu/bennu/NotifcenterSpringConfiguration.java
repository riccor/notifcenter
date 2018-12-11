package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;
//import org.springframework.context.annotation.Configuration;

//import org.fenixedu.commons.configuration.ConfigurationProperty;

//Este procura recursos em ../WEB-INF/resources/[notifcenter e mytest]/Resources.properties
//para o método "addInterceptors" funcionar, adicionei "extends WebMvcConfigurerAdapter"

//EnableAsync
@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources", "MyTestResources"})
public class NotifcenterSpringConfiguration /*extends WebMvcConfigurationSupport*/ {

    // Email LC 3-10-2018:
    public static final String BUNDLE = "resources.NotifcenterResources";

    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NotifcenterInterceptor());
    }
    */

    @ConfigurationManager(description = "Notifcenter Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "notifcenter.url", defaultValue = "http://localhost:8080/notifcenter")
        public String notifcenterUrl();

        //@ConfigurationProperty(key = "notifcenter.mytest.url", defaultValue = "http://localhost:8080/notifcenter")
        //public String notifcenterMyTestUrl();

        @ConfigurationProperty(key = "notifcenter.channels.credentials", defaultValue = "channelscredentials/%s.properties")
        public String notifcenterChannelsCredentials();

        @ConfigurationProperty(key = "notifcenter.mensagem.textocurto.maxsize", defaultValue = "90")
        public String notifcenterMensagemTextoCurtoMaxSize();

        @ConfigurationProperty(key = "notifcenter.filestorage.name", defaultValue = "notistore-0")
        public String notifcenterFileStorageName();

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
