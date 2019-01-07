package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;

import org.fenixedu.bennu.core.domain.groups.PersistentGroup;

import org.fenixedu.bennu.core.json.JsonBuilder;

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
        jObj.add("members", jArrayMembers);

        return jObj;
    }


}
