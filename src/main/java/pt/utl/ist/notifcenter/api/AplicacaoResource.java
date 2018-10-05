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
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
import pt.utl.ist.notifcenter.domain.AppPermissions;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

//@Path("/api/aplicacoes")
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    @RequestMapping(value = "test2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test2() {
        return view(SistemaNotificacoes.createAplicacao("app test name"), AplicacaoAdapter.class);
    }

    @RequestMapping(value = "test3/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test3(@PathVariable("appname") String appname) {
        return view(SistemaNotificacoes.createAplicacao(appname), AplicacaoAdapter.class);
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

}
