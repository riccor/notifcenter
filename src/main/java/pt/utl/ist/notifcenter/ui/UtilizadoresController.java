package pt.utl.ist.notifcenter.ui;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.DomainEntity;
import pt.ist.fenixframework.dml.Slot;
import pt.ist.fenixframework.dml.ValueType;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.api.json.ContactoAdapter;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.Contacto;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.security.SkipAccessTokenValidation;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/utilizadores")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.utilizadores")
public class UtilizadoresController {

    @SkipCSRF
    @SkipAccessTokenValidation
    @RequestMapping("/{user}")
    public String contactos(Model model, HttpServletRequest request, @PathVariable("user") User user) {

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user1 = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user1);
        //UtilsResource.checkNotifcenterAdminsGroupPermissions(user1);

        if (!FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        //Avoid hacks (if user is not notifcenter admin or bennu manager, they can only edit their own contacts)
        if (!UtilsResource.isUserBennuManager(user1) && !UtilsResource.isUserNotifcenterAdmin(user1)) {
            if (!user.equals(user1)) {
                throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_PAGE_ERROR);
            }
        }

        String changesMessage = "";

        if (!Strings.isNullOrEmpty(request.getParameter("createContacto"))) {
            JsonObject jsonObject = HTTPClient.getHttpServletRequestParamsAsJson(request, "utilizador"); //avoid hacks
            jsonObject.addProperty("utilizador", user.getExternalId());
            ContactoAdapter.create2(jsonObject);

            changesMessage = "Contact was added!";
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteContacto"))) {
            String id = request.getParameter("deleteContacto");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Contacto.class, id))) {
                if (!user.getContactosSet().contains(UtilsResource.getDomainObject(Contacto.class, id))) {
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_CONTACT_ERROR);
                }
                else {
                    UtilsResource.getDomainObject(Contacto.class, id).delete();
                    changesMessage = "Contact was removed!";
                }
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editContacto"))) {
            String id = request.getParameter("editContacto");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Contacto.class, id))) {
                if (!user.getContactosSet().contains(UtilsResource.getDomainObject(Contacto.class, id))) {
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_CONTACT_ERROR);
                }
                else {
                    ContactoAdapter.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), UtilsResource.getDomainObject(Contacto.class, id));
                    changesMessage = "Contact was edited!";
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("contacts", getExistingUserContactos(user));
        model.addAttribute("parametros_contacto", new String[]{"data"});
        model.addAttribute("canais", CanaisController.getExistingChannels());
        model.addAttribute("changesmessage", changesMessage);

        return "notifcenter/contactos";
    }

    public static List<HashMap<String, String>> getExistingUserContactos(User user) {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Contacto c : user.getContactosSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", c.getExternalId());
            map.put("channel", c.getCanal().getClass().getSimpleName() + " (" + c.getCanal().getExternalId() + ")");
            map.put("data", c.getDadosContacto());
            list.add(map);
        }

        return list;
    }

    @SkipAccessTokenValidation
    @RequestMapping("/{user}/messages")
    public String userMensagens(Model model, HttpServletRequest request, @PathVariable("user") User user) {

        if (!UtilsResource.isUserLoggedIn()) {
            ///throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user1 = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user1);
        //UtilsResource.checkNotifcenterAdminsGroupPermissions(user);

        if (!UtilsResource.isUserBennuManager(user1) && !UtilsResource.isUserNotifcenterAdmin(user1)) {
            if (!user.equals(user1)) {
                throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_PAGE_ERROR);
            }
        }

        model.addAttribute("utilizador", user.getUsername());
        model.addAttribute("messages", MensagensController.getExistingUserMensagens(user));

        return "notifcenter/usermessages";
    }

    @SkipAccessTokenValidation
    @RequestMapping
    public String utilizadores(Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            ///throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkNotifcenterAdminsGroupPermissions(user);

        model.addAttribute("users", getExistingUtilizadores());

        return "notifcenter/utilizadores";
    }

    public List<HashMap<String, String>> getExistingUtilizadores() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (User u: FenixFramework.getDomainRoot().getBennu().getUserSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", u.getExternalId());
            map.put("username", u.getUsername());
            map.put("displayName", u.getDisplayName());
            list.add(map);
        }

        return list;
    }

    @ExceptionHandler({NotifcenterException.class})
    public ResponseEntity<String> errorHandlerHTML(NotifcenterException ex) {

        HttpHeaders header = new HttpHeaders();

        if (ex.getMoreDetails() != null) {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toHTMLWithDetails(ex.getMoreDetails()), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
        else {
            return new ResponseEntity<>(ex.getErrorsAndWarnings().toHTML(), header, ex.getErrorsAndWarnings().getHttpStatus());
        }
    }

}

