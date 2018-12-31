package pt.utl.ist.notifcenter.domain;

//import org.springframework.http.ResponseEntity;
import pt.ist.fenixframework.Atomic;

import java.util.HashMap;

public abstract class Canal extends Canal_Base {

    public Canal() {
        super();
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    ///
    /*
    @Atomic
    public static Canal createCanal(final String email, final String password) {
        Canal canal = new Canal();
        canal.setEmail(email);
        canal.setPassword(password);
        return canal;
    }
    */

    /*
    public ResponseEntity<String> sendMessage(final String to, final String message) {
        return null;
    }
    */

    public abstract void sendMessage(Mensagem msg); //{ System.out.println("\n\nshould not see this"); }

    //used to print information about an object of a channel class on a html page
    //criei isto porque não consigo aceder a metodos através dos respectivos nomes em JSTL
    public abstract HashMap<String, String> getParams();

    @Atomic
    public void delete() {
        for (CanalNotificacao cn : this.getCanalNotificacaoSet()) {
            cn.delete();
        }

        for (Contacto c : this.getContactoSet()) {
            c.delete();
        }

        for (EstadoDeEntregaDeMensagemEnviadaAContacto e : this.getEstadoDeEntregaDeMensagemEnviadaAContactoSet()) {
            e.delete();
        }

        this.getSistemaNotificacoes().removeCanais(this);
        this.setSistemaNotificacoes(null);

        this.deleteDomainObject();
    }

}
