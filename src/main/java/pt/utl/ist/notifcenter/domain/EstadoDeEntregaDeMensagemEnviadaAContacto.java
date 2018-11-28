package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class EstadoDeEntregaDeMensagemEnviadaAContacto extends EstadoDeEntregaDeMensagemEnviadaAContacto_Base {

    private EstadoDeEntregaDeMensagemEnviadaAContacto(Canal canal, Mensagem msg, Contacto contacto) {
        super();
        this.setCanal(canal);
        this.setMensagem(msg);
        this.setContacto(contacto);
    }

    @Atomic
    public static EstadoDeEntregaDeMensagemEnviadaAContacto createEstadoDeEntregaDeMensagemEnviadaAContacto(String idExterno, Canal canal, Mensagem msg, Contacto contacto) {
        EstadoDeEntregaDeMensagemEnviadaAContacto estadoDeEntregaDeMensagemEnviadaAContacto = new EstadoDeEntregaDeMensagemEnviadaAContacto(canal, msg, contacto);
        estadoDeEntregaDeMensagemEnviadaAContacto.setIdExterno(idExterno);
        return estadoDeEntregaDeMensagemEnviadaAContacto;
    }

}
