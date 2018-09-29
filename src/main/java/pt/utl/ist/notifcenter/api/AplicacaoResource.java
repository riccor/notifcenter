package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import org.fenixedu.bennu.core.rest.BennuRestResource;
import javax.ws.rs.core.MediaType;
import pt.utl.ist.notifcenter.domain.Aplicacao;
///import pt.utl.ist.notifcenter.domain.AppPermissions;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.*;

//@RequestMapping("/notifcenter/api")
@Path("/notifcenter/api")
public class AplicacaoResource extends BennuRestResource {

    protected Aplicacao create(/*String json*/ JsonElement jsonElement) {
        return create(jsonElement, Aplicacao.class);
    }

    //@ResponseBody
    //@RequestMapping(value = "/api/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    //public Aplicacao createAplicacao(@PathParam("appname") String appName) { //PathVariable
    @POST
    @Path("/appname")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonElement /*String*/ createAplicacao(/*String jsonString*/ JsonElement jsonElement) {
        ///verifyAndGetRequestAuthor(); OU
        //accessControl("#users");
        return view(create(/*jsonString*/ jsonElement));
    }

}
