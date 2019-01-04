package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.rest.BennuRestResource;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.CanalAdapter;
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
import pt.utl.ist.notifcenter.ui.NotifcenterController;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import java.lang.reflect.Method;
import java.util.Arrays;

@RestController
@RequestMapping("/apicanais")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api.canais")
public class CanalResource extends BennuRestResource {

    //AGRUPAMENTO: Canais

    @SkipCSRF
    @RequestMapping(value = "/addcanal", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addCanal(@RequestBody JsonElement body) {

        //TwilioWhatsapp.createTwilioWhatsApp(accountSID, authToken, fromPhoneNumber, uri);

        return view(create2(body), CanalAdapter.class);
    }

    @RequestMapping(value = "/listclassescanais", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listClassesCanais() {
        JsonObject jObj = new JsonObject();
        jObj.add("classes_canais", getAvailableChannelsNamesAndParamsAsJson());
        return jObj;
    }

    public static JsonElement getAvailableChannelsNamesAndParamsAsJson() {
        MultiValueMap<String, String> list = getAvailableChannelsNamesAndParams();
        JsonArray jArray = new JsonArray();

        list.forEach((k, v) -> {
            //System.out.println("class: " + k);

            JsonArray jA = new JsonArray();
            v.forEach(i -> {
                //System.out.println("param: " + i);
                jA.add(i);
            });

            JsonObject jO = new JsonObject();
            jO.addProperty("channelType", k);
            jO.add("params", jA);

            jArray.add(jO);
        });

        return jArray;
    }


    public static MultiValueMap<String, String> getAvailableChannelsNamesAndParams() {
        MultiValueMap<String, String> list = new LinkedMultiValueMap<>();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(AnotacaoCanal.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(NotifcenterSpringConfiguration.getConfiguration().notifcenterDomain())) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
                String name = clazz.getSimpleName(); //bd.getBeanClassName().substring(bd.getBeanClassName().lastIndexOf('.') + 1);
                String[] params = annotation.classFields();
                list.put(name, Arrays.asList(params));
            } catch (Exception e) {
                System.out.println("error on getting a channel class params");
            }
        }

        return list;
    }

    @RequestMapping(value = "/{canal}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewCanal(@PathVariable(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        return view(canal, CanalAdapter.class);
    }

    @RequestMapping(value = "/listcanais", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listCanais() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Canal c : SistemaNotificacoes.getInstance().getCanaisSet()) {
            jArray.add(view(c, CanalAdapter.class));
        }

        jObj.add("canais", jArray);
        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{canal}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteCanal(@PathVariable(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.add("deleted_canal", view(canal, CanalAdapter.class));

        canal.delete();

        return jObj;
    }

    //vários canais têm diferentes parâmetros -> portanto uso apenas este /update que recebe um objecto json no body do pedido
    @SkipCSRF
    @RequestMapping(value = "/{canal}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateCanal(@PathVariable("canal") Canal canal, @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        return view(update2(body, canal), CanalAdapter.class);
    }

    @ExceptionHandler({NotifcenterException.class})
    public ResponseEntity<JsonElement> errorHandler(NotifcenterException ex) {

        HttpHeaders header = new HttpHeaders();

        if (ex.getMoreDetails() != null) {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJsonWithDetails(ex.getMoreDetails()), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
        else {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJson(), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
    }


    //As seguintes funções não estão no ficheiro CanalAdapter.java porque se estivessem precisaria de adicionar nele a anotação @DefaultJsonAdapter cada vez que se adiciona uma nova classe de canal ao sistema
    public static Canal create2(JsonElement jsonElement /*, JsonBuilder ctx*/) {
        String channelType = getRequiredValue(jsonElement.getAsJsonObject(), "channelType");
        Class<?> clazz;
        String[] params;

        try {
            clazz = Class.forName(NotifcenterSpringConfiguration.getConfiguration().notifcenterDomain() + "." + channelType);
            AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
            params = annotation.classFields();
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_NAME_ERROR);
        }

        Class[] args = new Class[params.length]; //sempre strings
        Arrays.fill(args, String.class);

        Object[] methodArgs = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            methodArgs[i] = getRequiredValue(jsonElement.getAsJsonObject(), params[i]);
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

    public static Canal update2(JsonElement jsonElement, Canal canal /*, JsonBuilder ctx*/) {
        Class<?> clazz = canal.getClass();
        String[] params;

        try {
            AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
            params = annotation.classFields();
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INTERNAL_SERVER_ERROR, "Such class is not identified as a channel.");
        }

        Class[] args = new Class[params.length]; //sempre strings
        Arrays.fill(args, String.class);

        Object[] methodArgs = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            methodArgs[i] = getRequiredValueOrReturnNullInstead(jsonElement.getAsJsonObject(), params[i]);
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

    private static String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).getAsString().isEmpty()) {
                return obj.get(property).getAsString();
            }
        }
        throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Missing parameter " + property + "!"); //"HTTP Status 412 - Não foi possível criar a entidade"
    }

    private static String getRequiredValueOrReturnNullInstead(JsonObject obj, String property) {
        if (obj.has(property)) {
            if (!obj.get(property).getAsString().isEmpty()) {
                return obj.get(property).getAsString();
            }
        }
        return null;
    }

}
