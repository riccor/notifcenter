package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class Canal extends Canal_Base {

    public Canal() {
        super();
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static Canal createCanal(final String email, final String password) {
        Canal canal = new Canal();
        canal.setEmail(email);
        canal.setPassword(password);
        return canal;
    }


}
