package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import pt.ist.fenixframework.Atomic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Attachment extends Attachment_Base {

    public Attachment(Mensagem msg) {
        super();
        this.setMensagem(msg);
    }

    @Override
    public boolean isAccessible(User user) {
        for (PersistentGroup pg : this.getMensagem().getGruposDestinatariosSet()) {
            if (pg.isMember(user)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccessibleByApp(Aplicacao app) {
        if (this.getMensagem().getCanalNotificacao().getRemetente().getAplicacao().equals(app)) {
            return true;
        }
        return false;
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

        //TODO how to delete file from filesystem ???
        ///Files.deleteIfExists("awd");
        /*
        final File existingFile = new File(getFilePath());
        if (!existingFile.exists() || existingFile.delete()) {
            setFileSupport(null);
            deleteDomainObject();
        }*/

        this.setStorage(null);
        this.setFileSupport(null);
        this.deleteDomainObject();
    }

}
