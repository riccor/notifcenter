package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.groups.Group;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    private SistemaNotificacoes() {
        super();
        this.setRoot(FenixFramework.getDomainRoot());
        Group.dynamic("notificationAdmins").toPersistentGroup();
        Group.
    }

    @Atomic
    public static SistemaNotificacoes getInstance() {
        //LC 8-10-2018
        final SistemaNotificacoes sistema = FenixFramework.getDomainRoot().getSistemaNotificacoes();
        return sistema == null ? new SistemaNotificacoes() : sistema;
    }


    //como fazer print


}
