package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

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
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("username", obj.getUsername());
        jObj.addProperty("name", obj.getName());
        jObj.addProperty("displayName", obj.getDisplayName());
        jObj.addProperty("email", obj.getEmail());
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!");
    }

}
