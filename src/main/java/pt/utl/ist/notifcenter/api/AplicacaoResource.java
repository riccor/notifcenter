package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.api.json.ExemploIdentidadeAdapter;
import pt.utl.ist.notifcenter.api.json.RemetenteAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;

@RestController
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    static JsonObject jObjInvalidTokenError;
    static JsonObject jObjInvalidAppError;

    static {
        jObjInvalidTokenError = new JsonObject();
        jObjInvalidTokenError.addProperty("error", "invalidAccessToken");
        jObjInvalidTokenError.addProperty("error_description", "Invalid access token.");

        jObjInvalidAppError = new JsonObject();
        jObjInvalidAppError.addProperty("error", "invalidApp");
        jObjInvalidAppError.addProperty("error_description", "Invalid application ID.");
    }

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

    Isto é apenas um exemplo, não sei quais as operações que fazem ou não
    sentido, mas tentava orientar a API ao recurso.

    */


    //Adicionar aplicacao

    /*
    3.1.3 Adicão de aplicacao:
            1. Aplicação regista-se no sistema usando protocolo de autenticação;
            2. Sistema verifca e guarda dados da aplicação;
            3. Administrador defne as permissões da aplicação;
            4. Sistema verifca e guarda confgurações.
    */

    //ver cd ./notifcenter/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security //CSRFToken token = new CSRFToken("awd");
    //exemplo de pedido: http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_2&redirect_uri=http://app2_site.com/codedescription=descricao_app2
    @SkipCSRF
    @RequestMapping(value = "/oauth/addaplicacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addAplicacao(@RequestParam(value="description") String description, @RequestParam(value="name") String name, @RequestParam(value="redirect_uri") String redirectUrl, @RequestParam(value="author", defaultValue = "none") String authorName, @RequestParam(value="site_url", defaultValue = "none") String siteUrl) {

        if (Aplicacao.findByAplicacaoName(name) != null) {
            JsonObject jObj = new JsonObject();
            jObj.addProperty("error", "applicationNameAlreadyRegistered");
            jObj.addProperty("error_description", "Such application name is already registered.");
            return jObj.toString();
        }

        Aplicacao app = Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
        return view(app, AplicacaoAdapter.class).toString();
    }

    //exemplo GET: http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715746?access_token=
    @RequestMapping(value = "/oauth/viewaplicacao/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacao(@PathVariable("app") Aplicacao app, @RequestParam(value="access_token") String accessToken) {

        if (app == null) {
            return jObjInvalidAppError.toString();
        }

        if (!app.isValidAccessToken(accessToken)) {
            return jObjInvalidTokenError.toString();
        }

        return view(app, AplicacaoAdapter.class).toString();
    }


    // Adicionar remetente

    //exemplo pedido POST: http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/addremetente?name=pessoa2&access_token=
    @SkipCSRF
    @RequestMapping(value = "/{app}/addremetente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addRemetente(@PathVariable("app") Aplicacao app,
                                    @RequestParam(value="name") String nomeRemetente,
                                    @RequestParam(value="access_token") String accessToken) {

        if (app == null) {
            return jObjInvalidAppError;
        }

        if (!app.isValidAccessToken(accessToken)) {
            return jObjInvalidTokenError;
        }

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);
        return view(remetente, RemetenteAdapter.class);
    }
    
    //exemplo pedido GET: http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/listremetentes?access_token=
    @RequestMapping(value = "/{app}/listremetentes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listRemetentes(@PathVariable("app") Aplicacao app, @RequestParam(value="access_token") String accessToken) {

        if (app == null) {
            return jObjInvalidAppError;
        }

        if (!app.isValidAccessToken(accessToken)) {
            return jObjInvalidTokenError;
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("aplicacao", app.getName());

        String names = "";
        for (Remetente remetente : app.getRemetentesSet()) {
            names = names + ", " + remetente.getNome();
        }
        jObj.addProperty("remetentes", names);

        return jObj;
    }


    // IGNORAR (são apenas testes):

    @RequestMapping(value = "/update/{app}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplic(@PathVariable("app") Aplicacao app, JsonElement json) {
        return updateApp(app, json);
    }

    protected JsonElement updateApp(Aplicacao app, JsonElement json) {
        app = update(json, app);
        return view(app, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "test4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test4() {
        String t4 = "test4";
        return t4;
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="oi!") String name) {
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
    public JsonElement test7(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return view(ExemploIdentidade.createExemploIdentidade(name), ExemploIdentidadeAdapter.class);
    }

    @RequestMapping(value = "test8", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExemploIdentidade test8(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
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

    /*@ResponseBody
    @RequestMapping(value = "test2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test2() {
        Aplicacao app = Aplicacao.createAplicacao("app test name");
        return view(app, AplicacaoAdapter.class);
    }*/
