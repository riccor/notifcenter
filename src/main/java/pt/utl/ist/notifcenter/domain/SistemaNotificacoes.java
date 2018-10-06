package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    public SistemaNotificacoes() {
        super();
    }

    @Atomic
    public static Aplicacao createAplicacao(final String nome) {
        Aplicacao app = new Aplicacao();
        app.setName(nome);
        app.setPermissoesAplicacao(AppPermissions.RREQUIRES_APPROVAL);
        return app;
    }


    public static void main(String [] args) {

        Aplicacao app1 = createAplicacao("app1");

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

}
