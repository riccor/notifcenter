package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;
import org.springframework.util.MultiValueMap;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import java.util.*;

public class UtilsResource {

    public static String getRequiredValueOrReturnNullInsteadRecursive(JsonObject obj, String property) {

        String toReturn = getRequiredValueOrReturnNullInsteadSpecial(obj, property);

        if (toReturn == null) {
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {

                //System.out.println("key: " + entry.getKey() + " | value: " + entry.getValue().toString());

                if (entry.getValue() instanceof JsonObject) {
                    toReturn = getRequiredValueOrReturnNullInsteadRecursive(entry.getValue().getAsJsonObject(), property);
                }

                if (toReturn != null) {
                    break;
                }
            }
        }

        return toReturn;
    }

    public static JsonObject stringToJson(String messageJson) {
        try {
            JsonParser parser = new JsonParser();
            return parser.parse(messageJson).getAsJsonObject();
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_JSON_ERROR, "Bad parameter json: " + messageJson);
        }
    }

    public static String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).getAsString().isEmpty()) {
                return obj.get(property).getAsString();
            }
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!");
    }

    //Using toString() instead of getAsString()
    public static String getRequiredValueOrReturnNullInsteadSpecial(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).toString().isEmpty()) {
                return obj.get(property).toString().replace("\"", ""); //remove quotes
            }
        }
        return null;
    }

    public static String getRequiredValueOrReturnNullInstead(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).getAsString().isEmpty()) {
                return obj.get(property).getAsString();
            }
        }
        return null;
    }

    public static void deletePropertyFromJsonObject(JsonObject obj, String property) {
        while (obj.has(property)) {
            obj.remove(property);
        }
    }

    public static String getRequiredValueFromMultiValueMap(MultiValueMap<String, String> map, String key) {
        List<String> value = map.get(key);
        if (value != null) {
            if (!value.get(0).isEmpty()) { //here we only return the first parameter found
                return value.get(0);
            }
        }
        throw new NotifcenterException(ErrorsAndWarnings.MISSING_PARAMETER_ERROR, "Missing parameter " + key + "!");
    }

    public static String getRequiredValueFromMultiValueMapOrReturnNullInstead(MultiValueMap<String, String> map, String key) {
        List<String> value = map.get(key);
        if (value != null) {
            if (!value.get(0).isEmpty()) { //here we only return the first parameter found
                return value.get(0);
            }
        }
        return null;
    }

    public static String[] getRequiredArrayValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            //Gson googleJson = new Gson();
            //ArrayList<String> arrayList = googleJson.fromJson(obj.get(property).getAsJsonArray(), ArrayList.class);
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator<JsonElement> i = obj.get(property).getAsJsonArray().iterator();

            while (i.hasNext()) {
                arrayList.add(i.next().getAsString());
            }

            return arrayList.toArray(new String[0]);
        }
        throw new NotifcenterException(ErrorsAndWarnings.MISSING_PARAMETER_ERROR, "Missing parameter " + property + "!");
    }


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

            if (!FenixFramework.isDomainObjectValid((DomainObject) t)) {
                throw new Exception("error");
            }

            if (!clazz.isInstance(t)) {
                throw new Exception("error");
            }

            return t;
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Invalid parameter " + clazz.getSimpleName() + " id " + id + " !");
        }
    }

    public static <T> T getDomainObjectFromJsonProperty(JsonElement jsonElement, Class<T> clazz, String property) {
        String id = getRequiredValue(jsonElement.getAsJsonObject(), property);
        T dObj = getDomainObject(clazz, id);
        return dObj;
    }

    public static DateTime getDatetime(String dt) {
        try {
            DateTime date = DateTime.parse(dt, org.joda.time.format.DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss.SSS"));
            return date;
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_DATETIME_ERROR, "Invalid datetime " + dt + " !");
        }
    }

    public static <T> List<T> getDomainObjectsArray(Class<T> clazz, String[] id) {
        ArrayList<T> al = new ArrayList<>();

        for (String i : id) {
            try {
                DomainObject dObj = FenixFramework.getDomainObject(i);
                T t = (T) dObj;

                if (!FenixFramework.isDomainObjectValid((DomainObject) t)) {
                    throw new Exception("error");
                }

                if (!clazz.isInstance(t)) {
                    throw new Exception("error");
                }

                al.add(t);
            }
            catch (Exception e) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Invalid parameter " + clazz.getSimpleName() + " id " + i + " !");
            }
        }

        return al;
    }

}
