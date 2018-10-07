package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.ExemploIdentidade;

@DefaultJsonAdapter(ExemploIdentidade.class)
public class ExemploIdentidadeAdapter implements JsonAdapter<ExemploIdentidade> {

    protected ExemploIdentidade create(JsonElement json) {
        ExemploIdentidade identi = new ExemploIdentidade();
        return identi;
    }

    @Override
    public ExemploIdentidade create(JsonElement jsonElement, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        ExemploIdentidade identi = create(jsonElement);
        identi.setParam1(getRequiredValue(jObj, "param 1 name"));
        return identi;
    }

    @Override
    public ExemploIdentidade update(JsonElement jsonElement, ExemploIdentidade identi, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        identi.setParam1(getRequiredValue(jObj, "new param 1 update"));
        return identi;
    }

    @Override
    public JsonElement view(ExemploIdentidade obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("name", obj.getParam1());
        return jObj;
    }

    protected String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

}
