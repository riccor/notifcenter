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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.CanalAdapter;
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.UserMessageDeliveryStatus;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
import pt.utl.ist.notifcenter.ui.NotifcenterController;
import pt.utl.ist.notifcenter.utils.AnotherNotifcenterException;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/apicanais")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api.canais")
public class CanalResource extends BennuRestResource {

    //AGRUPAMENTO: Canais

    @SkipCSRF
    @RequestMapping(value = "/addcanal", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addCanal(@RequestBody JsonElement body) {

        //TwilioWhatsapp.createTwilioWhatsApp(accountSID, authToken, fromPhoneNumber, uri);

        return view(CanalAdapter.create2(body), CanalAdapter.class);
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
            jO.addProperty("createChannel", k);
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
                //AnotacaoCanal annotation = clazz.getAnnotation(AnotacaoCanal.class);
                String name = clazz.getSimpleName(); //bd.getBeanClassName().substring(bd.getBeanClassName().lastIndexOf('.') + 1);
                //String[] params = annotation.classFields();
                String[] params = Utils.getDomainClassSlots(clazz);
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

        return view(CanalAdapter.update2(body, canal), CanalAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{canal}/messagedeliverystatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement messageDeliveryStatus(@PathVariable("canal") Canal canal, HttpServletRequest request) {
        //Received content might not be JSON, so we do not use "@RequestBody JsonElement body"

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        System.out.println("####### got new messagedeliverystatus message!!");
        System.out.println(HTTPClient.getHttpServletRequestParamsAsJson(request).toString());

        UserMessageDeliveryStatus ede = canal.dealWithMessageDeliveryStatusCallback(request);

        if (ede == null) {
            throw new NotifcenterException(ErrorsAndWarnings.UNKNOWN_MESSAGE_ID);
        }
        else {

            //If message parameter callbackUrlEstadoEntrega is not "none", then send message delivery status to the app
            if (!ede.getMensagem().getCallbackUrlEstadoEntrega().equals("none")) {

                MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

                header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                body.put("MessageId", Collections.singletonList(ede.getMensagem().getExternalId()));
                body.put("User", Collections.singletonList(ede.getUtilizador().getUsername())); ///?
                body.put("MessageStatus", Collections.singletonList(ede.getEstadoEntrega()));

                DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                deferredResult.setResultHandler((Object responseEntity) -> {
                    HTTPClient.printResponseEntity((ResponseEntity<String>) responseEntity); ///anything else to do?
                });

                HTTPClient.restASyncClient(HttpMethod.POST, ede.getMensagem().getCallbackUrlEstadoEntrega(), header, body, deferredResult);
            }

            throw new NotifcenterException(ErrorsAndWarnings.SUCCESS_THANKS);
        }
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

    //String returned, not JSON
    @ExceptionHandler({AnotherNotifcenterException.class})
    public ResponseEntity<String> errorHandler2(NotifcenterException ex) {

        HttpHeaders header = new HttpHeaders();

        //For Facebook Messenger to confirm my webhook (https://developers.facebook.com/apps/298908694309495/webhooks/)
        if (ex.getMoreDetails().equalsIgnoreCase("definable")) {
            return new ResponseEntity<>(ex.getMoreDetails(), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
        else {
            return new ResponseEntity<>("error", header, ex.getErrorsAndWarnings().getHttpStatus());
        }
    }

}
