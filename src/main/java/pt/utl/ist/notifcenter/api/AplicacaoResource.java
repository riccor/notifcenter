// Pedidos disponíveis:

//curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"param1":"value1"}'


//OK4:
//GET http://{{DOMAIN}}:8080/notifcenter/apicanais/listcanais
//GET http://{{DOMAIN}}:8080/notifcenter/apicanais/listclassescanais
//GET http://{{DOMAIN}}:8080/notifcenter/apicanais/281835753963522 (show canal)
//POST {"name": "Messenger", "accountSID":"accountSID1", "authToken":"authToken1", "fromPhoneNumber":"fromPhoneNumber1", "uriaa":"uri2"} -> http://{{DOMAIN}}:8080/notifcenter/apicanais/addcanal


//OK3:
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao?name=app_99&redirect_uri=http://app99_site.com/code&description=descricao_app99
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listaplicacoes
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715716/update?description=d1&name=n1&redirect_uri=r1&author=a1&site_urlTONULL=s1
//POST {"description":"d2", "name":"n2", "redirect_uri":"r2", "author":"a2", "site_url": "s2"} http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715716/update2
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715716/delete

//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714 (view app)

//OK2:
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281835753963522/messagedeliverystatus // {canal}/messagedeliverystatus
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281681135140872/deliverystatus
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto3&textocurto=aparecenowhatsppcurto3&textolongo=algumtextolongo3
//DEBUG http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/deletemensagens //?msg=opcional

//OK:
//POST {"nameEEE":"novo_nome"} -> http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/update
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/delete
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/listremetentes

//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855 //{app}/{remetente}
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/listgruposdestinatarios
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/removegrupodestinario?group=281702609977345
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/adddrupodestinario?group=281702609977345

//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/pedidocanalnotificacao?canal=281835753963522
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/281775624421380/delete ///{app}/{remetente}/{canalnotificacao}/delete"
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/281724084813855/listcanaisnotificacao
//DEBUG POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/approvecanalnotificacao?cn=281775624421380
//DEBUG POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/disapprovecanalnotificacao?cn=281775624421380



//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listmensagens

//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/listutilizadores
//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/{utilizador}
//POST http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/addcontacto?canal=281835753963522&dados=whatsapp:%2B351961077271
//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/listcontactos
//GET http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879236 ///{utilizador}/{contacto}
//POST http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879234/delete
//POST http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879236/update
//POST {"data":"novos_dados"} http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/281582350893057/281715494879236/update2



//ROBOT
//http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html

//1: https://medium.com/@mayfernandes/season-api-testing-ep-01-testes-de-api-com-o-robot-framework-332b72490096
//keyword driven: https://robotizandotestes.blogspot.com/2017/09/season-tutoriais-ep-03-escrevendo-o.html
//response: https://robotizandotestes.blogspot.com/2017/11/season-api-testing-ep-02-trabalhando.html
//stackoverflow: https://stackoverflow.com/questions/45204684/robot-framework-differences-between-suite-setup-and-test-setup

//https://www.twilio.com/console/sms/whatsapp/learn
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto1&textocurto=aparecenowhatsppcurto&textolongo=algumtextolongo

//DADOS EXEMPLO:
//app "app_77": 281736969715714
//remetente "rem1": 281724084813855
//user "bennu0": 281582350893059
//user "admin": 281582350893057
//grupo "managers": 281702609977345
//canal TwilioWhatsApp: 281835753963522
//(pedido) de canal de notificacao: 281775624421380
//user "admin" -> contacto whatsapp: 281715494879236

//REGISTAR APP:
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://{{DOMAIN}}:8080/notifcenter/oauth/userdialog?client_id=281736969715714&redirect_uri=http://app77_site.com/code
//POST http://{{DOMAIN}}:8080/notifcenter/oauth/access_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&redirect_uri=http://app77_site.com/code&code=d46d6939c0564846fed10cdcb3233b18716f6fb5770d3f58c134379e43316d138471e47d42f1a49160c65c22577180705fc615df027e0017a68f47f4b595a0c3&grant_type=authorization_code
//POST http://{{DOMAIN}}:8080/notifcenter/oauth/refresh_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&refresh_token=MjgxNjg1NDMwMTA4MTYzOjdhOWIzNDIyNTRmZDIzN2ZmODQ4N2U2NjFjMjllYWQyODAxYjhhZWMwNDFiZDhiZDU1MDEwZjI5OWNiZmQzOGI3NDQwMGEwZGNhMTAwMjFhYjMyOTYwN2U2NDJkNjMzMWMwZDQ3YmFjYmNkZDk2ZjA3ZGQ3ZmI0NTMyZTg3MzRj&grant_type=refresh_token

