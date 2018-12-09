package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Attachment;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Attachment.class)
public class AttachmentAdapter implements JsonAdapter<Attachment> {

    @Override
    public Attachment create(JsonElement jsonElement, JsonBuilder ctx) {
        return null;
    }

    @Override
    public Attachment update(JsonElement jsonElement, Attachment Attachment, JsonBuilder ctx) {

        return null;
    }

    @Override
    public JsonElement view(Attachment obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("name", obj.getDisplayName());
        jObj.addProperty("filename", obj.getFilename());
        jObj.addProperty("externalId", obj.getExternalId());
        jObj.addProperty("creationDate", obj.getCreationDate().toString());
        jObj.addProperty("contentType", obj.getContentType());
        jObj.addProperty("size", obj.getSize());
        jObj.addProperty("downloadUrl", NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/apiaplicacoes/attachments/" + obj.getExternalId());
        //jObj.addProperty("downloadUrl", FileDownloadServlet.getDownloadUrl(file));
        //jObj.addProperty("contentKey", obj.getContentKey()); //igual a externalId
        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!"); //"HTTP Status 412 - Não foi possível criar a entidade"
    }

}
