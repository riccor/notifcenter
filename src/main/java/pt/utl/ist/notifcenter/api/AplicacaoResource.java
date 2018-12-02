// Pedidos disponíveis:

//PARA TESTAR:
//curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"sid":"algumsid", "status":"algumstatus"}' http://localhost:8080/notifcenter/apiaplicacoes/281835753963522/messagedeliverystatus
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/pedidocanalnotificacao?canal=281835753963522&remetente=281724084813826
//GET http://localhost:8080/notifcenter/apiaplicacoes/{msg}/deliverystatus
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/sendmessage?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto2&textocurto=aparecenowhatsppcurto2&textolongo=algumtextolongo2
//GET http://localhost:8080/notifcenter/apiaplicacoes/listmessages


//ROBOT
//http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html

//1: https://medium.com/@mayfernandes/season-api-testing-ep-01-testes-de-api-com-o-robot-framework-332b72490096
//keyword driven: https://robotizandotestes.blogspot.com/2017/09/season-tutoriais-ep-03-escrevendo-o.html
//response: https://robotizandotestes.blogspot.com/2017/11/season-api-testing-ep-02-trabalhando.html
//stackoverflow: https://stackoverflow.com/questions/45204684/robot-framework-differences-between-suite-setup-and-test-setup

//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/pedidocanalnotificacao?canal=281835753963522&remetente=281724084813826
//https://www.twilio.com/console/sms/whatsapp/learn
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/sendmessage?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto1&textocurto=aparecenowhatsppcurto&textolongo=algumtextolongo

//DADOS EXEMPLO:
//app "app_77": 281736969715714
//remetente "rem1": 281724084813826
//user "bennu0": 281582350893059
//user "admin": 281582350893057
//grupo "managers": 281702609977345
//canal TwilioWhatsApp: 281835753963522
//pedido de canal de notificacao: 281775624421380 <--- criar novo!

//REGISTAR APP:
//POST http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://localhost:8080/notifcenter/oauth/userdialog?client_id=281736969715714&redirect_uri=http://app77_site.com/code
//POST http://localhost:8080/notifcenter/oauth/access_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&redirect_uri=http://app77_site.com/code&code=d46d6939c0564846fed10cdcb3233b18716f6fb5770d3f58c134379e43316d138471e47d42f1a49160c65c22577180705fc615df027e0017a68f47f4b595a0c3&grant_type=authorization_code
//POST http://localhost:8080/notifcenter/oauth/refresh_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&refresh_token=MjgxNjg1NDMwMTA4MTYzOjdhOWIzNDIyNTRmZDIzN2ZmODQ4N2U2NjFjMjllYWQyODAxYjhhZWMwNDFiZDhiZDU1MDEwZjI5OWNiZmQzOGI3NDQwMGEwZGNhMTAwMjFhYjMyOTYwN2U2NDJkNjMzMWMwZDQ3YmFjYmNkZDk2ZjA3ZGQ3ZmI0NTMyZTg3MzRj&grant_type=refresh_token

//METODOS
//POST http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715714?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/addremetente?name=ric&access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/listremetentes?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET/POST http://localhost:8080/notifcenter/apiaplicacoes/notifcentercallback
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/281582350893057/adddadoscontacto?canal=281835753963522&data=dados1contacto@ex.com
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/pedidocanalnotificacao?canal=281835753963522&remetente=281724084813826
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/sendmessage?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto1&textocurto=aparecenowhatsppcurto&textolongo=algumtextolongo

//UTEIS:
//http://localhost:8080/notifcenter/apiaplicacoes/viewcanal/281835753963522
//http://localhost:8080/notifcenter/apiaplicacoes/listcanais
//http://localhost:8080/notifcenter/apiaplicacoes/listusers
//http://localhost:8080/notifcenter/apiaplicacoes/listapps
//http://localhost:8080/notifcenter/apiaplicacoes/listgroups
//http://localhost:8080/notifcenter/apiaplicacoes/attachments/list
//http://localhost:8080/notifcenter/apiaplicacoes/attachments/{fileName}

//"no" http://localhost:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893059&group=281702609977345
//"yes" http://localhost:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893057&group=281702609977345

//CURL
//curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":100}' http://localhost/api/postJsonReader.do
//curl -H "Content-type: application/json" -X POST -d '{"id":101, "content":"ola1"}' http://localhost:8080/notifcenter/apiaplicacoes/greet
//curl -H "Content-type: application/json" -X POST -d '{"email":"someemail@awd.com", "password":"pass1"}' http://localhost:8080/notifcenter/apiaplicacoes/canal1

