package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

public class Contacto extends Contacto_Base {

    private Contacto() {
        super();
    }

    @Atomic
    public static Contacto createContacto(final User utilizador, final String dadosContacto) {
        Contacto contacto = new Contacto();
        contacto.setUtilizador(utilizador);
        contacto.setDadosContacto(dadosContacto);
        return contacto;
    }

    @Atomic
    public Contacto SetCanal(final Canal canal) {
        this.setCanal(canal);
        return this;
    }

    @Atomic
    public static Contacto createContacto(final User utilizador, final Canal canal) {
        Contacto contacto = new Contacto();
        contacto.setUtilizador(utilizador);
        contacto.setCanal(canal);
        return contacto;
    }

}
