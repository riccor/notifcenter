package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.ExemploIdentidade;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(ExemploIdentidade.class)
public class ExemploIdentidadeAdapter implements JsonAdapter<ExemploIdentidade> {

    @Override
    public ExemploIdentidade create(JsonElement jsonElement, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String nome = UtilsResource.getRequiredValue(jObj, "name");
        return ExemploIdentidade.createExemploIdentidade(nome);
    }

    @Override
    public ExemploIdentidade update(JsonElement jsonElement, ExemploIdentidade app, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String param1 = UtilsResource.getRequiredValue(jObj, "param1");
        return app.updateExemploIdentidade(param1);
    }

    @Override
    public JsonElement view(ExemploIdentidade obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("param1", obj.getParam1());
        jObj.addProperty("id", obj.getExternalId());
        return jObj;
    }

}
