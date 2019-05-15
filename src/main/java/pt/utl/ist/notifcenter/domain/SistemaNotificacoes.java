package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.groups.Group;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    private SistemaNotificacoes() {
        super();
        this.setRoot(FenixFramework.getDomainRoot());

        //"notifcenterAdmins" manage everything except channels
        this.setNotifcenterAdminsGroup(Group.dynamic(NotifcenterSpringConfiguration.getConfiguration().notifcenterAdminsGroupName()).toPersistentGroup());

        //"managers" group from Bennu can do anything, specially compiling code and editing/adding/removing channels
        this.setNotifcenterDevelopersGroup(Group.managers().toPersistentGroup());

        //Debug (example group for users with no administration role)
        //Group.dynamic(NotifcenterSpringConfiguration.getConfiguration().notifcenterExampleGroup()).toPersistentGroup();
    }

    @Atomic
    public static SistemaNotificacoes getInstance() {
        final SistemaNotificacoes sistema = FenixFramework.getDomainRoot().getSistemaNotificacoes();
        return sistema == null ? new SistemaNotificacoes() : sistema;
    }

}
