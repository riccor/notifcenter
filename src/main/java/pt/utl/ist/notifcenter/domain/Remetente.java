package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import pt.ist.fenixframework.Atomic;

public class Remetente extends Remetente_Base {

    private Remetente(Aplicacao app) {
        super();
        this.setAplicacao(app);
    }

    @Atomic
    public static Remetente createRemetente(Aplicacao app, final String nameRemetente) {
        Remetente remetente = new Remetente(app);
        remetente.setNome(nameRemetente);
        return remetente;
    }

    @Atomic
    public Remetente update(final String nome) {
        this.setNome(nome);
        return this;
    }

    @Atomic
    public void addGroupToSendMesssages(PersistentGroup group) {
        this.addGrupos(group);
    }

    @Atomic
    public void removeGroupToSendMesssages(PersistentGroup group) {
        this.removeGrupos(group);
    }

    @Atomic
    public void delete() {
        for (PersistentGroup pg : this.getGruposSet()) {
            pg.removeRemetente(this);
            this.removeGrupos(pg); ///
        }

        for (CanalNotificacao cn : this.getCanaisNotificacaoSet()) {
            cn.delete();
        }

        this.getAplicacao().removeRemetentes(this);
        this.setAplicacao(null); ///

        this.deleteDomainObject();
    }

}
