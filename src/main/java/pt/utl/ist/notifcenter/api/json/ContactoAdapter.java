package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.Contacto;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Contacto.class)
public class ContactoAdapter implements JsonAdapter<Contacto> {

    //Allows creating this entity via website
    public static Contacto create2(JsonElement jsonElement) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        User utilizador = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, User.class, "utilizador");
        Canal canal = UtilsResource.getDomainObjectFromJsonProperty(jsonElement, Canal.class, "channel");
        String dadosContacto = UtilsResource.getRequiredValue(jObj, "data");

        for (Contacto c : utilizador.getContactosSet()) {
            if (c.getCanal().equals(canal) /*&& c.getDadosContacto().equals(dadosContacto)*/) {
                String a = "Contact data " + c.getDadosContacto() + " already exists for channel " + canal.getExternalId() + " and user " + utilizador.getExternalId() + "!";
                //debug
                //System.out.println(a);
                throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_CONTACT_ERROR, a);
            }
        }

        return Contacto.createContacto(utilizador, canal, dadosContacto);
    }

    //Allows modifying this entity via website
    public static Contacto update2(JsonElement jsonElement, Contacto contacto) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String dadosContacto = UtilsResource.getRequiredValue(jObj, "data");
        return contacto.update(dadosContacto);
    }

    @Override
    public Contacto create(JsonElement jsonElement, JsonBuilder ctx) {
        return create2(jsonElement);
    }

    @Override
    public Contacto update(JsonElement jsonElement, Contacto contacto, JsonBuilder ctx) {
        return update2(jsonElement, contacto);
    }

    @Override
    public JsonElement view(Contacto obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("user", obj.getUtilizador().getExternalId());
        jObj.addProperty("channel", obj.getCanal().getExternalId());
        jObj.addProperty("data", obj.getDadosContacto());
        return jObj;
    }

}
