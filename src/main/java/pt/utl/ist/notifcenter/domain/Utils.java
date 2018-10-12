package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.core.domain.User;
import org.springframework.util.CollectionUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Iterator;
import java.util.Set;

public class Utils {

    /* já existe no bennu e mais eficiente (usa ConcurrentHashMap como cache):
    @Atomic
    public static User findUserByName(String name) {
        Set<User> users = FenixFramework.getDomainRoot().getBennu().getUserSet();
        for (User u : users) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }
    */

    //might be useful ...or not:
    public static <E> void removeElementFromSet(java.util.Set<E> set, E element) {

        if (CollectionUtils.isEmpty(set)) {
            Iterator<E> i = set.iterator();
            while (i.hasNext()) {
                E o = i.next();

                if (o.equals(element)) {
                    i.remove();
                    break;
                }
            }
        }
    }

}
