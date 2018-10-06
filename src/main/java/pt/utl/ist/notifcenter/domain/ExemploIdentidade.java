package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

public class ExemploIdentidade extends ExemploIdentidade_Base {
    
    public ExemploIdentidade() {
        super();
    }

    @Atomic
    public static ExemploIdentidade createExemploIdentidade(final String nome) {
        ExemploIdentidade ex = new ExemploIdentidade();
        ex.setParam1(nome);
        ex.setInt1(123);
        return ex;
    }

}
