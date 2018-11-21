package pt.utl.ist.notifcenter.domain;

import org.springframework.http.ResponseEntity;
import pt.ist.fenixframework.Atomic;

public class Canal extends Canal_Base implements InterfaceDeCanal{

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

    public ResponseEntity<String> sendMessage(final String to, final String message) {
        return null;
    }

    public ResponseEntity<String> sendMessage(Mensagem msg) {
        return null;
    }
}
