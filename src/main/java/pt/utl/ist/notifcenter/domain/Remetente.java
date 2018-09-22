package pt.utl.ist.notifcenter.domain;

import org.springframework.util.CollectionUtils;

public class Remetente extends Remetente_Base {

    private java.util.Set<org.fenixedu.bennu.core.domain.groups.PersistentGroup> grupos;
    private java.util.Set<CanalNotificacao> canais_notificacao;
    private Aplicacao aplicacao;

    private String nome;

    // Constructors
    public Remetente() {
        super();
    }

    public Remetente(String nome,
                     java.util.Set<org.fenixedu.bennu.core.domain.groups.PersistentGroup> gruposSet,
                     java.util.Set<CanalNotificacao> canais_notificacaoSet,
                     Aplicacao aplicacao) {
        super();

        this.setNome(nome);
        this.setGruposSet(gruposSet);
        this.setCanais_notificacao(canais_notificacaoSet);
        this.setAplicacao(aplicacao);
    }

    // Getters and Setters

    @Override
    public java.lang.String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }

    // Role Methods

    @Override
    public void addGrupos(org.fenixedu.bennu.core.domain.groups.PersistentGroup grupos) {

        if (CollectionUtils.isEmpty(this.grupos)) {
            this.grupos = new java.util.HashSet<>();
        }

        this.grupos.add(grupos);
    }

    @Override
    public void removeGrupos(org.fenixedu.bennu.core.domain.groups.PersistentGroup grupos) {
        Utils.removeElementFromSet(this.grupos, grupos);
    }

    @Override
    public java.util.Set<org.fenixedu.bennu.core.domain.groups.PersistentGroup> getGruposSet() {
        return this.grupos;
    }

    public void setGruposSet(java.util.Set<org.fenixedu.bennu.core.domain.groups.PersistentGroup> gruposSet) {
        this.grupos = gruposSet;
    }

    @Override
    public void addCanais_notificacao(pt.utl.ist.notifcenter.domain.CanalNotificacao canais_notificacao) {

        if (CollectionUtils.isEmpty(this.canais_notificacao)) {
            this.canais_notificacao = new java.util.HashSet<>();
        }

        this.canais_notificacao.add(canais_notificacao);
    }

    @Override
    public void removeCanais_notificacao(pt.utl.ist.notifcenter.domain.CanalNotificacao canais_notificacao) {
        Utils.removeElementFromSet(this.canais_notificacao, canais_notificacao);
    }

    public void setCanais_notificacao(java.util.Set<pt.utl.ist.notifcenter.domain.CanalNotificacao> canais_notificacaoSet) {
        this.canais_notificacao = canais_notificacaoSet;
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.CanalNotificacao> getCanais_notificacaoSet() {
        return canais_notificacao;
    }

    @Override
    public pt.utl.ist.notifcenter.domain.Aplicacao getAplicacao() {
        return this.aplicacao;
    }

    @Override
    public void setAplicacao(pt.utl.ist.notifcenter.domain.Aplicacao aplicacao) {
        this.aplicacao = aplicacao;
    }
}
