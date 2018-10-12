package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.api.json.ExemploIdentidadeAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;

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
    @ResponseBody
    @RequestMapping(value = "/oauth/register/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String registerApp(/*@PathVariable("app") Aplicacao app*/ @PathVariable("appname") String appname) {

        if(Aplicacao.findByAplicacaoName(appname) != null){
            return "{'error':'applicationNameAlreadyRegistered','error_description':'Aplicacao name is already registered.'}";
        }

        Aplicacao app = Aplicacao.registerAplicacao(appname);
        //return "{'success':'application'}";
        return view(app, AplicacaoAdapter.class).toString();
    }

    /*AJUDA:
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

    @RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement createAplic(JsonElement json) {
        Aplicacao app = create(json, Aplicacao.class);
        return view(app, AplicacaoAdapter.class);
    }

    @RequestMapping(value = "/update/{app}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplic(@PathVariable("app") Aplicacao app, JsonElement json) {
        return updateApp(app, json);
    }

    protected JsonElement updateApp(Aplicacao app, JsonElement json) {
        app = update(json, app);
        return view(app, AplicacaoAdapter.class);
    }

    @ResponseBody
    @RequestMapping(value = "test4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test4() {
        String t4 = "test4";
        return t4;
    }

    @ResponseBody
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="oi!") String name) {
        ///return new Greeting(1234, name);
        return new Greeting();
    }

    @ResponseBody
    @RequestMapping(value = "test1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test1() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo1", "valor1");
        return jObj;
    }

    @ResponseBody
    @RequestMapping(value = "test7", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test7(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return view(ExemploIdentidade.createExemploIdentidade(name), ExemploIdentidadeAdapter.class);
    }

    @ResponseBody
    @RequestMapping(value = "test8", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExemploIdentidade test8(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return ExemploIdentidade.createExemploIdentidade(name);
    }

    @ResponseBody
    @RequestMapping(value = "test9", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test9() {
        User user = User.findByUsername("admin");
        if(user != null)
            return "username '" + user.getName() + "' exists!";
        else
            return "non-existing user name";
    }

}
