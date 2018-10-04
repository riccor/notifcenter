package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;
import org.fenixedu.commons.configuration.ConfigurationInvocationHandler;
import org.fenixedu.commons.configuration.ConfigurationManager;
//import org.fenixedu.commons.configuration.ConfigurationProperty;

//Este procura recursos em ../WEB-INF/resources/[notifcenter e mytest]/Resources.properties
@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources", "MyTestResources"})
public class NotifcenterSpringConfiguration {

    // Email LC 3-10-2018:
    public static final String BUNDLE = "resources.NotifcenterResources";

    @ConfigurationManager(description = "Notifcenter Configuration")
    public interface ConfigurationProperties {

    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigurationInvocationHandler.getConfiguration(ConfigurationProperties.class);
    }

}
