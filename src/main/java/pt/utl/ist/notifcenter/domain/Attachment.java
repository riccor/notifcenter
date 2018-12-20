package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Attachment extends Attachment_Base {

    public Attachment(Mensagem msg) {
        super();
        //this.setMensagem(null); //criado antes de mensagem existir.
        this.setMensagem(msg); //TODO poderá ser necessário criar mensagem antes.
    }

    @Override
    public boolean isAccessible(User user) {
        return true;
    }

    @Atomic
    public static Attachment createAttachment(Mensagem msg, String displayName, String filename, byte[] content) {
        Attachment egf = new Attachment(msg);
        egf.init(displayName, filename, content);
        return egf;
    }

    @Atomic
    public static Attachment createAttachment(Mensagem msg, String displayName, String filename, File file) throws IOException {
        Attachment egf = new Attachment(msg);
        egf.init(displayName, filename, file);
        return egf;
    }

    @Atomic
    public static Attachment createAttachment(Mensagem msg, String displayName, String filename, InputStream file) throws IOException {
        Attachment egf = new Attachment(msg);
        egf.init(displayName, filename, file);
        return egf;
    }

    @Atomic
    public void delete() {
        this.setMensagem(null);
        this.setStorage(null);
        this.deleteDomainObject();
    }

}
