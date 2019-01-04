package pt.utl.ist.notifcenter.api;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import java.util.ArrayList;
import java.util.List;

public class UtilsResource {

    public static User getAuthenticatedUser() {
        return Authenticate.getUser();
    }

    public static boolean isUserLoggedIn() {
        return Authenticate.isLogged();
    }

    public static void checkIsUserValid(User user) {
        if (user == null || !FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }
    }

    public static void checkAdminPermissions(User user) {

        DynamicGroup g = Group.managers();

        //debug
        //g.getMembers().forEach(e -> System.out.println("admin member: " + e.getUsername()));

        if (!g.isMember(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_PAGE_ERROR, "You are not a system admin.");
        }
    }

    public static <T> T getDomainObject(Class<T> clazz, String id) {
        try {
            DomainObject dObj = FenixFramework.getDomainObject(id);
            T t = (T) dObj;
            return t;
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Invalid parameter " + clazz.getSimpleName() + " id " + id + " !");
        }
    }

    public static <T> List<T> getDomainObjectsArray(Class<T> clazz, String[] id) {
        ArrayList<T> al = new ArrayList<>();

        for (String i : id) {
            try {
                DomainObject dObj = FenixFramework.getDomainObject(i);
                T t = (T) dObj;
                al.add(t);
            }
            catch (Exception e) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Invalid parameter " + clazz.getSimpleName() + " id " + i + " !");
            }
        }

        return al;
    }

}
