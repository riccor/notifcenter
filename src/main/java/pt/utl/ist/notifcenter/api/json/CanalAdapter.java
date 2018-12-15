package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@DefaultJsonAdapter(Canal.class)
public class CanalAdapter implements JsonAdapter<Canal> {

    @Override
    public Canal create(JsonElement jsonElement, JsonBuilder ctx) {

        /*
        final JsonObject jObj = jsonElement.getAsJsonObject();
        String email = getRequiredValue(jObj, "email");
        String password = getRequiredValue(jObj, "password");
        return Canal.createCanal(email, password);
        */

        return null; //Classe Canal é abstrato
    }

    @Override
    public Canal update(JsonElement jsonElement, Canal Canal, JsonBuilder ctx) {

        return null;
    }

    /* ///JsonObject jO = getJsonFromPa(obj.getClass());
    public JsonObject getJsonFromPa(Class<? extends Canal> clazz) {
        AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
        //String name = annotation.name();
        String name = clazz.getSimpleName(); //bd.getBeanClassName().substring(bd.getBeanClassName().lastIndexOf('.') + 1);
        String[] params = annotation.creatingParams();

        JsonObject jO = new JsonObject();
        JsonArray jA = new JsonArray();

        for (String s : params) {
            jA.add(s);
        }

        jO.addProperty("name", name);
        jO.add("params", jA);

        return jO;
    }*/

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
                String methodName = "get" + str.substring(0, 1).toUpperCase() + str.substring(1);
                String value = (String) obj.getClass().getMethod(methodName).invoke(obj); //são sempre strings
                jObj.addProperty(str, value);
            }
        }
        catch (Exception e) {
            System.out.println("error on getting a channel class params");
        }

        return jObj;
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!"); //"HTTP Status 412 - Não foi possível criar a entidade"
    }

}

/*
    private <T> T castToSpecificChannel(Class<T> clazz, Canal canal) {
        return (T) canal;
    }
*/