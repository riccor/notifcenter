package pt.utl.ist.notifcenter.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Slot;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.rmi.CORBA.Util;
import java.lang.reflect.Method;
import java.util.Arrays;

@DefaultJsonAdapter(Canal.class)
public class CanalAdapter implements JsonAdapter<Canal> {

    public static Canal create2(JsonElement jsonElement) {
        String channelType = UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), "createChannel");
        Class<?> clazz;
        String[] params;

        try {
            clazz = Class.forName(NotifcenterSpringConfiguration.getConfiguration().notifcenterDomain() + "." + channelType);

            if(!Utils.isClassAChannel(clazz)) {
                throw new Exception("error");
            }

            //AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
            //params = annotation.classFields();
            params = Utils.getDomainClassSlots(clazz);
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_NAME_ERROR);
        }

        Class[] args = new Class[params.length]; //always strings
        Arrays.fill(args, String.class);

        Object[] methodArgs = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            methodArgs[i] = UtilsResource.getRequiredValue(jsonElement.getAsJsonObject(), params[i]);
        }

        try {
            Method m = clazz.getMethod("createChannel", args);
            Canal novoCanal = (Canal) m.invoke(null, methodArgs);
            return novoCanal;
        }
        catch (Exception e) {
            ///e.printStackTrace();
            throw new NotifcenterException(ErrorsAndWarnings.INTERNAL_SERVER_ERROR, "Server could not create a new channel.");
        }
    }

    public static Canal update2(JsonElement jsonElement, Canal canal) {
        Class<?> clazz = canal.getClass();
        String[] params;

        try {
            //AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
            ///params = annotation.classFields();
            params = Utils.getDomainClassSlots(clazz);
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INTERNAL_SERVER_ERROR, "Such class is not identified as a channel.");
        }

        Class[] args = new Class[params.length]; //always strings
        Arrays.fill(args, String.class);

        Object[] methodArgs = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            methodArgs[i] = UtilsResource.getRequiredValueOrReturnNullInstead(jsonElement.getAsJsonObject(), params[i]);
        }

        try {
            Method m = clazz.getMethod("updateChannel", args);
            Canal updatedCanal = (Canal) m.invoke(canal, methodArgs);
            return updatedCanal;
        }
        catch (Exception e) {
            ///e.printStackTrace();
            throw new NotifcenterException(ErrorsAndWarnings.INTERNAL_SERVER_ERROR, "Server could not update channel.");
        }
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
        jObj.addProperty("email", obj.getEmail());
        ///jObj.addProperty("password", obj.getPassword());

        try {
            //AnotacaoCanal annotation = obj.getClass().getAnnotation(AnotacaoCanal.class);
            //for (String str : annotation.classFields()) {
            for (String str : Utils.getDomainClassSlots(obj.getClass())) {
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
