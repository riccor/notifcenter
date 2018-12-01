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
    public static EstadoDeEntregaDeMensagemEnviadaAContacto createEstadoDeEntregaDeMensagemEnviadaAContacto(Canal canal, Mensagem msg, Contacto contacto, String idExterno, String estadoEntrega) {
        EstadoDeEntregaDeMensagemEnviadaAContacto estadoDeEntregaDeMensagemEnviadaAContacto = new EstadoDeEntregaDeMensagemEnviadaAContacto(canal, msg, contacto);
        estadoDeEntregaDeMensagemEnviadaAContacto.setIdExterno(idExterno);
        estadoDeEntregaDeMensagemEnviadaAContacto.setEstadoEntrega(estadoEntrega);
        return estadoDeEntregaDeMensagemEnviadaAContacto;
    }

    @Atomic
    public void delete() {
        this.getCanal().removeEstadoDeEntregaDeMensagemEnviadaAContacto(this);
        this.setCanal(null); ///

        this.getMensagem().removeEstadoDeEntregaDeMensagemEnviadaAContacto(this);
        this.setMensagem(null); ///

        this.getContacto().removeEstadoDeEntregaDeMensagemEnviadaAContacto(this);
        this.setContacto(null); ///

        this.deleteDomainObject();
    }

}
