package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    public SistemaNotificacoes() {
        super();
    }

    @Atomic
    public static SistemaNotificacoes createSistemaNotificacoes() {
        SistemaNotificacoes sistema = new SistemaNotificacoes();
        return sistema;
    }

    public static void main() {


       // SistemaNotificacoes sistemaNotificacoes = createSistemaNotificacoes();



       // sistemaNotificacoes.addAplicacoes();


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
    }



}
