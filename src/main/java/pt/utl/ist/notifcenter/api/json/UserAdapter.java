package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;

@DefaultJsonAdapter(User.class)
public class UserAdapter implements JsonAdapter<User> {

    @Override
    public User create(JsonElement jsonElement, JsonBuilder ctx) {
        return null;
    }

    @Override
    public User update(JsonElement jsonElement, User user, JsonBuilder ctx) {
        /*UserProfile up = user.getProfile();
        up.setEmail("awd");
        user.setProfile(up);*/
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


}
