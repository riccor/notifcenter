package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    private SistemaNotificacoes() {
        super();
        this.setRoot(FenixFramework.getDomainRoot());
    }

    @Atomic
    public static SistemaNotificacoes getInstance() {
        if(FenixFramework.getDomainRoot().getSistema_notificacoes() == null) {
            new SistemaNotificacoes();
        }
        return FenixFramework.getDomainRoot().getSistema_notificacoes();
    }

    public static void main() {
        String id = SistemaNotificacoes.getInstance().getExternalId();
        System.out.println("###################################################### SistemaNotificacoes external id:" + id);
    }

        /*
        //Aplicacao app1 = createAplicacao("app1");

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }*/



}


/*
    @Atomic
    public static SistemaNotificacoes createSistemaNotificacoes() {
        SistemaNotificacoes sistema = new SistemaNotificacoes();
        return sistema;
    }
 */