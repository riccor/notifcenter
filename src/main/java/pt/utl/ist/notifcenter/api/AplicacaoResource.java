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

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

//@Path("/api/aplicacoes")

@RequestMapping("/apiaplicacoes")
@SpringFunctionality(accessGroup = "logged", app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    @ResponseBody
    @RequestMapping(value = "test2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test2() {;
        Aplicacao app = Aplicacao.createAplicacao("app test name");
        return view(app, AplicacaoAdapter.class);
    }

    @ResponseBody
    @RequestMapping(value = "test0", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test0() {
        ///Aplicacao a = Aplicacao.createAplicacao("app test name");
        return "chegou aqui";
    }

    @RequestMapping(value = "test3/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test3(@PathVariable("appname") String appname) {
        return view(Aplicacao.createAplicacao(appname), AplicacaoAdapter.class);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement createAplic(JsonElement json) {
        Aplicacao app = createApp(json);
        return view(app, AplicacaoAdapter.class);
    }

    protected Aplicacao createApp(JsonElement json) {
        return create(json, Aplicacao.class);
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
    @RequestMapping(value = "aplic", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Aplicacao aplic(@RequestParam(value="name", defaultValue="nome_app") String name) {
        return Aplicacao.createAplicacao(name);
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
    @RequestMapping(value = "test8", method = RequestMethod.GET) ///, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExemploIdentidade test8(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return ExemploIdentidade.createExemploIdentidade(name);
    }

}
