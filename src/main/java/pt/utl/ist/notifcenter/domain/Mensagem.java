package pt.utl.ist.notifcenter.domain;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;

public class Mensagem extends Mensagem_Base {

    public Mensagem() {
        super();
    }

    public boolean isAccessible(User user) {
        for (PersistentGroup pg : this.getGruposDestinatariosSet()) {
            if (pg.isMember(user)) {
                return true;
            }
        }
        return false;
    }

    @Atomic
    public static Mensagem createMensagem(final CanalNotificacao canalNotificacao, final PersistentGroup[] gruposDestinatarios, final String assunto, final String textoCurto, final String textoLongo, @Nullable final DateTime dataEntrega, @Nullable final String callbackUrlEstadoEntrega /*, @Nullable final ArrayList<Attachment> attachments*/) {
        Mensagem mensagem = new Mensagem();
        mensagem.setCanalNotificacao(canalNotificacao);

        ///Arrays.stream(gruposDestinatarios).forEach(e -> mensagem.addGruposDestinatarios(e)); //nao testado
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

        /*if (attachments != null) {
            for (Attachment at : attachments) {
                mensagem.addAttachments(at);
            }
        }*/

        if (dataEntrega != null) {
            mensagem.setDataEntrega(dataEntrega);
            //TODO fazer algo para enviar mensagem no futuro
        }
        else {
            mensagem.setDataEntrega(DateTime.now());
        }

        return mensagem;
    }

    //este nao deve ser necessário:
    @Atomic
    public void addAttachment(Attachment at) {
        this.addAttachments(at);
    }

    @Atomic
    public void delete() {
        this.getCanalNotificacao().removeMensagem(this);
        this.setCanalNotificacao(null); ///

        for (PersistentGroup g : this.getGruposDestinatariosSet()) {
            g.removeMensagem(this);
            this.removeGruposDestinatarios(g); ///
        }

        for (Attachment a : this.getAttachmentsSet()) {
            a.delete();
            //this.removeAttachments(a);
        }

        for (EstadoDeEntregaDeMensagemEnviadaAContacto e : this.getEstadoDeEntregaDeMensagemEnviadaAContactoSet()) {
            e.delete();
        }

        this.deleteDomainObject();
    }


}
