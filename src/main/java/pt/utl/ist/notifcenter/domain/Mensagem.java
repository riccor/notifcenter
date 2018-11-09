package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class Mensagem extends Mensagem_Base {
    
    public Mensagem() {
        super();
    }

    @Atomic
    public static Mensagem createMensagem(final String assunto, final String textoCurto, final String textoLongo, final String callbackUrlEstadoEntrega) {
        Mensagem mensagem = new Mensagem();
        mensagem.setAssunto(assunto);
        mensagem.setTextoCurto(textoCurto);
        mensagem.setTextoLongo(textoLongo);
        mensagem.setCallbackUrlEstadoEntrega(callbackUrlEstadoEntrega);

        return mensagem;
    }
}
