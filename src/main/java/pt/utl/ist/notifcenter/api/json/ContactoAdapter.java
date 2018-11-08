package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Contacto;

@DefaultJsonAdapter(Contacto.class)
public class ContactoAdapter implements JsonAdapter<Contacto> {

    @Override
    public Contacto create(JsonElement jsonElement, JsonBuilder ctx) {

        return null;
    }

    @Override
    public Contacto update(JsonElement jsonElement, Contacto Contacto, JsonBuilder ctx) {

        return null;
    }

    @Override
    public JsonElement view(Contacto obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("utilizador", obj.getUtilizador().getName());
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("dados", obj.getDadosContacto());
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

}
