package org.fenixedu.bennu;

import org.fenixedu.bennu.spring.BennuSpringModule;

//Este procura recursos em ../WEB-INF/resources/[notifcenter e mytest]/Resources.properties
@BennuSpringModule(basePackages = "pt.utl.ist.notifcenter", bundles = {"NotifcenterResources", "MyTestResources"})
public class NotifcenterSpringConfiguration {

}