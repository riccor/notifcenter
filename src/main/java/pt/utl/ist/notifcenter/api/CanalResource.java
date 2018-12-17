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
import org.springframework.web.bind.annotation.*;
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
    public JsonElement addCanal(@RequestParam("name") String name, @RequestBody JsonElement body) {

        //TwilioWhatsapp.createTwilioWhatsApp(accountSID, authToken, fromPhoneNumber, uri);
        JsonObject jObj = new JsonObject();
        Class<?> clazz;
        String[] params;

        try {
            clazz = Class.forName(NotifcenterSpringConfiguration.getConfiguration().notifcenterDomain() + "." + name);
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
            methodArgs[i] = getRequiredValue(body.getAsJsonObject(), params[i]);
        }

        try {
            Method m = clazz.getMethod("createChannel", args);
            Canal novoCanal = (Canal) m.invoke(null, methodArgs);
            return view(novoCanal, CanalAdapter.class);
        }
        catch (Exception e) {
            ///e.printStackTrace();
            throw new NotifcenterException(ErrorsAndWarnings.INTERNAL_SERVER_ERROR, "Server could not create a new channel.");
        }
    }

    @RequestMapping(value = "/listclassescanais", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listClassesCanais() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(AnotacaoCanal.class));

        for (BeanDefinition bd : scanner.findCandidateComponents(NotifcenterSpringConfiguration.getConfiguration().notifcenterDomain())) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
                String name = clazz.getSimpleName(); //bd.getBeanClassName().substring(bd.getBeanClassName().lastIndexOf('.') + 1);
                String[] params = annotation.classFields();

                JsonObject jO = new JsonObject();
                JsonArray jA = new JsonArray();

                for (String s : params) {
                    jA.add(s);
                }

                jO.addProperty("name", name);
                jO.add("params", jA);

                jArray.add(jO);

            }
            catch (Exception e) {
                System.out.println("error on getting a channel class params");
            }
        }

        jObj.add("classes_canais", jArray);
        return jObj;
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

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            jArray.add(view(c, CanalAdapter.class));
        }

        jObj.add("canais", jArray);
        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{canal}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteAplicacao(@PathVariable(value = "canal") Canal canal) {

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
    public JsonElement updateAplicacao2(@PathVariable("canal") Canal canal, @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        return view(update(body, canal, CanalAdapter.class), CanalAdapter.class);
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.MISSING_PARAMETER_ERROR, "Missing parameter " + property + "!");
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

}
