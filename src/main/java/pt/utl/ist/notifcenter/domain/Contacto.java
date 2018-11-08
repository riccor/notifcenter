package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

public class Contacto extends Contacto_Base {

    private Contacto() {
        super();
    }

    //3.1.8
    @Atomic
    public static Contacto createContacto(final User utilizador, final String dadosContacto) {
        Contacto contacto = new Contacto();
        contacto.setUtilizador(utilizador);
        contacto.setDadosContacto(dadosContacto);
        return contacto;
    }

    //3.1.8
    @Atomic
    public Contacto setCanalDoContacto(final Canal canal) {
        this.setCanal(canal);
        return this;
    }

    //3.1.7
    @Atomic
    public static Contacto createContacto(final User utilizador, final Canal canal, final String dadosContacto) {
        Contacto contacto = new Contacto();
        contacto.setUtilizador(utilizador);
        contacto.setCanal(canal);
        contacto.setDadosContacto(dadosContacto);
        return contacto;
    }


}
