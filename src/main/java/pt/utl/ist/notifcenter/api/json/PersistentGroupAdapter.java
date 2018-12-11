package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.json.JsonAdapter;

import org.fenixedu.bennu.core.domain.groups.PersistentGroup;

import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Attachment;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import java.util.stream.Collectors;

@DefaultJsonAdapter(PersistentGroup.class)
public class PersistentGroupAdapter implements JsonAdapter<PersistentGroup> {

    @Override
    public PersistentGroup create(JsonElement jsonElement, JsonBuilder ctx) {

        return null;
    }

    @Override
    public PersistentGroup update(JsonElement jsonElement, PersistentGroup Group, JsonBuilder ctx) {

        return null;
    }

    @Override
    public JsonElement view(PersistentGroup obj, JsonBuilder ctx) {

        JsonArray jArrayMembers = new JsonArray();
        obj.getMembers().forEach(m -> jArrayMembers.add(m.getExternalId()));

        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("name", obj.getPresentationName());
        //jObj.addProperty("membros", obj.getMembers().map(User::getName).collect(Collectors.joining(",")));
        jObj.add("members", jArrayMembers);

        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!");
    }

}
