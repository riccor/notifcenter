package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.io.domain.GenericFile;
import pt.ist.fenixframework.Atomic;

public class ExtendGenericFile extends GenericFile {

    public ExtendGenericFile() {
        super();
    }

    @Override
    public boolean isAccessible(User user) {
        return true;
    }

    @Atomic
    public static ExtendGenericFile createExtendGenericFile() {
        ExtendGenericFile egf = new ExtendGenericFile();
        return egf;
    }

}
