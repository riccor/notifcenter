package pt.utl.ist.notifcenter.domain;

//import org.springframework.http.ResponseEntity;
import pt.ist.fenixframework.Atomic;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public abstract class Canal extends Canal_Base {

    public Canal() {
        super();
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    ///
    /*
    @Atomic
    public static Canal createChannel(final String email, final String password) {
        Canal canal = new Canal();
        canal.setEmail(email);
        canal.setPassword(password);
        return canal;
    }
    */

    public abstract void sendMessage(Mensagem msg); //{ System.out.println("\n\nshould not see this"); }

    public abstract void checkIsMessageAdequateForChannel(Mensagem msg);

    public abstract UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request);

    public abstract String getUri();

    @Atomic
    public void delete() {
        for (CanalNotificacao cn : this.getCanalNotificacaoSet()) {
            cn.delete();
        }

        for (Contacto c : this.getContactoSet()) {
            c.delete();
        }

        for (UserMessageDeliveryStatus e : this.getUserMessageDeliveryStatusSet()) {
            e.delete();
        }

        this.getSistemaNotificacoes().removeCanais(this);
        this.setSistemaNotificacoes(null);

        this.deleteDomainObject();
    }

}
