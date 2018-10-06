package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.domain.Aplicacao;
import pt.utl.ist.notifcenter.domain.ExemploIdentidade;
import pt.utl.ist.notifcenter.domain.Greeting;
import pt.utl.ist.notifcenter.domain.AppPermissions;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

//@Path("/api/aplicacoes")
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    @RequestMapping(value = "test2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test2() {
        return view(Aplicacao.createAplicacao("app test name"), AplicacaoAdapter.class);
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

    //@POST
    //@Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
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
    @RequestMapping(value = "test5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test5() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo1", "valor1");
        return jObj;
    }

    @ResponseBody
    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="oi!") String name) {
        return new Greeting(1234, name);
    }

    @RequestMapping("/aplic")
    public Aplicacao aplic(@RequestParam(value="name", defaultValue="nome_app") String name) {
        return Aplicacao.createAplicacao(name);
    }

    @RequestMapping("/ex")
    public ExemploIdentidade ex(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return ExemploIdentidade.createExemploIdentidade(name);
    }


    @ResponseBody
    @RequestMapping(value = "test1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test1() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo1", "valor1");
        return jObj;
    }

}
