package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.groups.Group;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    private SistemaNotificacoes() {
        super();
        this.setRoot(FenixFramework.getDomainRoot());

        //LC 31-1-2019
        //"notifcenterAdmins" group only manages apps (app permissions, grupos destinatarios, canais notificacao, remetentes) and users contacts via GUI
        //"managers" group from Bennu can do anything, specially compiling code and editing/adding/removing channels
        Group.dynamic(NotifcenterSpringConfiguration.getConfiguration().notifcenterAdminsGroupName()).toPersistentGroup();
    }

    @Atomic
    public static SistemaNotificacoes getInstance() {
        //LC 8-10-2018
        final SistemaNotificacoes sistema = FenixFramework.getDomainRoot().getSistemaNotificacoes();
        return sistema == null ? new SistemaNotificacoes() : sistema;
    }

}
