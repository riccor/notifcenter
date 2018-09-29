package pt.utl.ist.notifcenter.api.json.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.oauth.domain.ExternalApplication;
import org.fenixedu.bennu.oauth.domain.ExternalApplicationScope;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.domain.Aplicacao;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@DefaultJsonAdapter(Aplicacao.class)
public class AplicacaoAdapter implements JsonAdapter<ExternalApplication> {

    @Override
    public JsonElement view(Aplicacao obj, JsonBuilder ctx) {
        JsonObject json = new JsonObject();
        json.addProperty("id", obj.getExternalId());
        json.addProperty("permissoes", obj.getPermissoesAplicacao());
        return json;
    }

    @Override
    protected Aplicacao create(JsonElement json, JsonBuilder ctx) {
        ExternalApplication app = new ExternalApplication();
        app.setAuthor(Authenticate.getUser());
        return app;
    }

    @Override
    public Aplicacao update(JsonElement jsonElement, Aplicacao app, JsonBuilder ctx) {
        final JsonObject json = jsonElement.getAsJsonObject();
        String name = json.get("name");

        String name = json.get("title");
        String isbn = json.get("isbn");
        return book.update(title, isbn);
        app.setName(getRequiredValue(jObj, "name"));
        app.setDescription(getRequiredValue(jObj, "description"));
        app.setRedirectUrl(getRedirectUrl(jObj));
        return app;
    }
}
