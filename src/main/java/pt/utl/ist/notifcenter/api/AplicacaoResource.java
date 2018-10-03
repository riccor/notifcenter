package pt.utl.ist.notifcenter.api;

/*
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET localhost:8080/notifcenter/notifcenter/aaa/eee/iii
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.rest.BennuRestResource;
///import javax.ws.rs.core.MediaType;
/*
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
*/
import pt.utl.ist.notifcenter.domain.Aplicacao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/*
///import pt.utl.ist.notifcenter.domain.AppPermissions;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ResponseBody;
*/
///import javax.ws.rs.*;


//RequestMapping("/v2") //notifcenter/v1
@Path("/aaa/eee")
public class AplicacaoResource extends BennuRestResource {

    protected Aplicacao create(/*String json*/ JsonElement jsonElement) {
        return create(jsonElement, Aplicacao.class);
    }

    //public Aplicacao createAplicacao(@PathParam("appname") String appName) { //PathVariable
    //@GET
    //@Path("/appname")
    //@Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
   /* @ResponseBody
    @RequestMapping(value = "appname" // /api/{appname} //, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement createAplicacao(JsonElement jsonElement //String jsonString// ) {
        ///verifyAndGetRequestAuthor(); OU
        //accessControl("#users");
        return view(create(//jsonString// jsonElement));
    }

    @ResponseBody
    @RequestMapping(value = "test/ola/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ola() {
        return "ola!";
    }*/

    //ResponseBody
    //RequestMapping(value = "test/adeus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    @GET
    @Path("iii")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject adeus() {
        //return "adeus!";
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo", "valor");
        return jObj;
    }

}

