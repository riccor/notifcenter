package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import org.fenixedu.bennu.core.rest.BennuRestResource;
///import javax.ws.rs.core.MediaType;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.utl.ist.notifcenter.domain.Aplicacao;
///import pt.utl.ist.notifcenter.domain.AppPermissions;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ResponseBody;

///import javax.ws.rs.*;

@RequestMapping("/notifcenter/api")
//@Path("/notifcenter/api")
public class AplicacaoResource extends BennuRestResource {

    protected Aplicacao create(/*String json*/ JsonElement jsonElement) {
        return create(jsonElement, Aplicacao.class);
    }

    //public Aplicacao createAplicacao(@PathParam("appname") String appName) { //PathVariable
    //@GET
    //@Path("/appname")
    //@Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    @ResponseBody
    @RequestMapping(value = "/api/appname" /*/api/{appname}*/, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement createAplicacao(JsonElement jsonElement /*String jsonString*/ ) {
        ///verifyAndGetRequestAuthor(); OU
        //accessControl("#users");
        return view(create(/*jsonString*/ jsonElement));
    }

    @ResponseBody
    @RequestMapping(value = "/api/ola", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ola() {
        return "ola!";
    }

}
