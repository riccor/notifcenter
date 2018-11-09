// Pedidos disponíveis:

//DADOS EXEMPLO:
//app "app_77": 281736969715714
//remetente "rem1": 281724084813826
//user "bennu0": 281582350893059
//user "admin": 281582350893057
//grupo "managers": 281702609977345
//canal TwilioWhatsApp: 281835753963522
//pedido de canal de notificacao: 281775624421378

//REGISTAR APP:
//POST http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://localhost:8080/notifcenter/oauth/userdialog?client_id=281736969715714&redirect_uri=http://app77_site.com/code
//POST http://localhost:8080/notifcenter/oauth/access_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&redirect_uri=http://app77_site.com/code&code=d46d6939c0564846fed10cdcb3233b18716f6fb5770d3f58c134379e43316d138471e47d42f1a49160c65c22577180705fc615df027e0017a68f47f4b595a0c3&grant_type=authorization_code
//POST http://localhost:8080/notifcenter/oauth/refresh_token?client_id=281736969715714&client_secret=y3MW4pX%2B3hGu9DbfSpYYtFx71llEx5qCpKsJdWrtlVjuG9%2FRozatZkYvWj9FbHkDEM52%2B3oWUuRCI7HYowXEfw%3D%3D&refresh_token=MjgxNjg1NDMwMTA4MTYzOjdhOWIzNDIyNTRmZDIzN2ZmODQ4N2U2NjFjMjllYWQyODAxYjhhZWMwNDFiZDhiZDU1MDEwZjI5OWNiZmQzOGI3NDQwMGEwZGNhMTAwMjFhYjMyOTYwN2U2NDJkNjMzMWMwZDQ3YmFjYmNkZDk2ZjA3ZGQ3ZmI0NTMyZTg3MzRj&grant_type=refresh_token

//METODOS
//POST http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715714?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi/POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/addremetente?name=ric&access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/listremetentes?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET/POST http://localhost:8080/notifcenter/apiaplicacoes/notifcentercallback
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/pedidocanalnotificacao?canal=281835753963522&remetente=281724084813826
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715714/281582350893057/adddadoscontacto?canal=281835753963522&data=dados1contacto@ex.com

//UTEIS:
//http://localhost:8080/notifcenter/apiaplicacoes/viewcanal/281835753963522
//http://localhost:8080/notifcenter/apiaplicacoes/listcanais
//http://localhost:8080/notifcenter/apiaplicacoes/listusers
//http://localhost:8080/notifcenter/apiaplicacoes/listapps
//http://localhost:8080/notifcenter/apiaplicacoes/listgroups
//"no" http://localhost:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893059&group=281702609977345
//"yes" http://localhost:8080/notifcenter/apiaplicacoes/isusergroupmember?user=281582350893057&group=281702609977345

package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.rest.BennuRestResource;

