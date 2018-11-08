package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Canal;

@DefaultJsonAdapter(Canal.class)
public class CanalAdapter implements JsonAdapter<Canal> {

    @Override
    public Canal create(JsonElement jsonElement, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String email = getRequiredValue(jObj, "email");
        String password = getRequiredValue(jObj, "password");
        return Canal.createCanal(email, password);
    }

    @Override
    public Canal update(JsonElement jsonElement, Canal Canal, JsonBuilder ctx) {

        return null;
    }

    @Override
    public JsonElement view(Canal obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("type", obj.getClass().getSimpleName());
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("email", obj.getEmail());
        jObj.addProperty("password", obj.getPassword());
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

}
