package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Attachment extends Attachment_Base {

    public Attachment() {
        super();
    }

    @Override
    public boolean isAccessible(User user) {
        return true;
    }

    @Atomic
    public static Attachment createAttachment(String displayName, String filename, byte[] content) {
        Attachment egf = new Attachment();
        egf.init(displayName, filename, content);
        return egf;
    }

    @Atomic
    public static Attachment createAttachment(String displayName, String filename, File file) throws IOException {
        Attachment egf = new Attachment();
        egf.init(displayName, filename, file);
        return egf;
    }

    @Atomic
    public static Attachment createAttachment(String displayName, String filename, InputStream file) throws IOException {
        Attachment egf = new Attachment();
        egf.init(displayName, filename, file);
        return egf;
    }


}