//curl -F 'file=@/home/cr/imgg.png' http://localhost:8080/notifcenter/apiaplicacoes/upload


//curl -H "Content-type: application/x-www-form-urlencoded; charset=utf-8" -x POST -d "param1=value1&param2=value2" http://localhost:8080/notifcenter/apiaplicacoes/xyz


package pt.utl.ist.notifcenter.api;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.io.domain.FileStorage;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.io.servlet.FileDownloadServlet;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.joda.time.DateTime;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.*;

import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    /*
    Numa API REST, neste caso o resource é aplicação, e queremos adicionar
    remetentes a essa aplicação. Para mim faria mais sentido ter algo do género:

        //API Aplicacao (/apiaplicacoes):

       /adicionarAplicacao
       /listarAplicacoes
       /{app}
       /{app}/update
       /{app}/remover
       /{app}/adicionarRemetente
       /{app}/listarRemetentes
       /{app}/{remetente}
       /{app}/{remetente}/update
       /{app}/{remetente}/remover
       /{app}/{remetente}/adicionarGrupoDestinatario
       /{app}/{remetente}/listarGruposDestinatarios


       /{app}/pedidoCanalNotificacao
       /{app}/sendmessage


       //API canal (/apicanais):

       /adicionarCanal
       /listarCanais
       /{canal}
       /{canal}/update
       // /{canal}/listarCanaisNotificacao
       // /{canal}/listarContactos


       //API utilizador (/apiutilizadores):

       /listarUtilizadores
       /{utilizador}
       /{utilizador}/adicionarContacto
       /{utilizador}/listarContactos


        O que fazer: orientar a API ao recurso.
    */


    //ADICIONAR APLICACAO

    //ver cd ./notifcenter/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security
    //@SkipAccessTokenValidation //diz ao método preHandler em "NotifcenterInterceptor.java" para aceitar pedidos sem access_token
    @SkipCSRF ///INDIFERENTE USAR ISTO SE USAR O MEU INTERCEPTOR
    @RequestMapping(value = "/oauth/addaplicacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addAplicacao(@RequestParam(value = "description") String description,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "redirect_uri") String redirectUrl,
                               @RequestParam(value = "author", defaultValue = "none") String authorName,
                               @RequestParam(value = "site_url", defaultValue = "none") String siteUrl) {

        if (Aplicacao.findByAplicacaoName(name) != null) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APPNAME_ERROR);
        }

        Aplicacao app = Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
        return view(app, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "/oauth/viewaplicacao/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewAplicacao(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        return view(app, AplicacaoAdapter.class);
    }


    //ADICIONAR DADOS CONTACTO

    @SkipCSRF
    @RequestMapping(value = "/{app}/{user}/adddadoscontacto", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addDadosContactoUtilizador(@PathVariable("app") Aplicacao app,
                                                  @PathVariable("user") User utilizador,
                                                  @RequestParam(value = "data") String dadosContacto,
                                                  @RequestParam(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        //opcional (?)
        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        Contacto contacto = Contacto.createContacto(utilizador, canal, dadosContacto);

        return view(contacto, ContactoAdapter.class);
    }


    // ADICIONAR REMETENTE

    @SkipCSRF
    @RequestMapping(value = "/{app}/addremetente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addRemetente(@PathVariable("app") Aplicacao app, @RequestParam(value = "name") String nomeRemetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);
        return view(remetente, RemetenteAdapter.class);
    }

    @RequestMapping(value = "/{app}/listremetentes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listRemetentes(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Remetente r: app.getRemetentesSet()) {
            jArray.add(view(r, RemetenteAdapter.class));
        }

        jObj.add("remetentes", jArray);

        return jObj;
    }


    //RECEBER NOTIFICACOES DO ESTADO DE ENTREGA DE MENSAGENS POR PARTE DOS CANAIS:

    @SkipCSRF
    @RequestMapping(value = "/{canal}/messagedeliverystatus", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement messageDeliveryStatus(@PathVariable("canal") Canal canal, @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            return ErrorsAndWarnings.INVALID_CHANNEL_ERROR.toJson();
        }

        //TODO - NAO É PRECISO FAZER ESTA DISTINCAO, POIS ESTE RECURSO ESTARÀ DENTRO DE TWILIOWHATSAPPRESOURCE.java!
        ///TwilioWhatsapp
        //if (canal.getClass().getSimpleName().equals("TwilioWhatsapp")) {
        if (!body.getAsJsonObject().has("sid")) {
            return ErrorsAndWarnings.ERROR_MISSING_PARAMETER.toJsonWithDetails("No \"sid\" parameter.");
        }

        if (!body.getAsJsonObject().has("status")) {
            return ErrorsAndWarnings.ERROR_MISSING_PARAMETER.toJsonWithDetails("No \"status\" parameter.");
        }
        ///}

        String idExterno = body.getAsJsonObject().get("sid").getAsString();
        String estadoEntrega = body.getAsJsonObject().get("status").getAsString();
        boolean knownIdExterno = false;

        for (EstadoDeEntregaDeMensagemEnviadaAContacto e : canal.getEstadoDeEntregaDeMensagemEnviadaAContactoSet()) {
            if (e.getIdExterno().equals(idExterno)) {

                e.setEstadoEntrega(estadoEntrega);
                knownIdExterno = true;

                break;
            }
        }

        if (!knownIdExterno) {
            return ErrorsAndWarnings.UNKNOWN_MESSAGE_SID.toJson();
        }
        else {
            return ErrorsAndWarnings.SUCCESS_THANKS.toJson();
        }
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        return null;
    }


    //Notifcenter callback

    @SkipCSRF
    @RequestMapping(value = "notifcentercallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement notifcenterCallback(HttpServletRequest request) {
        List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
        JsonObject jObj = new JsonObject();
        jObj.addProperty("response", "elements are these:");
        jObj.addProperty("header names", request.getHeaderNames().toString());

        for (String name : parameterNames) {
            jObj.addProperty(name, request.getParameter(name));
        }

        System.out.println("####### got new notifcentercallback message!!");
        System.out.println(jObj.toString());

        /*
        String sid = null;
        String status = null;

        if((sid = getRequiredValue(jObj, "sid")) != null && (status = getRequiredValue(jObj, "MessageStatus")) != null) {
            System.out.println("sid: " + sid + ", messsagestatus: " + status + ", MessageSid: " + getRequiredValue(jObj, "MessageSid") + ", AccountSid: " + getRequiredValue(jObj, "AccountSid"));
        }
        else {
            System.out.println("no sid and status");
        }
        */

        return jObj;
    }


    // CANAL NOTIFICACAO

    @SkipCSRF
    @RequestMapping(value = "/{app}/pedidocanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement pedidoCanalNotificacao(@PathVariable("app") Aplicacao app,
                                           @RequestParam("canal") Canal canal,
                                           @RequestParam("remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        ///TODO associar canais de notificacao todos ao sistemadenotifiacoes? (sim ou nao? por ccausa do pedidocriacaocanalnotificacao)
        CanalNotificacao pedidoCriacaoCanalNotificacao = CanalNotificacao.createCanalNotificacao(canal, remetente, true);

        return view(pedidoCriacaoCanalNotificacao, CanalNotificacaoAdapter.class);
    }

    @RequestMapping(value = "/{msg}/deliverystatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement getMessageStatus(/*@PathVariable("app") Aplicacao app,*/ @PathVariable("msg") Mensagem msg) {

        /*if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }*/

        if (!FenixFramework.isDomainObjectValid(msg)) { // || !msg.getCanalNotificacao().getRemetente().getAplicacao().getExternalId().equals(app.getExternalId())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        JsonObject jObj = view(msg, MensagemAdapter.class).getAsJsonObject();

        JsonArray jArray = new JsonArray();

        //TODO -> done.
        for (EstadoDeEntregaDeMensagemEnviadaAContacto e : msg.getEstadoDeEntregaDeMensagemEnviadaAContactoSet()) {
            jArray.add(view(e, EstadoDeEntregaDeMensagemEnviadaAContactoAdapter.class));
        }

        jObj.add("status", jArray);

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/sendmessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement sendMessage(@PathVariable("app") Aplicacao app,
                                   @RequestParam("canalnotificacao") CanalNotificacao canalNotificacao,
                                   @RequestParam("gdest") PersistentGroup[] gruposDestinatarios,
                                   @RequestParam("assunto") String assunto,
                                   @RequestParam("textocurto") String textoCurto,
                                   @RequestParam("textolongo") String textoLongo,
                                   @RequestParam(value = "dataentrega", required = false) @DateTimeFormat(pattern="dd.MM.yyyy HH:mm:ss.SSS") DateTime dataEntrega,
                                   @RequestParam(value = "callbackurl", required = false) String callbackUrlEstadoEntrega,
                                   @RequestParam(value = "anexos", required = false) MultipartFile[] anexos) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(canalNotificacao)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR, "canalNotificacao + '" + canalNotificacao.toString() + "' doesnt exist.");
        }
        if (!app.getRemetentesSet().contains(canalNotificacao.getRemetente())) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_CANALNOTIFICACAO_ERROR);
        }

        for (PersistentGroup group : gruposDestinatarios) {
            if (!FenixFramework.isDomainObjectValid(group)) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR, "Group + '" + group.toString() + "' doesnt exist.");
            }

            if (canalNotificacao.getRemetente().getGruposSet().stream().noneMatch(e -> e.equals(group))) {
                throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_GROUP_ERROR, "No permissions to send messages to group + '" + group.getExternalId() + "'!");
            }
        }

        if (textoCurto.length() > Integer.parseInt(NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemTextoCurtoMaxSize())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "TextoCurto must be at most " +
                    NotifcenterSpringConfiguration.getConfiguration().notifcenterMensagemTextoCurtoMaxSize() + " characters long.");
        }

        ArrayList<Attachment> attachments = null;

        if (anexos != null) {
            attachments = new ArrayList<>();

            for (MultipartFile file: anexos) {
                try {
                    attachments.add(Attachment.createAttachment(file.getOriginalFilename(), "lowlevelname-" + canalNotificacao.getExternalId() + " " + file.getOriginalFilename(), file.getInputStream()));
                    //System.out.println("anexo: " + FileDownloadServlet.getDownloadUrl(at));
                } catch (IOException e) {
                    //e.printStackTrace();
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "Attachment \"" + file.getOriginalFilename() + "\" could not be loaded.");
                }
            }
        }

        Mensagem msg = Mensagem.createMensagem(canalNotificacao, gruposDestinatarios, assunto, textoCurto, textoLongo, dataEntrega, callbackUrlEstadoEntrega, attachments);
        /*return*/
        //System.out.println(view(msg, MensagemAdapter.class).toString());

        //TwilioWhatsapp tw = FenixFramework.getDomainObject("281835753963522");
        //TwilioWhatsapp tw = (TwilioWhatsapp) msg.getCanalNotificacao().getCanal();

        ///
        /*
        Canal tw = msg.getCanalNotificacao().getCanal();

        List<ResponseEntity<String>> responseEntities = new ArrayList<>();

        for (PersistentGroup group : msg.getGruposDestinatariosSet()) {
            group.getMembers().forEach(user -> {

                System.out.println("user: " + user.getDisplayName() + " with email: " + user.getEmail());

                for (Contacto contacto : user.getContactosSet()) {

                    if (contacto.getCanal().getExternalId().equals(tw.getExternalId())) {
                        //responseEntities.add(tw.sendMessage("whatsapp:+351961077271", msg.getTextoCurto()));
                        responseEntities.add(tw.sendMessage(contacto.getDadosContacto(), msg.getTextoCurto()));
                    }
                }
            });
        }

        //ResponseEntity<String> responseEntity = tw.sendMessage("whatsapp:+351961077271", msg.getTextoCurto());

        responseEntities.stream().forEach(responseEntity -> {
            if (responseEntity == null) {
                throw new NotifcenterException(ErrorsAndWarnings.COULD_NOT_DELIVER_MESSAGE, "Channel '" + msg.getCanalNotificacao().getCanal().getClass().getName() + "' is unavailable right now. Try again later.");
                //ODO dizer que falhou envio para pessoa X ///
            }
        });
        */
        ///

        /*if (responseEntity == null) {
            throw new NotifcenterException(ErrorsAndWarnings.COULD_NOT_DELIVER_MESSAGE, "Channel '" + msg.getCanalNotificacao().getCanal().getClass().getName() + "' is unavailable right now. Try again later.");
            //return new ResponseEntity<String>("nope!", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }*/

        //return responseEntity.getBody().toString();
        ///return new JsonParser().parse(responseEntity.getBody());
        ///return new JsonParser().parse(responseEntities.stream().map(HttpEntity::getBody).collect(Collectors.joining(",")));

        //hmm https://howtodoinjava.com/spring-boot2/enableasync-async-controller/
        ///new Thread(() -> {
        Canal ic = msg.getCanalNotificacao().getCanal();
        ic.sendMessage(msg);
        ///}).start();

        return view(msg, MensagemAdapter.class);
    }


    @RequestMapping(value = "/attachments/{fileName}", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadAttachment(@PathVariable("fileName") GenericFile genericFile) {

        if (!FenixFramework.isDomainObjectValid(genericFile)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ATTACHMENT_ERROR);
        }

        byte[] fileContent = genericFile.getContent();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", genericFile.getContentType());
        header.add("Content-Disposition", "attachment; filename=" + genericFile.getDisplayName().replace(" ", "_"));
        header.add("Content-Length", String.valueOf(fileContent.length));
        ///header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + genericFile.getDisplayName().replace(" ", "_"));
        ///header.setContentLength(fileContent.length);

        return new HttpEntity<>(fileContent, header);
    }


    //Called when NotifcenterException is thrown due to some error

    @ExceptionHandler({NotifcenterException.class})
    public ResponseEntity<JsonElement> errorHandler(NotifcenterException ex) {

        HttpHeaders header = new HttpHeaders();

        return new ResponseEntity<>(ex.getErrorsAndWarnings().toJson(), header, ex.getErrorsAndWarnings().getHttpStatus());
    }


    // LIST CANAIS / APPS / USERS / GROUPS / ATTACHMENTS / MENSAGENS

    @RequestMapping(value = "/listcanais", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listCanais() {

        JsonArray jArray = new JsonArray();

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            jArray.add(view(c, CanalAdapter.class));
        }

        return jArray;
    }

    @RequestMapping(value = "/listapps", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listApps() {

        JsonArray jArray = new JsonArray();

        for (Aplicacao a: SistemaNotificacoes.getInstance().getAplicacoesSet()) {
            jArray.add(view(a, AplicacaoAdapter.class));
        }

        return jArray;
    }

    @RequestMapping(value = "/listusers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listUsers() {

        JsonArray jArray = new JsonArray();

        for (User a: FenixFramework.getDomainRoot().getBennu().getUserSet()) {
            jArray.add(view(a, UserAdapter.class));
        }

        return jArray;
    }

    @RequestMapping(value = "/listgroups", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listGroups() {

        JsonArray jArray = new JsonArray();

        for (PersistentGroup g: FenixFramework.getDomainRoot().getBennu().getGroupSet()) {
            jArray.add(view(g, PersistentGroupAdapter.class));
        }

        return jArray;
    }

    @RequestMapping(value = "/attachments/list", method = RequestMethod.GET)
    public JsonElement listAttachments() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (FileStorage fs : FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet()) {
            if (fs.getName().equals(NotifcenterSpringConfiguration.getConfiguration().notifcenterFileStorageName())) {
                for (GenericFile atch : fs.getFileSet()) {
                    jArray.add(view(atch, AttachmentAdapter.class));
                }
                break;
            }
        }

        jObj.add("attachments", jArray);

        return jObj;
    }

    @RequestMapping(value = "/listmessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listMessages() {

        JsonArray jArray = new JsonArray();

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem msg : cn.getMensagemSet()) {
                    jArray.add(view(msg, MensagemAdapter.class));
                }
            }
        }

        return jArray;
    }

    @RequestMapping(value = "/deletemessages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteMessages(@RequestParam(value = "msg", required = false) Mensagem msg) {

        JsonArray jArray = new JsonArray();

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem msg2 : cn.getMensagemSet()) {
                    jArray.add(msg2.getExternalId());
                    if (FenixFramework.isDomainObjectValid(msg)) {
                        if (msg.equals(msg2)) {
                            msg2.delete();
                            return jArray;
                        }
                    }
                    else {
                        msg.delete();
                    }
                }
            }
        }

        return jArray;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////

    //IGNORAR PARA BAIXO

    /////////////////////////////////////////////////////////////////////////////////////////////

    //Upload attachment (TEST)

    @SkipCSRF
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {

        ///System.out.println("fenix storages: " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(FileStorage::getName).collect(Collectors.joining(",")));

        System.out.println(" ");
        System.out.println("files in fenix (2): " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(e -> e.getFileSet().stream().map(GenericFile::getDisplayName).collect(Collectors.joining(","))).collect(Collectors.joining("|")));
        System.out.println(" ");

        Attachment at;
        String toReturn = "no file to save\n";

        if(file != null) {
            try {
                at = Attachment.createAttachment(file.getOriginalFilename(), "lowlevelname-" + file.getOriginalFilename(), file.getInputStream());

                System.out.println("getOriginalFileName: " + file.getOriginalFilename());
                System.out.println("externalId: " + at.getExternalId());

                //toreturn = FileDownloadServlet.getDownloadUrl(at) + "\n";
                toReturn = NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/apiaplicacoes/attachments/" + at.getExternalId() + "\n";

                System.out.println("getDownloadUrl(): " + FileDownloadServlet.getDownloadUrl(at));
                System.out.println("file url: " + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/apiaplicacoes/attachments/" + at.getExternalId());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return toReturn;
    }

    //SAVE FILE FROM OWN COMPUTER(TEST)
    @SkipCSRF
    @RequestMapping(value = "/uploadcomfiletxt", method = RequestMethod.POST)
    public String uploadFileTXT(@RequestParam(value = "filename", defaultValue = "/home/cr/file.txt") String filename) {

        System.out.println(" ");
        System.out.println("files in fenix (2): " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(e -> e.getFileSet().stream().map(GenericFile::getDisplayName).collect(Collectors.joining(","))).collect(Collectors.joining("|")));
        System.out.println(" ");

        Attachment at;
        File file;

        try{
            Resource filee = new FileSystemResource(filename);
            file = filee.getFile();

            System.out.println("getAbsolutePath: " + file.getAbsolutePath());

            at = Attachment.createAttachment("prettyname2", "lowlevelname2", file);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return "ok";
    }

    //Uso PersistentGroup e não Group, pois ao usar Group dá o erro:
    //"Failed to convert value of type 'java.lang.String' to required type 'org.fenixedu.bennu.core.groups.Group'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'org.fenixedu.bennu.core.groups.Group': no matching editors or conversion strategy found"
    @RequestMapping(value = "/isusergroupmember", method = RequestMethod.GET)
    public String isGroupMember(@RequestParam("user") User user,
                                @RequestParam("group") PersistentGroup group) {

        if (group.isMember(user)) {
            return "yes!";
        }

        return "no";
    }

    // CANAL (TEST)

    //POST http://localhost:8080/notifcenter/apiaplicacoes/canal?email=email2&password=password2
    /*@RequestMapping(value = "/canal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement canal(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {

        Canal canal = Canal.createCanal(email, password);

        return view(canal, CanalAdapter.class);
    }*/

    @RequestMapping(value = "/viewcanal/{canal}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewCanal(@PathVariable(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        return view(canal, CanalAdapter.class);
    }


    /* IGNORAR - UpdateAppPermissions
    @SkipCSRF
    @RequestMapping(value = "/{app}/updatepermissions", method = RequestMethod.POST)
    public String UpdateAppPermissions(@PathVariable("app") Aplicacao app, @RequestParam("permissions") AppPermissions appPermissions) {

        if (app == null || !FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        System.out.println("New app permissions: " + appPermissions.name());

        app.SetAppPermissions(appPermissions);

        return view(app, AplicacaoAdapter.class).toString();
    }*/


    //IGNORAR TWILIO

    /* ///
    @RequestMapping(value = "/twiliowhatsappsms", method = RequestMethod.POST)
    public ResponseEntity<String> twilioWhatsappSMS(@RequestParam(value = "message",
            defaultValue = "mensagem teste do notifcenter 1 =D") String message) {

        ///comentar depois de usar:
        ///TwilioWhatsapp twilioWhatsapp = TwilioWhatsapp.createTwilioWhatsappFromPropertiesFile("twiliowhatsapp1");
        TwilioWhatsapp twilioWhatsapp = FenixFramework.getDomainObject("281835753963522");

        ResponseEntity<String> responseEntity = twilioWhatsapp.sendMessage("whatsapp:+351961077271", message);

        if (responseEntity == null) {
            return new ResponseEntity<String>("nope!", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }
    */

    /*
    @RequestMapping(value = "/twilio", method = RequestMethod.GET)
    public String twilio() {
        Twilio.createTwilio("some_sid", "some_auth_token");

        String t = SistemaNotificacoes.getInstance().getCanaisSet().stream().map(Canal::getEmail).collect(Collectors.joining(","));

        return "emails dos canais: " + t;
    }
    */

    @RequestMapping(value = "/twiliowhatsappfile", method = RequestMethod.GET)
    public String twiliofile() {
        //opens file /channelscredentials/twiliowhatsapp1.properties:
        TwilioWhatsapp twilioWhatsapp = TwilioWhatsapp.createTwilioWhatsappFromPropertiesFile("twiliowhatsapp1");

        System.out.println(view(twilioWhatsapp, CanalAdapter.class).toString());

        String t = SistemaNotificacoes.getInstance().getCanaisSet().stream().map(Canal::getEmail).collect(Collectors.joining(","));

        return "emails dos canais: " + t;
    }


    // IGNORAR (HTTP client sync and async tests):

    @RequestMapping(value = "/oauth/viewaplicacaodelayed/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacaoDelayed(@PathVariable("app") Aplicacao app) {

        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) { }

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        return view(app, AplicacaoAdapter.class).toString();
    }


    @RequestMapping(value = "viewaplicacaoclientsync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> viewAplicacaoClientSync(@RequestParam(value = "app", defaultValue = "281736969715714") Aplicacao app) {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacaodelayed/" + app.getExternalId() + "/?access_token=NTYz";

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("headerParam1", Arrays.asList("headerParamValue1")); //Collections.singletonList

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("bodyParam1", Arrays.asList("bodyParamValue1")); //Collections.singletonList

        return HTTPClient.restSyncClient(HttpMethod.GET, uri, header, body);
    }


    @RequestMapping(value = "viewaplicacaoclientasync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewAplicacaoClientASync(@RequestParam(value = "app", defaultValue = "281736969715714") Aplicacao app) {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacaodelayed/" + app.getExternalId() + "/?access_token=NTYz";

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("headerParam1", Arrays.asList("headerParamValue1")); //Collections.singletonList

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("bodyParam1", Arrays.asList("bodyParamValue1")); //Collections.singletonList

        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
        deferredResult.setResultHandler((Object responseEntity) -> {
            HTTPClient.printResponseEntity((ResponseEntity<String>) responseEntity);
        });

        HTTPClient.restASyncClient(HttpMethod.GET, uri, header, body, deferredResult);

        JsonObject jObj = new JsonObject();
        jObj.addProperty("info", "waiting/processing answer...");
        return jObj;
    }


    // IGNORAR (outros testes):

    @OAuthEndpoint("scope2")
    @RequestMapping(value = "test4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test4() {
        String t4 = "test4";
        return t4;
    }

    @OAuthEndpoint("scope3")
    @RequestMapping(value = "test5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test5() {
        String t4 = "test5";
        return t4;
    }

    @RequestMapping(value = "/oauth/viewaplicacao2/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> viewAplicacao2(@PathVariable("app") Aplicacao app) {
        return new ResponseEntity<String>(view(app, AplicacaoAdapter.class).toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{app}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplic(@PathVariable("app") Aplicacao app, JsonElement json) {
        return updateApp(app, json);
    }

    protected JsonElement updateApp(Aplicacao app, JsonElement json) {
        app = update(json, app);
        return view(app, AplicacaoAdapter.class);
    }



    @RequestMapping("greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue="oi!") String name) {
        ///return new Greeting(1234, name);
        return new Greeting();
    }


    ////////////////////////////////////////////////

    //FUNCIONA (Greeting não é um domain object)
    @SkipCSRF
    @RequestMapping(value = "greet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Greeting greet(@RequestBody Greeting g) {
        System.out.println(g.toString());
        return g;
    }

    //NAO FUNCIONA (dá o erro: RuntimeException: Failed to invoke ... with no args)
    @SkipCSRF
    @RequestMapping(value = "canal1", method = RequestMethod.POST, /*consumes = MediaType.APPLICATION_JSON_VALUE,*/ produces = MediaType.APPLICATION_JSON_VALUE)
    public Canal canal1(@RequestBody Canal c) {
        return c;
    }

    //FUNCIONA
    @SkipCSRF
    @RequestMapping(value = "canal2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement canal2(@RequestBody JsonElement c) {
        return c;
    }

    //NAO FUNCIONA (não dá para retornar Canal (dá erro: "Bad object"))
    @SkipCSRF
    @RequestMapping(value = "canal3", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Canal canal3(@RequestBody JsonElement c) {
        return create(c, Canal.class);
    }

    //FUNCIONA (POSSO USAR ESTE MODO EM ALTERNATIVA AOS @RequestParam's!!
    //e.g. curl -H "Content-type: application/json" -X POST -d '{"email":"someemail@awd.com", "password":"pass1"}' http://localhost:8080/notifcenter/apiaplicacoes/canal4
    @SkipCSRF
    @RequestMapping(value = "canal4", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement canal4(@RequestBody JsonElement c) {
        return view(create(c, Canal.class), CanalAdapter.class);
    }

    ////////////////////////////////////////////////


    @RequestMapping(value = "test1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test1() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo1", "valor1");
        return jObj;
    }

    @RequestMapping(value = "test7", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test7(@RequestParam(value = "name", defaultValue="exemplo de param1") String name) {
        return view(ExemploIdentidade.createExemploIdentidade(name), ExemploIdentidadeAdapter.class);
    }

    @RequestMapping(value = "test8", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExemploIdentidade test8(@RequestParam(value = "name", defaultValue="exemplo de param1") String name) {
        return ExemploIdentidade.createExemploIdentidade(name);
    }

    @RequestMapping(value = "test9", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test9() {
        User user = User.findByUsername("admin");
        if(user != null)
            return "username '" + user.getName() + "' exists!";
        else
            return "non-existing user name";
    }


}

//core -> CSRFApiProtectionFilter nao chamado #1 #2 #3
//spring -> CSRFInterceptor nao chamado #7 #8 #9 //POR CAUSA DE ESTAR OVERRIDDEN PELO MEU INTERCEPTOR
//core -> CSRFFeature nao chamado (e.g. test4 nao tem a anotacao @skipCSRF e devia ser chamado) #5
//oauth -> BennuOAuthFeature

    /*
    @RequestMapping(value = "notifcentercallback2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifcenterCallback2(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        return "ok! Here: " + Arrays.toString(map.entrySet().toArray());
    }*/


    /*
    @RequestMapping(value = "/restURL")
    public String serveRest(@RequestBody String body, @RequestHeader HttpHeaders headers) {
        etc...
    }*/

    /*
    @ResponseBody
    @RequestMapping(value = urlPattern , method = RequestMethod.POST)
    public Test addNewWorker(@RequestBody Test jsonString) {
        //do business logic
        return test;
    }
    @RequestBody -Covert Json object to java
    @ResponseBody - convert Java object to json
    */

    /*
    Spring will use the same HttpMessageConverter objects to convert the User object as it
    does with @ResponseBody, except now you have more control over the status code and headers
    you want to return in the response.
    @RequestMapping(value = "/user?${id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = ...;
        if (user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    */

//Notifcenter callback:
//String callbackURL = "http://localhost:8080/notifcenter/apiaplicacoes/notifcentercallback";
//String queryParams = "?code=1223456789";
//HTTPClient.restSyncClient(HttpMethod.GET, callbackURL + queryParams, header, body);


//TIPS:

//print to console (nice ways):
/*
app.getRemetentesSet().forEach(System.out::println);
System.out.println(app.getRemetentesSet().stream().map(Remetente::getNome).collect(Collectors.joining(",")));
*/

//URLEncoder.encode(string, "UTF-8")


///JsonObject jObj = new JsonObject();
///jObj.addProperty("aplicacao", app.getName());
///jObj.addProperty("remetentes", app.getRemetentesSet().stream().map(Remetente::getNome).collect(Collectors.joining(",")));

////List<String> resultSet = app.getRemetentesSet().stream().flatMap(e -> Stream.of(e.getNome(), e.getExternalId())).collect(Collectors.toList());
///System.out.println(resultSet.toString());


    /*nao é necessario (usado para criar novos file storages:
    String st = FenixFramework.getDomainRoot().getBennu().getFileSupport().getDefaultStorage().store(at, file.getInputStream());
    System.out.println("String store(GenericFile, File) returns: " + st);
    System.out.println(" ");

    String i = new String(FenixFramework.getDomainRoot().getBennu().getFileSupport().getDefaultStorage().read(at));
    System.out.println("byte[] read(GenericFile) returns: " + i);
    System.out.println(" ");*/


    /*        try {
            WrittenEvaluation eval = getDomainObject(oid, WrittenEvaluation.class);
            if (!StringUtils.isBlank(enrol)) {
                if (enrol.equalsIgnoreCase(ENROL)) {
                    EnrolStudentInWrittenEvaluation.runEnrolStudentInWrittenEvaluation(getPerson().getUsername(),
                            eval.getExternalId());
                } else if (enrol.equalsIgnoreCase(UNENROL)) {
                    UnEnrollStudentInWrittenEvaluation.runUnEnrollStudentInWrittenEvaluation(getPerson().getUsername(),
                            eval.getExternalId());
                }
            }
            return evaluations(response, request, context);

        } catch (Exception e) {
            throw newApplicationError(Status.PRECONDITION_FAILED, e.getMessage(), e.getMessage());
        }
    }
    */


        /*
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "filename", defaultValue = "/home/cr/file.txt") String filename) {

        //Resource resource = new ClassPathResource(filename, getClass());
        Resource file = new FileSystemResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    */


        /*
            @SkipCSRF
    @RequestMapping(value = "notifcentercallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement notifcenterCallback(HttpServletRequest request) {
        List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
        JsonObject jObj = new JsonObject();
        jObj.addProperty("response", "elements are these:");
        jObj.addProperty("header names", request.getHeaderNames().toString());

        String sid = null;
        String status = null;

        for (String name : parameterNames) {
            jObj.addProperty(name, request.getParameter(name));
        }

        System.out.println("####### got new notifcentercallback message!!");
        System.out.println(jObj.toString());

        if((sid = getRequiredValue(jObj, "sid")) != null && (status = getRequiredValue(jObj, "MessageStatus")) != null) {
            System.out.println("sid: " + sid + ", messsagestatus: " + status + ", MessageSid: " + getRequiredValue(jObj, "MessageSid") + ", AccountSid: " + getRequiredValue(jObj, "AccountSid"));
        }
        else {
            System.out.println("no sid and status");
        }

        return jObj;
    }
         */