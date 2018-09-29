package pt.utl.ist.notifcenter.api;

import org.fenixedu.bennu.core.rest.BennuRestResource;
import org.springframework.http.MediaType;
import pt.utl.ist.notifcenter.domain.Aplicacao;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


//@RequestMapping("/notifcenter/api")
@Path("/notifcenter/api")
public class AplicacaoResource extends BennuRestResource {

    //@ResponseBody
    //@RequestMapping(value = "/api/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{appname}")
    public Aplicacao test(@PathParam("appname") String appName) { //PathVariable

        Aplicacao app = new Aplicacao();
        app.setName(appName);

        return "nome da app: " + appName;
    }
}
+
