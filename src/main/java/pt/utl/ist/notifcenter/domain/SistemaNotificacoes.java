package pt.utl.ist.notifcenter.domain;

import org.springframework.util.CollectionUtils;

public class SistemaNotificacoes extends SistemaNotificacoes_Base {
    
    private java.util.Set<Aplicacao> aplicacoes;
    private java.util.Set<Canal> canais;
    //private java.util.Set<org.fenixedu.bennu.core.domain.User> utilizadores;


    // Constructors
    public SistemaNotificacoes() {
        super();
    }
    
    public SistemaNotificacoes(java.util.Set<Aplicacao> aplicacoes,
                               java.util.Set<Canal> canais/*,
                               java.util.Set<org.fenixedu.bennu.core.domain.User> utilizadores*/) {
        super();

        this.setAplicacoesSet(aplicacoes);
        this.setCanaisSet(canais);
        //setUtilizadoresSet(utilizadores);
    }


    // Getters and Setters

    // Role Methods

    @Override
    public void addCanais(pt.utl.ist.notifcenter.domain.Canal canais) {

        if (CollectionUtils.isEmpty(this.canais)) {
            this.canais = new java.util.HashSet<>();
        }

        this.canais.add(canais);
    }

    @Override
    public void removeCanais(pt.utl.ist.notifcenter.domain.Canal canais) {
        Utils.removeElementFromSet(this.canais, canais);
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.Canal> getCanaisSet() {
        return this.canais;
    }

    public void setCanaisSet(java.util.Set<pt.utl.ist.notifcenter.domain.Canal> canaisSet) {
        this.canais = canaisSet;
    }

    @Override
    public void addAplicacoes(pt.utl.ist.notifcenter.domain.Aplicacao aplicacoes) {

        if (CollectionUtils.isEmpty(this.aplicacoes)) {
            this.aplicacoes = new java.util.HashSet<>();
        }

        this.aplicacoes.add(aplicacoes);
    }

    @Override
    public void removeAplicacoes(pt.utl.ist.notifcenter.domain.Aplicacao aplicacoes) {
        Utils.removeElementFromSet(this.aplicacoes, aplicacoes);
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.Aplicacao> getAplicacoesSet() {
        return this.aplicacoes;
    }

    public void setAplicacoesSet(java.util.Set<pt.utl.ist.notifcenter.domain.Aplicacao> aplicacoes) {
        this.aplicacoes = aplicacoes;
    }

}
