package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Contacto;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Contacto.class)
public class ContactoAdapter implements JsonAdapter<Contacto> {

    @Override
    public Contacto create(JsonElement jsonElement, JsonBuilder ctx) {

        return null;
    }

    @Override
    public Contacto update(JsonElement jsonElement, Contacto contacto, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String dadosContacto = getRequiredValue(jObj, "data");
        return contacto.update(dadosContacto);
    }

    @Override
    public JsonElement view(Contacto obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("utilizador", obj.getUtilizador().getExternalId());
        jObj.addProperty("canal", obj.getCanal().getExternalId());
        jObj.addProperty("dados", obj.getDadosContacto());
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!");
    }

}
