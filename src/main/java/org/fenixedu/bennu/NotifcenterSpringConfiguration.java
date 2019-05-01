package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import pt.utl.ist.notifcenter.security.NotifcenterInterceptor;


@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources"})
public class NotifcenterSpringConfiguration /*extends WebMvcConfigurationSupport*/ { //NOTE: Uncomment this code to enable NotifcenterInterceptor (in order to make OAuth work)

    public static final String BUNDLE = "resources.NotifcenterResources";

    //NOTE: Uncomment following code to enable NotifcenterInterceptor (in order to make OAuth work):
    /*
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NotifcenterInterceptor());
    }*/

    @ConfigurationManager(description = "Notifcenter Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "notifcenter.group.admins.name", defaultValue = "notifcenterAdmins")
        public String notifcenterAdminsGroupName();

        @ConfigurationProperty(key = "notifcenter.url", defaultValue = "localhost:8080/notifcenter")
        public String notifcenterUrl();

        @ConfigurationProperty(key = "notifcenter.url.attachments", defaultValue = "localhost:8080/notifcenter/mensagens/attachments/")
        public String notifcenterUrlForAttachments();

        @ConfigurationProperty(key = "notifcenter.domain", defaultValue = "pt.utl.ist.notifcenter.domain")
        public String notifcenterDomain();

        @ConfigurationProperty(key = "notifcenter.mensagem.textocurto.maxsize", defaultValue = "90")
        public String notifcenterMensagemTextoCurtoMaxSize();

        @ConfigurationProperty(key = "notifcenter.mensagem.assunto.maxsize", defaultValue = "256")
        public String notifcenterMensagemAssuntoMaxSize();

        @ConfigurationProperty(key = "notifcenter.mensagem.attachment.maxsize", defaultValue = "25000000") //bytes
        public String notifcenterMensagemAttachmentMaxSizeBytes();

        @ConfigurationProperty(key = "notifcenter.filestorage.name", defaultValue = "notistore-1")
        public String notifcenterFileStorageName();
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
