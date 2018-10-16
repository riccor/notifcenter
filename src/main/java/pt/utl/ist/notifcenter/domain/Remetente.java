package pt.utl.ist.notifcenter.domain;

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
}
