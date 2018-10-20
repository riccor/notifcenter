package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Aplicacao;

@DefaultJsonAdapter(Aplicacao.class)
public class AplicacaoAdapter implements JsonAdapter<Aplicacao> {

    @Override
    public Aplicacao create(JsonElement jsonElement, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String name = getRequiredValue(jObj, "name");
        String redirectUrl = getRequiredValue(jObj, "redirect_uri");
        String description = getRequiredValue(jObj, "description");
        String authorName = getRequiredValue(jObj, "author");
        String siteUrl = getRequiredValue(jObj, "site_url");
        return Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
    }

    @Override
    public Aplicacao update(JsonElement jsonElement, Aplicacao app, JsonBuilder ctx) {
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String nome = getRequiredValue(jObj, "name");
        ///outros parametros (será que ctx pode ajudar?)
        return app.updateAplicacaoName(nome);
    }

    @Override
    public JsonElement view(Aplicacao obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("client_id", obj.getExternalId());
        jObj.addProperty("name", obj.getName());
        jObj.addProperty("author", obj.getAuthorName());
        jObj.addProperty("permissoes", obj.getPermissoesAplicacao().name());
        jObj.addProperty("description", obj.getDescription());
        jObj.addProperty("site_url", obj.getSiteUrl());
        jObj.addProperty("redirect_uri", obj.getRedirectUrl());
        jObj.addProperty("client_secret", obj.getSecret());
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw BennuCoreDomainException.cannotCreateEntity();
    }

}

/*
    ///not needed: app.setPermissoesAplicacao(getRequiredValue_AppPermissions(jObj));
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

}*/
