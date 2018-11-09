package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.io.domain.GenericFile;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ExtendGenericFile extends GenericFile {

    public ExtendGenericFile() {
        super();
        this.setStorage(FenixFramework.getDomainRoot().getBennu().getFileSupport().getDefaultStorage());
    }

    @Override
    public boolean isAccessible(User user) {
        return true;
    }

    @Atomic
    public static ExtendGenericFile createExtendGenericFile(String displayName, String filename, byte[] content) {
        ExtendGenericFile egf = new ExtendGenericFile();
        egf.init(displayName, filename, content);
        return egf;
    }

}
