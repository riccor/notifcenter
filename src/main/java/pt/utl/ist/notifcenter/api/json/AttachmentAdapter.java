package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.Attachment;

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
        jObj.addProperty("creationDate", obj.getCreationDate().toString("dd.MM.yyyy HH:mm:ss.SSS"));
        jObj.addProperty("contentType", obj.getContentType());
        jObj.addProperty("size", obj.getSize());
        jObj.addProperty("downloadUrl", NotifcenterSpringConfiguration.getConfiguration().notifcenterUrlForAttachments() + obj.getExternalId());
        //jObj.addProperty("downloadUrl", FileDownloadServlet.getDownloadUrl(file));
        //jObj.addProperty("contentKey", obj.getContentKey()); //igual a externalId
        return jObj;
    }

}