//import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.io.domain.FileStorage;
import org.fenixedu.bennu.io.domain.GenericFile;
import org.fenixedu.bennu.io.servlet.FileDownloadServlet;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.joda.time.DateTime;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    /*
    Numa API REST, neste caso o resource é aplicação, e queremos adicionar
    remetentes a essa aplicação. Para mim faria mais sentido ter algo do género:

       /adicionarAplicacao
       /listarAplicacoes
       /{app}
       /{app}/update
       /{app}/listarRemetentes
       /{app}/adicionarRemetente
       /{app}/{remetente}
       /{app}/{remetente}/update
       /{app}/{remetente}/remover

    O que fazer: orientar a API ao recurso.
    */


    //ADICIONAR APLICACAO

    //ver cd ./notifcenter/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security
    //@SkipAccessTokenValidation //diz ao método preHandler em "NotifcenterInterceptor.java" para aceitar pedidos sem access_token
    @SkipCSRF ///INDIFERENTE USAR ISTO SE USAR O MEU INTERCEPTOR
    @RequestMapping(value = "/oauth/addaplicacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addAplicacao(@RequestParam(value = "description") String description,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "redirect_uri") String redirectUrl,
                               @RequestParam(value = "author", defaultValue = "none") String authorName,
                               @RequestParam(value = "site_url", defaultValue = "none") String siteUrl) {

        if (Aplicacao.findByAplicacaoName(name) != null) {
           return ErrorsAndWarnings.INVALID_APPNAME_ERROR.toJson().toString();
        }

        Aplicacao app = Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
        return view(app, AplicacaoAdapter.class).toString();
    }

    @RequestMapping(value = "/oauth/viewaplicacao/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacao(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson().toString();
        }

        return view(app, AplicacaoAdapter.class).toString();
    }

    // CANAL NOTIFICACAO

    @SkipCSRF
    @RequestMapping(value = "/{app}/pedidocanalnotificacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement pedidoCanalNotificacao(@PathVariable("app") Aplicacao app,
                                           @RequestParam("canal") Canal canal,
                                           @RequestParam("remetente") Remetente remetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        if (!FenixFramework.isDomainObjectValid(canal)) {
            return ErrorsAndWarnings.INVALID_CHANNEL_ERROR.toJson();
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            return ErrorsAndWarnings.INVALID_REMETENTE_ERROR.toJson();
        }

        CanalNotificacao pedidoCriacaoCanalNotificacao = CanalNotificacao.createPedidoCriacaoCanalNotificacao(canal, remetente);

        return view(pedidoCriacaoCanalNotificacao, CanalNotificacaoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{app}/{remetente}/sendmessage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement sendMessage(@PathVariable("app") Aplicacao app,
                                   @PathVariable("remetente") Remetente remetente,
                                   @RequestParam("gdest[]") PersistentGroup[] gruposDestinatarios,
                                   @RequestParam("canalnotificacao") CanalNotificacao canalNotificacao,
                                   @RequestParam("assunto") String assunto,
                                   @RequestParam("textocurto") String textoCurto,
                                   @RequestParam("textolongo") String textoLongo,
                                   @RequestParam(value = "dataentrega", required = false) DateTime dataEntrega,
                                   @RequestParam(value = "callbackurl", required = false) String callbackUrlEstadoEntrega,
                                   //@RequestParam(value = "file[]", required = false) GenericFile[] anexos) {
                                   @RequestParam(value = "file", required = false) MultipartFile anexo) {

        //GenericFile ou MultipartFile?? ^^ multipart muito provavelmente

        if (!FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            return ErrorsAndWarnings.INVALID_REMETENTE_ERROR.toJson();
        }


        //store file in system

        ///GenericFile gf =...

        //FALTA RESTO (Primeiro perceber o genericfile

        ///String st = FenixFramework.getDomainRoot().getBennu().getFileSupport().getDefaultStorage().store(gf, anexo);
        ///System.out.println("to access file refer to: " + st);

        ///System.out.println("getDownloadUrl(): "+ FileDownloadServlet.getDownloadUrl(gf));

        return view(remetente, RemetenteAdapter.class);
    }

    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    //curl -F 'file=@/home/cr/imgg.png' http://localhost:8080/notifcenter/apiaplicacoes/upload
    //example group id: 281702609977345
    @SkipCSRF
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam(value = "file") MultipartFile file) {

        ///System.out.println("fenix storages: " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(FileStorage::getName).collect(Collectors.joining(",")));

        /* ALTERNATIVA
        System.out.println("files in fenix (1):");
        for (FileStorage fs : FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet()) {
            for (GenericFile f : fs.getFileSet()) {
                System.out.println(f.getDisplayName());
            }
        }
        */

        System.out.println(" ");
        System.out.println("files in fenix (2): " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(e -> e.getFileSet().stream().map(GenericFile::getDisplayName).collect(Collectors.joining(","))).collect(Collectors.joining("|")));
        System.out.println(" ");

        Attachment at;
        ////File file;

        try{

            ////Resource filee = new FileSystemResource("/home/cr/file.txt");
            ////file = filee.getFile();

            ///System.out.println("getOriginalFilename: " + file.getOriginalFilename());
            System.out.println("getOriginalFileName: " + file.getOriginalFilename());
            System.out.println(" ");

            at = Attachment.createAttachment("prettyname4", "lowlevelname4", file.getInputStream());

            //nao deve ser necessario:
            /*String st = FenixFramework.getDomainRoot().getBennu().getFileSupport().getDefaultStorage().store(at, file);
            System.out.println("String store(GenericFile, File) returns: " + st);
            System.out.println(" ");*/

            System.out.println("getDownloadUrl(): " + FileDownloadServlet.getDownloadUrl(at));
            System.out.println(" ");

            String i = new String(FenixFramework.getDomainRoot().getBennu().getFileSupport().getDefaultStorage().read(at));
            System.out.println("byte[] read(GenericFile) returns: " + i);
            System.out.println(" ");

        }
        catch (IOException e){
            e.printStackTrace();
        }


        return "ok";
    }


    //SAVE FILE FROM OWN COMPUTER(TEST)
    @SkipCSRF
    @RequestMapping(value = "/uploadcomfiletxt", method = RequestMethod.POST)
    public String uploadFileTXT() {

        System.out.println(" ");
        System.out.println("files in fenix (2): " + FenixFramework.getDomainRoot().getBennu().getFileSupport().getFileStorageSet().stream().map(e -> e.getFileSet().stream().map(GenericFile::getDisplayName).collect(Collectors.joining(","))).collect(Collectors.joining("|")));
        System.out.println(" ");

        Attachment at;
        File file;

        try{
            Resource filee = new FileSystemResource("/home/cr/file.txt");
            file = filee.getFile();

            ///System.out.println("getOriginalFilename: " + file.getOriginalFilename());
            System.out.println("getName: " + file.getName());
            System.out.println("getAbsolutePath: " + file.getAbsolutePath());
            System.out.println(" ");

            at = Attachment.createAttachment("prettyname2", "lowlevelname2", file);

        }
        catch (IOException e){
            e.printStackTrace();
        }


        return "ok";
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "filename", defaultValue = "/home/cr/file.txt") String filename) {

        //Resource resource = new ClassPathResource(filename, getClass());
        Resource file = new FileSystemResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    // LIST CANAIS / APPS / USERS / GROUPS
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
    @RequestMapping(value = "/canal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement canal(@RequestParam(value = "email") String email, @RequestParam(value = "password") String password) {

        Canal canal = Canal.createCanal(email, password);

        return view(canal, CanalAdapter.class);
    }

    @RequestMapping(value = "/viewcanal/{canal}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewCanal(@PathVariable(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(canal)) {
            return ErrorsAndWarnings.INVALID_CHANNEL_ERROR.toJson();
        }

        return view(canal, CanalAdapter.class);
    }


    //TWILIO

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

    //ADICIONAR DADOS CONTACTO

    @SkipCSRF
    @RequestMapping(value = "/{app}/{user}/adddadoscontacto", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addDadosContactoUtilizador(@PathVariable("app") Aplicacao app,
                                                  @PathVariable("user") User utilizador,
                                                  @RequestParam(value = "data") String dadosContacto,
                                                  @RequestParam(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            return ErrorsAndWarnings.INVALID_USER_ERROR.toJson();
        }

        Contacto contacto = Contacto.createContacto(utilizador, canal, dadosContacto);

        return view(contacto, ContactoAdapter.class);
    }


    // ADICIONAR REMETENTE

    @SkipCSRF
    @RequestMapping(value = "/{app}/addremetente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addRemetente(@PathVariable("app") Aplicacao app, @RequestParam(value = "name") String nomeRemetente) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);
        return view(remetente, RemetenteAdapter.class);
    }

    @RequestMapping(value = "/{app}/listremetentes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listRemetentes(@PathVariable("app") Aplicacao app) {

        if (!FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        JsonArray jArray = new JsonArray();

        for (Remetente r: app.getRemetentesSet()) {
            jArray.add(view(r, RemetenteAdapter.class));
        }

        return jArray;
    }

    //Notifcenter callback

    @SkipCSRF
    @RequestMapping(value = "notifcentercallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement notifcenterCallback(HttpServletRequest request) {
        List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
        JsonObject jObj = new JsonObject();
        jObj.addProperty("response", "elements are these:");

        for (String name : parameterNames) {
            jObj.addProperty(name, request.getParameter(name));
        }

        System.out.println(jObj.toString());

        return jObj;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////

    //IGNORAR PARA BAIXO

    /////////////////////////////////////////////////////////////////////////////////////////////

    /* IGNORAR - UpdateAppPermissions
    @SkipCSRF
    @RequestMapping(value = "/{app}/updatepermissions", method = RequestMethod.POST)
    public String UpdateAppPermissions(@PathVariable("app") Aplicacao app, @RequestParam("permissions") AppPermissions appPermissions) {

        if (app == null || !FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson().toString();
        }

        System.out.println("New app permissions: " + appPermissions.name());

        app.SetAppPermissions(appPermissions);

        return view(app, AplicacaoAdapter.class).toString();
    }*/


    // IGNORAR TWILIO

    @RequestMapping(value = "/twilio", method = RequestMethod.GET)
    public String twilio() {
        Twilio.createTwilio("some_sid", "some_auth_token");

        String t = SistemaNotificacoes.getInstance().getCanaisSet().stream().map(Canal::getEmail).collect(Collectors.joining(","));

        return "emails dos canais: " + t;
    }


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
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson().toString();
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



    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue="oi!") String name) {
        ///return new Greeting(1234, name);
        return new Greeting();
    }

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


