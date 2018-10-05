package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.rest.BennuRestResource;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.domain.Aplicacao;
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

    @RequestMapping(value = "test3/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test3(@PathVariable("appname") String appname) {
        return view(Aplicacao.createAplicacao(appname), AplicacaoAdapter.class);
    }

    //@POST
    //@Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement createAplicacao(JsonElement json) {
        Aplicacao app = create(json);
        return view(app, AplicacaoAdapter.class);
    }

    protected Aplicacao create(JsonElement json) {
        return create(json, Aplicacao.class);
    }

    //@PUT
    //@Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    //@Path("/{app}")
    @RequestMapping(value = "/update/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplicacao(@PathVariable("app") Aplicacao app, JsonElement json) {
        return update(app, json);
    }

    protected JsonElement update(Aplicacao app, JsonElement json) {
        app = update(json, app);
        return view(app, AplicacaoAdapter.class);
    }

    //@GET
    //@Produces(MediaType.APPLICATION_JSON)
    //@Path("/test1")
    @RequestMapping(value = "test1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test1() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo1", "valor1");
        return jObj;
    }

}
