package pt.utl.ist.notifcenter.domain;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

public class Mensagem extends Mensagem_Base {

    public Mensagem() {
        super();
    }

    //Verifies if a user can view a message
    public boolean isAccessible(User user) {
        for (PersistentGroup pg : this.getGruposDestinatariosSet()) {
            if (pg.isMember(user)) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public static Mensagem createMensagem(final CanalNotificacao canalNotificacao, final PersistentGroup[] gruposDestinatarios, final String assunto, final String textoCurto, final String textoLongo, @Nullable final DateTime dataEntrega, @Nullable final String callbackUrlEstadoEntrega) {

        //Imposing a maximum length for TextoCurto
        /*
        if (textoCurto.length() > Integer.parseInt(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemTextoCurtoMaxSize())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "TextoCurto must be at most " +
                    NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemTextoCurtoMaxSize() + " characters long.");
        }
         */

        Mensagem mensagem = new Mensagem();
        mensagem.setCanalNotificacao(canalNotificacao);

        for (PersistentGroup g : gruposDestinatarios) {
            mensagem.addGruposDestinatarios(g);
        }

        mensagem.setAssunto(assunto);
        mensagem.setTextoCurto(textoCurto);
        mensagem.setTextoLongo(textoLongo);

        if (callbackUrlEstadoEntrega != null) {
            mensagem.setCallbackUrlEstadoEntrega(callbackUrlEstadoEntrega);
        }
        else {
            mensagem.setCallbackUrlEstadoEntrega("none");
        }

        //not implemented (this feature would allow a message being sent in a future date)
        if (dataEntrega != null) {
            mensagem.setDataEntrega(dataEntrega);
        }
        else {
            mensagem.setDataEntrega(DateTime.now());
        }

        return mensagem;
    }

    @Atomic
    public void addAttachment(Attachment at) {
        this.addAttachments(at);
    }

    //Simulating "message content adaptation to a channel" feature
    public String createSimpleMessageNotificationWithLink() {
        String linkForMessage = " Check " + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/mensagens/" + this.getExternalId();
        String simple = this.getTextoCurto() + linkForMessage;
        return simple;
    }

    @Atomic
    public void delete() {
        this.getCanalNotificacao().removeMensagem(this);
        this.setCanalNotificacao(null);

        for (PersistentGroup g : this.getGruposDestinatariosSet()) {
            g.removeMensagem(this);
            this.removeGruposDestinatarios(g);
        }

        for (Attachment a : this.getAttachmentsSet()) {
            a.delete();
        }

        for (UserMessageDeliveryStatus e : this.getUserMessageDeliveryStatusSet()) {
            e.delete();
        }

        this.deleteDomainObject();
    }

}
