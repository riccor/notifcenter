package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
//import pt.ist.fenixframework.dml.runtime.DirectRelation;

public class Contacto extends Contacto_Base {

    private org.fenixedu.bennu.core.domain.User utilizador;
    private String dados_contacto;
    private Canal canal;

    // Constructors
    public Contacto(org.fenixedu.bennu.core.domain.User utilizador, java.lang.String dados_contacto, Canal canal) {
        super();

        //DirectRelation<User, Contacto> relationUtilizadorTemContactos = getRelationUtilizadorTemContactos();

        setUtilizador(utilizador);
        setDados_contacto(dados_contacto);
        setCanal(canal);
    }


    // Getters and Setters

    @Override
    public java.lang.String getDados_contacto() {
        return dados_contacto;
    }

    @Override
    public void setDados_contacto(java.lang.String dados_contacto) {
        this.dados_contacto = dados_contacto;
    }


    // Role Methods

    @Override
    public pt.utl.ist.notifcenter.domain.Canal getCanal() {
        return this.canal;
    }

    @Override
    public void setCanal(pt.utl.ist.notifcenter.domain.Canal canal) {
        this.canal = canal;
    }

    @Override
    public org.fenixedu.bennu.core.domain.User getUtilizador() {
        return this.utilizador;
    }

    @Override
    public void setUtilizador(org.fenixedu.bennu.core.domain.User utilizador) {
        this.utilizador = utilizador;
    }


}
