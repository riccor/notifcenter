package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
import org.fenixedu.commons.configuration.ConfigurationProperty;
//import org.fenixedu.commons.configuration.ConfigurationProperty;

//Este procura recursos em ../WEB-INF/resources/[notifcenter e mytest]/Resources.properties
@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources", "MyTestResources"})
public class NotifcenterSpringConfiguration {

    // Email LC 3-10-2018:
    public static final String BUNDLE = "resources.NotifcenterResources";

    @ConfigurationManager(description = "Notifcenter Configuration")
    public interface ConfigurationProperties {

        @ConfigurationProperty(key = "notifcenter.url", defaultValue = "http://localhost:8080/notifcenter")
        public String notifcenterUrl();

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