//METODOS
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714   (view app) ?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/addremetente?name=ric&access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET/POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/notifcentercallback
//POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/281736969715714/sendmensagem?canalnotificacao=281775624421380&gdest=281702609977345&assunto=umassunto1&textocurto=aparecenowhatsppcurto&textolongo=algumtextolongo

//UTEIS:
//http://{{DOMAIN}}:8080/notifcenter/apicanais/listcanais
//http://{{DOMAIN}}:8080/notifcenter/apiutilizadores/listutilizadores
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listaplicacoes
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/listgroups
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/attachments/list
//http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/attachments/{fileName}

//"no" http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893059&group=281702609977345
//"yes" http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893057&group=281702609977345

//CURL
//curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"id":100}' http://localhost/api/postJsonReader.do
//curl -H "Content-type: application/json" -X POST -d '{"id":101, "content":"ola1"}' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/greet
//curl -H "Content-type: application/json" -X POST -d '{"email":"someemail@awd.com", "password":"pass1"}' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/canal1

//curl -F 'file=@/home/cr/imgg.png' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/upload


//curl -H "Content-type: application/x-www-form-urlencoded; charset=utf-8" -x POST -d "param1=value1&param2=value2" http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/xyz


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
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api.aplicacoes")
public class
AplicacaoResource extends BennuRestResource {

    /*
    Numa API REST, neste caso o resource é aplicação, e queremos adicionar
    remetentes a essa aplicação. Para mim faria mais sentido ter algo do género:

        //API Aplicacao (/apiaplicacoes):

       /addaplicacao
       /listaplicacoes
       /{app}
       /{app}/update
       /{app}/delete

       /{app}/addremetente
       /{app}/listremetentes
       /{app}/{remetente}
       /{app}/{remetente}/update
       /{app}/{remetente}/delete
       /{app}/{remetente}/addgrupodestinario
       /{app}/{remetente}/removegrupodestinario
       /{app}/{remetente}/listgruposdestinatarios

       /{app}/{remetente}/pedidocanalnotificacao
       /{app}/{remetente}/{canalnotificacao}/delete
       /{app}/{remetente}/listcanaisnotificacao
       
       /{app}/sendmensagem


       //API canal (/apicanais):
       /addcanal
       /listcanais
       /listclassescanais
       /{canal}
       /{canal}/update
       /{canal}/delete
       // /{canal}/listCanaisNotificacao //nao implementado. Usar /apiaplicacoes/{app}/{remetente}/listcanaisnotificacao
       // /{canal}/listContactos //nao implementado. Usar /apiutilizadores/{utilizador}/listcontactos


       //API utilizador (/apiutilizadores):

       /listutilizadores
       /{utilizador}
       /{utilizador}/addcontacto
       /{utilizador}/{contacto}
       /{utilizador}/{contacto}/delete
       /{utilizador}/{contacto}/update
       /{utilizador}/listcontactos

        O que fazer: orientar a API ao recurso.
    */


    //AGRUPAMENTO: APLICACOES

    //ver cd ./notifcenter/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security
    //@SkipAccessTokenValidation //diz ao método preHandler em "NotifcenterInterceptor.java" para aceitar pedidos sem access_token
    @SkipCSRF ///INDIFERENTE USAR ISTO SE USAR O MEU INTERCEPTOR
    @RequestMapping(value = "/addaplicacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @RequestMapping(value = "/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewAplicacao(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        return view(app, AplicacaoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplicacao(@PathVariable("app") Aplicacao app,
                                       @RequestParam(value = "description", required = false) String description,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "redirect_uri", required = false) String redirectUrl,
                                       @RequestParam(value = "author", required = false) String authorName,
                                       @RequestParam(value = "site_url", required = false) String siteUrl) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        return view(app.updateAplicacao(name, redirectUrl, description, authorName, siteUrl), AplicacaoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/update2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplicacao2(@PathVariable("app") Aplicacao app, @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        return view(update(body, app, AplicacaoAdapter.class), AplicacaoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteAplicacao(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.add("deleted_app", view(app, AplicacaoAdapter.class));

        app.delete();

        return jObj;
    }

    @RequestMapping(value = "/listaplicacoes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listAplicacoes() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Aplicacao a: SistemaNotificacoes.getInstance().getAplicacoesSet()) {
            jArray.add(view(a, AplicacaoAdapter.class));
        }

        jObj.add("aplicacoes", jArray);
        return jObj;
    }


    //SUB-AGRUPAMENTO: APLICACOES->REMETENTES

    @SkipCSRF
    @RequestMapping(value = "/{app}/addremetente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addRemetente(@PathVariable("app") Aplicacao app, @RequestParam(value = "name") String nomeRemetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);
        return view(remetente, RemetenteAdapter.class);
    }

    @RequestMapping(value = "/{app}/{remetente}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement showRemetente(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        return view(remetente, RemetenteAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateRemetente(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                        @RequestParam(value = "name") String name) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        remetente.update(name);

        return view(remetente, RemetenteAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/update2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateRemetente2(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                       @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        return view(update(body, remetente, RemetenteAdapter.class), RemetenteAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteRemetente(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.add("deleted_remetente", view(remetente, RemetenteAdapter.class));

        remetente.delete();

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/addgrupodestinario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addGrupoDestinario(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                          @RequestParam("group") PersistentGroup group) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (remetente.getGruposSet().contains(group)) {
            throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_RELATION, "Remetente " + remetente.getExternalId() + " already contains group " + group.getExternalId() + "!");
        }

        remetente.addGroupToSendMesssages(group);

        JsonObject jObj = new JsonObject();

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("added group", view(group, PersistentGroupAdapter.class));

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/removegrupodestinario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement removeGrupoDestinario(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                          @RequestParam("group") PersistentGroup group) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!remetente.getGruposSet().contains(group)) {
            throw new NotifcenterException(ErrorsAndWarnings.NON_EXISTING_RELATION, "Remetente " + remetente.getExternalId() + " does not have group id " + group.getExternalId() + " permissions!");
        }

        remetente.removeGroupToSendMesssages(group);

        JsonObject jObj = new JsonObject();

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("removed group", view(group, PersistentGroupAdapter.class));

        return jObj;
    }

    @RequestMapping(value = "/{app}/{remetente}/listgruposdestinatarios", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listGruposDestinatarios(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (PersistentGroup group : remetente.getGruposSet()) {
            jArray.add(view(group, PersistentGroupAdapter.class));
        }

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("grupos", jArray);

        return jObj;
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

        jObj.addProperty("appId", app.getExternalId());
        jObj.add("remetentes", jArray);

        return jObj;
    }


    // CANAL NOTIFICACAO

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/pedidocanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement pedidoCanalNotificacao(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                              @RequestParam("canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        for (CanalNotificacao cn : remetente.getCanaisNotificacaoSet()) {
            if (cn.getCanal().equals(canal)) {
                throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_RESOURCE, "Notification channel id " + cn.getExternalId()  + " for user " + remetente.getExternalId() + " and channel " + canal.getExternalId() + " was already created before.");
            }
        }

        CanalNotificacao pedidoCriacaoCanalNotificacao = CanalNotificacao.createCanalNotificacao(canal, remetente, true);

        return view(pedidoCriacaoCanalNotificacao, CanalNotificacaoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/{canalnotificacao}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteCanalNotificacao(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente,
                                              @PathVariable("canalnotificacao") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(cn) || !remetente.getCanaisNotificacaoSet().contains(cn)){
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("deleted_canalnotificacao", view(cn, CanalNotificacaoAdapter.class));

        cn.delete();

        return jObj;
    }

    @RequestMapping(value = "/{app}/{remetente}/listcanaisnotificacao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listCanaisNotificacao(@PathVariable("app") Aplicacao app, @PathVariable(value = "remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (CanalNotificacao cn: remetente.getCanaisNotificacaoSet()) {
            jArray.add(view(cn, CanalNotificacaoAdapter.class));
        }

        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.add("canais_notificacao", jArray);

        return jObj;
    }


    //debug purposes:
    @SkipCSRF
    @RequestMapping(value = "approvecanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement approveCanalNotificacao(@RequestParam("cn") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(cn)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        cn.approveCanalNotificacao();

        return view(cn, CanalNotificacaoAdapter.class);
    }
    @SkipCSRF
    @RequestMapping(value = "disapprovecanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement disapproveCanalNotificacao(@RequestParam("cn") CanalNotificacao cn) {

        if (!FenixFramework.isDomainObjectValid(cn)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        cn.disapproveCanalNotificacao();

        return view(cn, CanalNotificacaoAdapter.class);
    }


    //Notifcenter callback

    @SkipCSRF
    @RequestMapping(value = "/notifcentercallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement notifcenterCallback(HttpServletRequest request) {

        JsonObject jObj = HTTPClient.getHttpServletRequestParams(request);

        System.out.println("####### got new notifcentercallback message!!");

        System.out.println(jObj.toString());

        return jObj;
    }

    //RECEBER NOTIFICACOES DO ESTADO DE ENTREGA DE MENSAGENS POR PARTE DOS CANAIS:

    @SkipCSRF
    @RequestMapping(value = "/{canal}/messagedeliverystatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement messageDeliveryStatus(@PathVariable("canal") Canal canal, HttpServletRequest request) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        //PROBLEMA: Nao pode levar "@RequestBody JsonElement body"!
        JsonObject jObj = HTTPClient.getHttpServletRequestParams(request);

        System.out.println("####### got new messagedeliverystatus message!!");
        System.out.println(jObj.toString());

        //TODO - NAO É PRECISO FAZER ESTA DISTINCAO, POIS ESTE RECURSO ESTARÀ DENTRO DE TWILIOWHATSAPPRESOURCE.java!
        ///TwilioWhatsapp
        //"MessageStatus":"delivered","MessageSid":"SM9f705525cc4143ef8dece27557549a5f"
        //if (canal.getClass().getSimpleName().equals("TwilioWhatsapp")) {
        String idExterno = getRequiredValue(jObj.getAsJsonObject("body").getAsJsonObject(), "MessageSid");
        String estadoEntrega = getRequiredValue(jObj.getAsJsonObject("body").getAsJsonObject(), "MessageStatus");
        ///}

        for (EstadoDeEntregaDeMensagemEnviadaAContacto e : canal.getEstadoDeEntregaDeMensagemEnviadaAContactoSet()) {
            if (e.getIdExterno().equals(idExterno)) {
                e.changeEstadoEntrega(estadoEntrega);

                //If there is a message callback, then send message status to the app
                if (!e.getMensagem().getCallbackUrlEstadoEntrega().equals("none")) {

                    MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
                    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

                    header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    body.put("MessageId", Collections.singletonList(e.getMensagem().getExternalId()));
                    body.put("User", Collections.singletonList(e.getContacto().getUtilizador().getUsername())); ///?
                    body.put("MessageStatus", Collections.singletonList(estadoEntrega));

                    DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
                    deferredResult.setResultHandler((Object responseEntity) -> {
                        HTTPClient.printResponseEntity((ResponseEntity<String>) responseEntity); /// anything else to do?
                    });

                    HTTPClient.restASyncClient(HttpMethod.POST, e.getMensagem().getCallbackUrlEstadoEntrega(), header, body, deferredResult);
                }

                throw new NotifcenterException(ErrorsAndWarnings.SUCCESS_THANKS);
            }
        }

        throw new NotifcenterException(ErrorsAndWarnings.UNKNOWN_MESSAGE_SID);
    }

    private String getRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        throw new NotifcenterException(ErrorsAndWarnings.MISSING_PARAMETER_ERROR, "Missing parameter " + property + "!");
    }

    private String[] getRequiredArrayValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            //Gson googleJson = new Gson();
            //ArrayList<String> arrayList = googleJson.fromJson(obj.get(property).getAsJsonArray(), ArrayList.class);
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator<JsonElement> i = obj.get(property).getAsJsonArray().iterator();

            while (i.hasNext()) {
                arrayList.add(i.next().getAsString());
            }

            return arrayList.toArray(new String[0]);
        }
        throw new NotifcenterException(ErrorsAndWarnings.MISSING_PARAMETER_ERROR, "Missing parameter " + property + "!");
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
    @RequestMapping(value = "/{app}/sendmensagem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement sendMensagem(@PathVariable("app") Aplicacao app,
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

        //create and send message
        Mensagem msg = verifyParamsAndCreateAMessage(app, canalNotificacao, gruposDestinatarios, assunto, textoCurto, textoLongo, dataEntrega, callbackUrlEstadoEntrega, anexos);
        Canal ic = msg.getCanalNotificacao().getCanal();
        ic.sendMessage(msg);

        return view(msg, MensagemAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/sendmensagem2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement sendMensagem2(@PathVariable("app") Aplicacao app,
                                    @RequestBody JsonElement jsonElement,
                                    @RequestParam(value = "anexos", required = false) MultipartFile[] anexos) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        //extract message params from JsonElement:
        String cn = getRequiredValue(jsonElement.getAsJsonObject(), "canalnotificacao");
        CanalNotificacao canalNotificacao;
        try {
            canalNotificacao = FenixFramework.getDomainObject(cn);
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
        }

        String[] gd = getRequiredArrayValue(jsonElement.getAsJsonObject(), "gdest");

        ArrayList<PersistentGroup> al = new ArrayList<>();
        String ss = "groupId";
        try {
            for (String s : gd) {
                ss = s;
                al.add(FenixFramework.getDomainObject(s));
            }
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR, "Group " + ss + " doesnt exist.");
        }
        PersistentGroup[] gruposDestinatarios = al.toArray(new PersistentGroup[0]);

        String assunto = getRequiredValue(jsonElement.getAsJsonObject(), "assunto");
        String textoCurto = getRequiredValue(jsonElement.getAsJsonObject(), "textocurto");
        String textoLongo = getRequiredValue(jsonElement.getAsJsonObject(), "textolongo");

        String de = tryTogetRequiredValue(jsonElement.getAsJsonObject(), "dataentrega");
        DateTime dataEntrega;
        try {
            dataEntrega = DateTime.parse(de, org.joda.time.format.DateTimeFormat.forPattern("dd.MM.yyyy HH:mm:ss.SSS"));
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_DELIVERY_DATETIME_ERROR);
        }

        String callbackUrlEstadoEntrega = tryTogetRequiredValue(jsonElement.getAsJsonObject(), "callbackurl");

        //create and send message
        Mensagem msg = verifyParamsAndCreateAMessage(app, canalNotificacao, gruposDestinatarios, assunto, textoCurto, textoLongo, dataEntrega, callbackUrlEstadoEntrega, anexos);
        Canal ic = msg.getCanalNotificacao().getCanal();
        ic.sendMessage(msg);

        return view(msg, MensagemAdapter.class);
    }


    private Mensagem verifyParamsAndCreateAMessage(Aplicacao app, CanalNotificacao canalNotificacao, PersistentGroup[] gruposDestinatarios, String assunto, String textoCurto, String textoLongo, DateTime dataEntrega, String callbackUrlEstadoEntrega, MultipartFile[] anexos) {

        if (!FenixFramework.isDomainObjectValid(canalNotificacao) || !app.getRemetentesSet().contains(canalNotificacao.getRemetente())) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR, "Such canalnotificacao doesnt exist.");
        }
        if (!canalNotificacao.isApproved()) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_CANALNOTIFICACAO_ERROR, "Canalnotificacao id " + canalNotificacao.getExternalId() + " is awaiting approval by system administrators.");
        }

        for (PersistentGroup group : gruposDestinatarios) {
            if (!FenixFramework.isDomainObjectValid(group)) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR, "Group id " + group.toString() + " doesnt exist.");
            }

            if (canalNotificacao.getRemetente().getGruposSet().stream().noneMatch(e -> e.equals(group))) {
                throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_GROUP_ERROR, "No permissions to send messages to group id " + group.getExternalId() + " !");
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
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR, "Attachment " + file.getOriginalFilename() + " could not be loaded.");
                }
            }
        }

        Mensagem msg = Mensagem.createMensagem(canalNotificacao, gruposDestinatarios, assunto, textoCurto, textoLongo, dataEntrega, callbackUrlEstadoEntrega, attachments);
        return msg;
    }

    private String tryTogetRequiredValue(JsonObject obj, String property) {
        if (obj.has(property)) {
            return obj.get(property).getAsString();
        }
        return null;
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

        if (ex.getMoreDetails() != null) {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJsonWithDetails(ex.getMoreDetails()), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
        else {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJson(), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
    }


    // LIST CANAIS / APPS / USERS / GROUPS / ATTACHMENTS / MENSAGENS / CONTACTOS

    @RequestMapping(value = "/listcontactos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listContactos() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (User u : FenixFramework.getDomainRoot().getBennu().getUserSet()) {
            for (Contacto c : u.getContactosSet()) {
                jArray.add(view(c, ContactoAdapter.class));
            }
        }

        jObj.add("contactos", jArray);
        return jObj;
    }

    /*
    @RequestMapping(value = "/deletecanais", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteCanais() {

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            c.delete();
        }

        return "All channels were deleted!";
    }*/

    @RequestMapping(value = "/listgrupos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listGrupos() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (PersistentGroup g: FenixFramework.getDomainRoot().getBennu().getGroupSet()) {
            jArray.add(view(g, PersistentGroupAdapter.class));
        }

        jObj.add("grupos", jArray);
        return jObj;
    }

    @RequestMapping(value = "/listattachments", method = RequestMethod.GET)
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

    @RequestMapping(value = "/listmensagens", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listMensagens() {

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem msg : cn.getMensagemSet()) {
                    jArray.add(view(msg, MensagemAdapter.class));
                }
            }
        }

        jObj.add("mensagens", jArray);

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/deletemensagens", method = RequestMethod.POST)
    public String deleteMnsagens(@RequestParam(value = "msg", required = false) Mensagem msg) {

        boolean flag = false;

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem msg2 : cn.getMensagemSet()) {
                    if (FenixFramework.isDomainObjectValid(msg)) {
                        if (msg.equals(msg2)) {
                            msg2.delete();
                            flag = true;
                            return "Message deleted!";
                        }
                    }
                    else {
                        msg2.delete();
                    }
                }
            }
        }

        if (FenixFramework.isDomainObjectValid(msg) && !flag) {
            return "Message not found!";
        }
        else {
            return "All messages were deleted!";
        }
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

    //POST http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/canal?email=email2&password=password2
    /*@RequestMapping(value = "/canal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement canal(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {

        Canal canal = Canal.createCanal(email, password);

        return view(canal, CanalAdapter.class);
    }*/


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
    //e.g. curl -H "Content-type: application/json" -X POST -d '{"email":"someemail@awd.com", "password":"pass1"}' http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/canal4
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
            return "username " + user.getName() + " exists!";
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
//String callbackURL = "http://{{DOMAIN}}:8080/notifcenter/apiaplicacoes/notifcentercallback";
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


           /*
        if (!jObj.getAsJsonObject("body").getAsJsonObject().has("MessageSid")) {
            throw new NotifcenterException(ErrorsAndWarnings.ERROR_MISSING_PARAMETER, "No sid parameter.");
        }

        if (!jObj.getAsJsonObject("body").getAsJsonObject().has("MessageStatus")) {
            throw new NotifcenterException(ErrorsAndWarnings.ERROR_MISSING_PARAMETER, "No status parameter.");
        }

                */

           /*
        Reflections reflections = new Reflections("pt.utl.ist.notifcenter");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(AnotacaoCanal.class);

        annotated.forEach(e -> {
            AnotacaoCanal annotation = e.getAnnotation(AnotacaoCanal.class);
            String name = annotation.name();
            String[] params = annotation.creatingParams();

            System.out.println("name: " + name);
            System.out.println("params: " + params);

            JsonObject jO = new JsonObject();
            JsonArray jA = new JsonArray();

            for (String s : params) {
                jA.add(s);
            }

            jO.addProperty("name", name);
            jO.add("params", jA);

            jArray.add(jO);
        });
        */