package pt.utl.ist.notifcenter.domain;

import org.joda.time.DateTime;
import java.util.List;

public class Mensagem extends Mensagem_Base {

    private CanalNotificacao canalDeNotificacao;
    private List<org.fenixedu.bennu.core.groups.Group> gruposDestinatarios = null;
    private org.fenixedu.bennu.core.domain.User remetente;
    private String assunto;
    private String texto_curto;
    private String texto_longo;
    private List<org.fenixedu.bennu.io.domain.GenericFile> attachments = null;
    private DateTime data_entrega;
    private String callback_url_estado_entrega;

    // Constructors
    protected  Mensagem_Base() {
        super();
    }

    public Mensagem(CanalNotificacao canalDeNotificacao,
                    List<org.fenixedu.bennu.core.groups.Group> gruposDestinatarios,
                    org.fenixedu.bennu.core.domain.User remetente,
                    String assunto,
                    String texto_curto,
                    String texto_longo,
                    DateTime data_entrega,
                    String callback_url_estado_entrega) {
        super();

        this.canalDeNotificacao = canalDeNotificacao;
        this.gruposDestinatarios = gruposDestinatarios;
        this.remetente = remetente;
        this.assunto = assunto;
        this.texto_curto = texto_curto;
        this.texto_longo = texto_longo;
        this.data_entrega = data_entrega;
        this.callback_url_estado_entrega = callback_url_estado_entrega;
    }


    // Getters and Setters

    @Override
    public java.lang.String getAssunto() {
        return this.assunto;
    }

    @Override
    public void setAssunto(java.lang.String assunto) {
        this.assunto = assunto;
    }

    @Override
    public java.lang.String getTexto_curto() {
        return this.texto_curto;
    }

    @Override
    public void setTexto_curto(java.lang.String texto_curto) {
        this.texto_curto = texto_curto;
    }

    @Override
    public java.lang.String getTexto_longo() {
        return this.texto_longo;
    }

    @Override
    public void setTexto_longo(java.lang.String texto_longo) {
        this.texto_longo = texto_longo;
    }

    @Override
    public org.joda.time.DateTime getData_entrega() {
        return this.data_entrega;
    }

    @Override
    public void setData_entrega(org.joda.time.DateTime data_entrega) {
        this.data_entrega = data_entrega;
    }

    @Override
    public java.lang.String getCallback_url_estado_entrega() {
        return this.callback_url_estado_entrega;
    }

    @Override
    public void setCallback_url_estado_entrega(java.lang.String callback_url_estado_entrega) {
        this.callback_url_estado_entrega = callback_url_estado_entrega;
    }

    // Role Methods

    @Override
    public void addAttachments(org.fenixedu.bennu.io.domain.GenericFile attachments) {

        if (this.attachments == null) {
            this.attachments = new ArrayList<org.fenixedu.bennu.io.domain.GenericFile>();
        }

        this.attachments.add(attachments);
    }

    @Override
    public void removeAttachments(org.fenixedu.bennu.io.domain.GenericFile attachments) {

        if (this.attachments != null) {
            Iterator<org.fenixedu.bennu.io.domain.GenericFile> i = this.attachments.iterator();
            while (i.hasNext()) {
                org.fenixedu.bennu.io.domain.GenericFile o = i.next();

                if (o.equals(attachments)) {
                    i.remove();
                    break;
                }
            }
        }
    }

    @Override
    public java.util.Set<org.fenixedu.bennu.io.domain.GenericFile> getAttachmentsSet() {
        return this.;
    }

    @Override
    public void addGrupos_destinatarios(org.fenixedu.bennu.core.domain.groups.PersistentGroup grupos_destinatarios) {
        this.;
    }

    @Override
    public void removeGrupos_destinatarios(org.fenixedu.bennu.core.domain.groups.PersistentGroup grupos_destinatarios) {
        this.;
    }

    @Override
    public java.util.Set<org.fenixedu.bennu.core.domain.groups.PersistentGroup> getGrupos_destinatariosSet() {
        this.;
    }

    @Override
    public pt.utl.ist.notifcenter.domain.CanalNotificacao getCanal_notificacao() {
        this.;
    }

    @Override
    public void setCanal_notificacao(pt.utl.ist.notifcenter.domain.CanalNotificacao canal_notificacao) {
        this.;
    }

}
