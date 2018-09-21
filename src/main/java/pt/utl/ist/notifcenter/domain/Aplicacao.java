package pt.utl.ist.notifcenter.domain;

import org.springframework.util.CollectionUtils;

public class Aplicacao extends Aplicacao_Base {

    private SistemaNotificacoes sistema_notificacoes;
    private java.util.Set<Remetente> remetentes;

    private AppPermissions permissoesAplicacao;
    //private String responsavel;
    //private String parametroAutenticacao1, parametroAutenticacao2; //a classe do Fenix já o faz com Oauth


    // Constructors
    public Aplicacao() {
        super();
    }

    public Aplicacao(SistemaNotificacoes sistema_notificacoes,
                     java.util.Set<Remetente> remetentes,
                     AppPermissions permissoesAplicacao) {
        super();

        this.sistema_notificacoes = sistema_notificacoes;
        this.remetentes = remetentes;
        this.permissoesAplicacao = permissoesAplicacao;
    }


    // Getters and Setters

    @Override
    public AppPermissions getPermissoesAplicacao() {
        return this.permissoesAplicacao;
    }

    @Override
    public void setPermissoesAplicacao(AppPermissions permissoesAplicacao) {
        this.permissoesAplicacao = permissoesAplicacao;
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
    public void addRemetentes(pt.utl.ist.notifcenter.domain.Remetente remetentes) {

        if (CollectionUtils.isEmpty(this.remetentes)) {
            this.remetentes = new java.util.HashSet<>();
        }
        this.remetentes.add(remetentes);
    }

    @Override
    public void removeRemetentes(pt.utl.ist.notifcenter.domain.Remetente remetentes) {

        Utils.removeElementFromSet(this.remetentes, remetentes);
    }

    @Override
    public java.util.Set<pt.utl.ist.notifcenter.domain.Remetente> getRemetentesSet() {
        return this.remetentes;
    }

}
