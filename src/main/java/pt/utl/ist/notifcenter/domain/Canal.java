package pt.utl.ist.notifcenter.domain;

//import org.springframework.http.ResponseEntity;
import pt.ist.fenixframework.Atomic;

public abstract class Canal extends Canal_Base implements InterfaceDeCanal{

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

    public void sendMessage(Mensagem msg) {
        System.out.print("NAO DEVERIA IMPRIMIR ISTO (SENDMESSAGE em Canal.java");
    }

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
