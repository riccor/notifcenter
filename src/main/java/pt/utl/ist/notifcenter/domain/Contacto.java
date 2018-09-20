package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
//import pt.ist.fenixframework.dml.runtime.DirectRelation;

public class Contacto extends Contacto_Base {

    private org.fenixedu.bennu.core.domain.User utilizador;
    private java.lang.String dados_contacto;
    private Canal canal;

    public Contacto(org.fenixedu.bennu.core.domain.User utilizador, java.lang.String dados_contacto, Canal canal) {
        super();

        //DirectRelation<User, Contacto> relationUtilizadorTemContactos = getRelationUtilizadorTemContactos();
        /// getUtilizador()        SERÁ QUE NAO PRECISAMOS de criar field 'utilizador'?!

        setUtilizador(utilizador);
        setDados_contacto(dados_contacto);
        setCanal(canal);
    }

    @Override
    public void setUtilizador(org.fenixedu.bennu.core.domain.User utilizador) {
        this.utilizador = utilizador;
    }

    @Override
    public void setDados_contacto(java.lang.String dados_contacto) {
        this.dados_contacto = dados_contacto;
    }

    @Override
    public void setCanal(pt.utl.ist.notifcenter.domain.Canal canal) {
        this.canal = canal;
    }
}
