package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.ContactoAdapter;
import pt.utl.ist.notifcenter.api.json.UserAdapter;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.Contacto;
import pt.utl.ist.notifcenter.ui.NotifcenterController;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import org.fenixedu.bennu.core.rest.BennuRestResource;

@RestController
@RequestMapping("/apiutilizadores")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api.utilizadores")
public class UtilizadoresResource extends BennuRestResource {

    //AGRUPAMENTO: Utilizador e respetivos contactos para canais

    @RequestMapping(value = "/{utilizador}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement showUtilizador(@PathVariable("utilizador") User utilizador) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        return view(utilizador, UserAdapter.class);
    }

    @RequestMapping(value = "/listutilizadores", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listUtilizadores() {

        JsonArray jArray = new JsonArray();

        for (User u: FenixFramework.getDomainRoot().getBennu().getUserSet()) {
            jArray.add(view(u, UserAdapter.class));
        }

        return jArray;
    }

    @SkipCSRF
    @RequestMapping(value = "/{utilizador}/addcontacto", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addContactoUtilizador(@PathVariable("utilizador") User utilizador,
                                             @RequestParam(value = "dados") String dadosContacto,
                                             @RequestParam(value = "canal") Canal canal) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(canal)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_ERROR);
        }

        for (Contacto c : utilizador.getContactosSet()) {
            if (c.getCanal().equals(canal) && c.getDadosContacto().equals(dadosContacto)) {
                String a = "Contact data " + dadosContacto + " already exists for channel " + canal.getExternalId() + " and user " + utilizador.getExternalId() + "!";
                System.out.println(a);
                throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_RELATION, a);
            }
        }

        Contacto contacto = Contacto.createContacto(utilizador, canal, dadosContacto);

        return view(contacto, ContactoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{utilizador}/{contacto}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteContacto(@PathVariable("utilizador") User utilizador, @PathVariable(value = "contacto") Contacto contacto) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(contacto) || !utilizador.getContactosSet().contains(contacto)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CONTACT_ERROR);
        }

        JsonObject jObj = new JsonObject();
        jObj.add("deleted contacto", view(contacto, ContactoAdapter.class));

        contacto.delete();

        return jObj;
    }

    @SkipCSRF
    @RequestMapping(value = "/{utilizador}/{contacto}/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteContacto(@PathVariable("utilizador") User utilizador, @PathVariable(value = "contacto") Contacto contacto,
                                      @RequestBody JsonElement body) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(contacto) || !utilizador.getContactosSet().contains(contacto)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CONTACT_ERROR);
        }

        return view(update(body, contacto, ContactoAdapter.class), ContactoAdapter.class);
    }

    @SkipCSRF
    @RequestMapping(value = "/{utilizador}/{contacto}/update2", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement deleteContacto2(@PathVariable("utilizador") User utilizador, @PathVariable(value = "contacto") Contacto contacto,
                                       @RequestParam(value = "data") String dados) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(contacto) || !utilizador.getContactosSet().contains(contacto)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CONTACT_ERROR);
        }

        contacto.update(dados);

        return view(contacto, ContactoAdapter.class);
    }

    @RequestMapping(value = "/{utilizador}/{contacto}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement showUtilizador(@PathVariable("utilizador") User utilizador, @PathVariable(value = "contacto") Contacto contacto) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(contacto) || !utilizador.getContactosSet().contains(contacto)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CONTACT_ERROR);
        }

        return view(contacto, ContactoAdapter.class);
    }

    @RequestMapping(value = "/{utilizador}/listcontactos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listContactosUtilizador(@PathVariable("utilizador") User utilizador) {

        if (!FenixFramework.isDomainObjectValid(utilizador)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        JsonObject jObj = new JsonObject();
        JsonArray jArray = new JsonArray();

        for (Contacto c : utilizador.getContactosSet()) {
            jArray.add(view(c, ContactoAdapter.class));
        }

        jObj.addProperty("utilizador", utilizador.getExternalId());
        jObj.add("contactos", jArray);

        return jObj;
    }

    //Called when NotifcenterException is thrown due to some error

    @ExceptionHandler({NotifcenterException.class})
    public ResponseEntity<JsonElement> errorHandler(NotifcenterException ex) {

        HttpHeaders header = new HttpHeaders();

        if (ex.getMoreDetails() != null) {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJsonWithDetails(ex.getMoreDetails()), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
        else {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toJson(), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
    }
}
