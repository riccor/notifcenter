package pt.utl.ist.notifcenter.domain;

import org.springframework.util.CollectionUtils;

public class Canal extends Canal_Base {

    private SistemaNotificacoes sistema_notificacoes;
    private java.util.Set<Contacto> contactos;
    private java.util.Set<CanalNotificacao> canal_notificacao;

    private String email;
    private String password;


    // Constructors
    public Canal() {
        super();
    }

    public  Canal(String email,
                  String password,
                  SistemaNotificacoes sistema_notificacoes,
                  java.util.Set<Contacto> contactos,
                  java.util.Set<CanalNotificacao> canal_notificacao) {
        super();

        this.email = email;
        this.password = password;
        this.sistema_notificacoes = sistema_notificacoes;
        this.contactos = contactos;
        this.canal_notificacao = canal_notificacao;
    }
    

    // Getters and Setters

    @Override
    public java.lang.String getEmail() {
        return this.email;
    }

    @Override
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    @Override
    public java.lang.String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    // Role Methods

    @Override
    public pt.utl.ist.notifcenter.domain.SistemaNotificacoes getSistema_notificacoes() {
        return this.sistema_notificacoes;
    }

    @Override
    public void setSistema_notificacoes(pt.utl.ist.notifcenter.domain.SistemaNotificacoes sistema_notificacoes) {
        this.sistema_notificacoes = sistema_notificacoes;
    }

    @Override
    public void addContacto(pt.utl.ist.notifcenter.domain.Contacto contacto) {

        if (CollectionUtils.isEmpty(this.contactos)) {
            this.contactos = new java.util.HashSet<>();
        }
        this.contactos.add(contacto);
    }

    @Override
    public void removeContacto(pt.utl.ist.notifcenter.domain.Contacto contacto) {
        Utils.removeElementFromSet(this.contactos, contacto);
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.Contacto> getContactoSet() {
        return this.contactos;
    }

    @Override
    public void addCanal_notificacao(pt.utl.ist.notifcenter.domain.CanalNotificacao canal_notificacao) {

        if (CollectionUtils.isEmpty(this.canal_notificacao)) {
            this.canal_notificacao = new java.util.HashSet<>();
        }
        this.canal_notificacao.add(canal_notificacao);
    }

    @Override
    public void removeCanal_notificacao(pt.utl.ist.notifcenter.domain.CanalNotificacao canal_notificacao) {
        Utils.removeElementFromSet(this.canal_notificacao, canal_notificacao);
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.CanalNotificacao> getCanal_notificacaoSet() {
        return this.canal_notificacao;
    }
}
