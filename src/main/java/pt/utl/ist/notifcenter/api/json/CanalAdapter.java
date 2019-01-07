package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.utils.Utils;
import pt.utl.ist.notifcenter.api.CanalResource;

@DefaultJsonAdapter(Canal.class)
public class CanalAdapter implements JsonAdapter<Canal> {

    @Override
    public Canal create(JsonElement jsonElement, JsonBuilder ctx) {
        return CanalResource.create2(jsonElement);
    }

    @Override
    public Canal update(JsonElement jsonElement, Canal canal, JsonBuilder ctx) {
        return CanalResource.update2(jsonElement, canal);
    }

    @Override
    public JsonElement view(Canal obj, JsonBuilder ctx) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("id", obj.getExternalId());
        jObj.addProperty("type", obj.getClass().getSimpleName());
        jObj.addProperty("email", obj.getEmail());
        ///jObj.addProperty("password", obj.getPassword());

        try {
            AnotacaoCanal annotation = obj.getClass().getAnnotation(AnotacaoCanal.class);

            for (String str : annotation.classFields()) {
                String methodName = "get" + Utils.capitalizeFirstLetter(str);
                String value = (String) obj.getClass().getMethod(methodName).invoke(obj); //always strings
                jObj.addProperty(str, value);
            }
        }
        catch (Exception e) {
            System.out.println("error on getting a channel class param");
        }

        return jObj;
    }


}
