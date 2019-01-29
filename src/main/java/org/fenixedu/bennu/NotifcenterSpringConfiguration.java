package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import pt.utl.ist.notifcenter.security.NotifcenterInterceptor;
//import org.springframework.context.annotation.Configuration;

//import org.fenixedu.commons.configuration.ConfigurationProperty;

//Este procura recursos em ../WEB-INF/resources/[notifcenter e mytest]/Resources.properties

//NOTA:
//para o método "addInterceptors" funcionar, adicionei "extends WebMvcConfigurationSupport ou WebMvcConfigurerAdapter"

//EnableAsync
@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources", "MyTestResources"})
public class NotifcenterSpringConfiguration /*extends WebMvcConfigurationSupport*/ {

    // Email LC 3-10-2018:
    public static final String BUNDLE = "resources.NotifcenterResources";

    //NOTA: descomentar para ativar o NotifcenterInterceptor:
    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NotifcenterInterceptor());
    }*/

    @ConfigurationManager(description = "Notifcenter Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "notifcenter.url", defaultValue = "localhost:8080/notifcenter")
        public String notifcenterUrl();

        @ConfigurationProperty(key = "notifcenter.url.attachments", defaultValue = "localhost:8080/notifcenter/mensagens/attachments/")
        public String notifcenterUrlForAttachments();

        //@ConfigurationProperty(key = "notifcenter.group.admin.name", defaultValue = "managers")
        //public String notifcenterGroupAdminName();

        @ConfigurationProperty(key = "notifcenter.domain", defaultValue = "pt.utl.ist.notifcenter.domain")
        public String notifcenterDomain();

        //@ConfigurationProperty(key = "notifcenter.mytest.url", defaultValue = "http://localhost:8080/notifcenter")
        //public String notifcenterMyTestUrl();

        @ConfigurationProperty(key = "notifcenter.channels.credentials", defaultValue = "channelscredentials/%s.properties")
        public String notifcenterChannelsCredentials();

        @ConfigurationProperty(key = "notifcenter.mensagem.textocurto.maxsize", defaultValue = "90")
        public String notifcenterMensagemTextoCurtoMaxSize();

        @ConfigurationProperty(key = "notifcenter.mensagem.assunto.maxsize", defaultValue = "256")
        public String notifcenterMensagemAssuntoMaxSize();

        @ConfigurationProperty(key = "notifcenter.mensagem.attachment.maxsize", defaultValue = "25000000") //bytes
        public String notifcenterMensagemAttachmentMaxSizeBytes();

        @ConfigurationProperty(key = "notifcenter.filestorage.name", defaultValue = "notistore-1")
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
