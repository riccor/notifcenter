package pt.utl.ist.notifcenter.domain;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;
import pt.ist.fenixframework.Atomic;

import java.util.ArrayList;

public class Mensagem extends Mensagem_Base {
    
    public Mensagem() {
        super();
    }

    @Atomic
    public static Mensagem createMensagem(final CanalNotificacao canalNotificacao, final PersistentGroup[] gruposDestinatarios, final String assunto, final String textoCurto, final String textoLongo, @Nullable final DateTime dataEntrega, @Nullable final String callbackUrlEstadoEntrega, @Nullable final ArrayList<Attachment> attachments) {
        Mensagem mensagem = new Mensagem();
        mensagem.setCanalNotificacao(canalNotificacao);

        ///Arrays.stream(gruposDestinatarios).forEach(e -> mensagem.addGruposDestinatarios(e)); //nao testado
        for (PersistentGroup g : gruposDestinatarios) {
            mensagem.addGruposDestinatarios(g);
        }

        mensagem.setAssunto(assunto);
        mensagem.setTextoCurto(textoCurto);
        mensagem.setTextoLongo(textoLongo);

        if (callbackUrlEstadoEntrega != null)
            mensagem.setCallbackUrlEstadoEntrega(callbackUrlEstadoEntrega);

        if (attachments != null) {
            for (Attachment at : attachments) {
                mensagem.addAttachments(at);
            }
        }

        if (dataEntrega != null) {
            mensagem.setDataEntrega(dataEntrega);
            //fazer algo
        }

        return mensagem;
    }

}
