package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.avro.data.Json;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.api.json.ExemploIdentidadeAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;

import java.util.Set;

@RestController
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    /*
        3.1.3 Adicão de aplicacao:
                1. Aplicação regista-se no sistema usando protocolo de autenticação;
                2. Sistema verifca e guarda dados da aplicação;
                3. Administrador defne as permissões da aplicação;
                4. Sistema verifca e guarda confgurações.
    */

    @RequestMapping(value = "/oauth/viewapplication/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewapplication(@PathVariable("app") Aplicacao app) {
        return view(app, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "/oauth/viewapplication2/{app}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewapplication2(@PathVariable("app") Aplicacao app) {
        return view(app, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "/oauth/viewapplicationsecret/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewapplicationsecret(@PathVariable("app") Aplicacao app) {
        return app.getSecret();
    }

    //ERRO: "HTTP Status 400 - CSRF Token not present or incorrect!" -> https://pastebin.com/vGe1y97c
    @RequestMapping(value = "/oauth/addapplicationtest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addApplicationTest1(@RequestBody Aplicacao jObj) {
        return view(jObj, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "/oauth/addapplication", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addApplication(@RequestParam(value="description") String description, @RequestParam(value="name") String name, @RequestParam(value="redirect_uri") String redirectUrl, @RequestParam(value="author", defaultValue = "none") String authorName, @RequestParam(value="site_url", defaultValue = "none") String siteUrl) {

        if (Aplicacao.findByAplicacaoName(name) != null) {
            JsonObject jObj = new JsonObject();
            jObj.addProperty("error", "applicationNameAlreadyRegistered");
            jObj.addProperty("error_description", "Such application name is already registered.");
            return jObj;
        }

        Aplicacao app = Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
        return view(app, AplicacaoAdapter.class);
    }

    /*
    @RequestMapping(value = "/remetente/{app}/adicionar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement adicionarRemetente(@PathVariable("app") Aplicacao app,
                                          @RequestParam(value="nome") String nomeRemetente,
                                          @RequestParam(value="access_token") String accessToken) {

        //como faço aqui para confirmar se o access_token é valido ou não?

        return view(Remetente.createRemetente(app, nomeRemetente), RemetenteAdapter.class);
    }*/

    @RequestMapping(value = "/remetente/{app}/adicionar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement adicionarRemetente(@PathVariable("app") Aplicacao app, @RequestParam(value="nome") String nomeRemetente) {

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);

        //Nota: vou mais tarde inserir este código no método RemetenteAdapter.view():
        JsonObject jObj = new JsonObject();
        jObj.addProperty("remetenteId", remetente.getExternalId());
        jObj.addProperty("name", remetente.getNome());
        jObj.addProperty("appID", remetente.getAplicacao().getExternalId());
        return jObj;
    }

    @RequestMapping(value = "/remetente/{app}/listar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listarRemetentes(@PathVariable("app") Aplicacao app) {

        JsonObject jObj = new JsonObject();
        jObj.addProperty("appId", app.getExternalId());
        jObj.addProperty("remetentes", app.getRemetentesSet().toString());
        return jObj;
    }


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

    @RequestMapping(value = "test7", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test7(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return view(ExemploIdentidade.createExemploIdentidade(name), ExemploIdentidadeAdapter.class);
    }

    @RequestMapping(value = "test8", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
