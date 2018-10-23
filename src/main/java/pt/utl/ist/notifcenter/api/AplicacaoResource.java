// Pedidos disponíveis:
//POST http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715746?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//POST http://localhost:8080/notifcenter/oauth/refresh_token?client_id=281736969715746&client_secret=HaEPQ/D6JhIUltRl4MiEvhKIQR52cJuOhQHlCey0ZC/uX8le/LftpRkN9M/4SjslzO6RqRyrYS03QifOLFY%2BsA==&refresh_token=NTYzMTYwNDA2ODE4ODIwOjY3OGI2MTRhOGViYWI2ZDQyYzljZDEwYWJlYTdmNzM4OWMyZTZkN2U5MTgyNjlkODFmMzk1N2QxNWIzMjhlMDM4MWNmZWZlNDBjY2U0M2I1ZWE5ZDNlNmI1Yjc4YWY3NmU5OWQ3MjU0YjIwYjRkNDE3YTVmZDFiNzQ4ODM3YWNk&grant_type=refresh_token
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/addremetente?name=ric&access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/listremetentes?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi

package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.rest.BennuRestResource;

//import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.api.json.ExemploIdentidadeAdapter;
import pt.utl.ist.notifcenter.api.json.RemetenteAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.security.SkipAccessTokenValidation;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;

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


    //Adicionar aplicacao

    //ver cd ./notifcenter/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security //CSRFToken token = new CSRFToken("awd");
    //exemplo de pedido: http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_2&redirect_uri=http://app2_site.com/codedescription=descricao_app2
    @SkipCSRF
    @SkipAccessTokenValidation //diz ao método preHandler em "NotifcenterInterceptor.java" para aceitar pedidos sem access_token
    @RequestMapping(value = "/oauth/addaplicacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addAplicacao(@RequestParam(value="description") String description,
                               @RequestParam(value="name") String name, 
                               @RequestParam(value="redirect_uri") String redirectUrl, 
                               @RequestParam(value="author", defaultValue = "none") String authorName, 
                               @RequestParam(value="site_url", defaultValue = "none") String siteUrl) {

        if (Aplicacao.findByAplicacaoName(name) != null) {
           return ErrorsAndWarnings.INVALID_APPNAME_ERROR.toJson().toString();
        }

        Aplicacao app = Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
        return view(app, AplicacaoAdapter.class).toString();
    }

    //exemplo GET: http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715746?access_token=
    @RequestMapping(value = "/oauth/viewaplicacao/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacao(@PathVariable("app") Aplicacao app) {

        if (app == null) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson().toString();
        }
        
        return view(app, AplicacaoAdapter.class).toString();
    }


    // Adicionar remetente

    //exemplo pedido POST: http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/addremetente?name=pessoa2&access_token=
    @SkipCSRF
    @RequestMapping(value = "/{app}/addremetente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addRemetente(@PathVariable("app") Aplicacao app, @RequestParam(value="name") String nomeRemetente) {

        if (app == null) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);
        return view(remetente, RemetenteAdapter.class);
    }
    
    //exemplo pedido GET: http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/listremetentes?access_token=
    @RequestMapping(value = "/{app}/listremetentes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listRemetentes(@PathVariable("app") Aplicacao app) {

        if (app == null) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
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

