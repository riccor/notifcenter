package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Canal.class)
public class CanalAdapter implements JsonAdapter<Canal> {

    //Allows creating this entity via website
    public static Canal create2(JsonElement jsonElement) {
        String channelType = UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "createChannel");
        Class<?> clazz;

        try {
            clazz = Class.forName(NotifcenterSpringConfiguration.getConfiguration().notifcenterDomain() + "." + channelType);

            if (Canal.CHANNELS.get(clazz) == null) {
                throw new Exception("error");
            }

        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_NAME_ERROR);
        }

        JsonObject config = UtilsResource.stringToJson(UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "config"));
        return Canal.createChannel(clazz, config.toString());
    }

    //Allows modifying this entity via website
    public static Canal update2(JsonElement jsonElement, Canal canal) {
        JsonObject config = UtilsResource.stringToJson(UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "config"));
        canal.setConfig(config.toString());
        return canal;
    }

    @Override
    public Canal create(JsonElement jsonElement, JsonBuilder ctx) {
        return create2(jsonElement);
    }

    @Override
    public Canal update(JsonElement jsonElement, Canal canal, JsonBuilder ctx) {
        return update2(jsonElement, canal);
    }

    @Override
    public JsonElement view(Canal obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("type", obj.getClass().getSimpleName());
        jObj.add("DEBUGconfig", obj.getConfigAsJson()); //DEBUG
        return jObj;
    }

}
