package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

public class Contacto extends Contacto_Base {

    private Contacto() {
        super();
    }

    @Atomic
    public static Contacto createContacto(final User utilizador, final Canal canal, final String dadosContacto) {
        Contacto contacto = new Contacto();
        contacto.setUtilizador(utilizador);
        contacto.setCanal(canal);
        contacto.setDadosContacto(dadosContacto);
        return contacto;
    }

    @Atomic
    public Contacto update(final String dadosContacto) {
        this.setDadosContacto(dadosContacto);
        return this;
    }

    @Atomic
    public void delete() {
        this.getUtilizador().removeContactos(this);
        this.setUtilizador(null);

        this.getCanal().removeContacto(this);
        this.setCanal(null);

        this.deleteDomainObject();
    }

}

