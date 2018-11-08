package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.joda.time.DateTime;

import java.util.stream.Stream;

@DefaultJsonAdapter(User.class)
public class UserAdapter implements JsonAdapter<User> {

    @Override
    public User create(JsonElement jsonElement, JsonBuilder ctx) {

        return null;
    }

    @Override
    public User update(JsonElement jsonElement, User User, JsonBuilder ctx) {

        return null;
    }

    @Override
    public JsonElement view(User obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("utilizador", obj.getName());
        jObj.addProperty("id", obj.getExternalId());
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

}
