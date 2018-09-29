package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
//import org.fenixedu.bennu.core.security.Authenticate;
import pt.utl.ist.notifcenter.domain.Aplicacao;
import pt.utl.ist.notifcenter.domain.AppPermissions;
//import org.fenixedu.bennu.oauth.domain.ApplicationUserAuthorization;
//import org.fenixedu.bennu.oauth.domain.ExternalApplication;

@DefaultJsonAdapter(AplicacaoAdapter.class)
public class AplicacaoAdapter implements JsonAdapter<Aplicacao> {

    @Override
    public JsonElement view(Aplicacao obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("name", obj.getName());
        jObj.addProperty("permissoes", obj.getPermissoesAplicacao().name());
        return jObj;
    }

    /*protected Aplicacao create(JsonElement jsonElement) {
        Aplicacao app = new Aplicacao();
        //app.setAuthor(Authenticate.getUser());
        return app;
    }*/

    protected String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

    protected AppPermissions getRequiredValue_AppPermissions(JsonObject obj) {
        if (obj.has("permissions")) {
            String permissionsString = obj.get("permissions").getAsString();

            if (permissionsString.equalsIgnoreCase("ALLOW_ALL")) {
                return AppPermissions.ALLOW_ALL;
            }
            else if (permissionsString.equalsIgnoreCase("RREQUIRES_APPROVAL")) {
                return AppPermissions.RREQUIRES_APPROVAL;
            }
            else if (permissionsString.equalsIgnoreCase("NONE")) {
                return AppPermissions.NONE;
            }
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

    @Override
    public Aplicacao create(JsonElement jsonElement, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        //Aplicacao app = create(json);
        Aplicacao app = new Aplicacao();
        app.setName(getRequiredValue(jObj, "name"));
        app.setAuthorName(getRequiredValue(jObj, "authorname"));
        ///app.setPermissoesAplicacao(getRequiredValue_AppPermissions(jObj)); //criar adapter (?)
        return app;
    }

    @Override
    public Aplicacao update(JsonElement jsonElement, Aplicacao app, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        app.setName(getRequiredValue(jObj, "name"));
        //return app.updatePermissions(appPermissions);
        return app;
    }
}

