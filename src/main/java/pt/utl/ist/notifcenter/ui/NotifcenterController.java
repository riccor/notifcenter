package pt.utl.ist.notifcenter.ui;

import com.google.common.base.Strings;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.CanalResource;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/notifcenter")
@SpringApplication(group = "anyone", path = "notifcenter", title = "title.Notifcenter")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter")
public class NotifcenterController {

    @RequestMapping
    public String home(Model model) {
        model.addAttribute("world", "World");
        return "notifcenter/home";
    }

    @ResponseBody
    @RequestMapping(value = "/exemplo/{nome}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String exemplo(@PathVariable("nome") String nome) {
        return "nome: " + nome;
    }


    //View message:

    private User getAuthenticatedUser() {
        return Authenticate.getUser();
    }

    private boolean isUserLoggedIn() {
        return Authenticate.isLogged();
    }

    @ResponseBody
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

    public void checkIsUserValid(User user) {
        if (user == null || !FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }
    }

    @RequestMapping(value = "/{msg}/view")
    public String messages(@PathVariable("msg") Mensagem msg, Model model, HttpServletRequest request) {

        if (!isUserLoggedIn()) {
            ///throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        User user = getAuthenticatedUser();
        checkIsUserValid(user);

        //model.addAttribute("world", "World");

        for (PersistentGroup g : msg.getGruposDestinatariosSet()) {
            if (g.isMember(user)) {

                model.addAttribute("message", msg);

                //tambem funciona mas não é necessario aqui (pois basta usar message.attachments):
                //List<Attachment> atl = new ArrayList<>(msg.getAttachmentsSet());
                //model.addAttribute("anexos", atl);

                List<String> attachmentsLinks = new ArrayList<>();
                for (Attachment at : msg.getAttachmentsSet()) {
                    String link = NotifcenterSpringConfiguration.getConfiguration().notifcenterUrlForAttachments() + at.getExternalId();
                    attachmentsLinks.add(link);
                }
                model.addAttribute("attachments_links", attachmentsLinks);

                return "notifcenter/messages";
            }
        }

        throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_MESSAGE_ERROR);
        //return "mytest/messages";
    }

    public void checkAdminPermissions(User user) {

        DynamicGroup g = Group.managers();

        //debug
        //g.getMembers().forEach(e -> System.out.println("admin member: " + e.getUsername()));

        if (!g.isMember(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_PAGE_ERROR, "You are not a system admin.");
        }
    }

    private <T> T getDomainObject(Class<T> clazz, String id) {
        try {
            DomainObject dObj = FenixFramework.getDomainObject(id);
            T t = (T) dObj;
            return t;
        }
        catch (Exception e) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ENTITY_ERROR, "Invalid parameter " + clazz.getSimpleName() + " id " + id + " !");
        }
    }

    @SkipCSRF
    @RequestMapping(value = "/canais") //, method = RequestMethod.POST)
    public String postCanal(Model model, HttpServletRequest request){

        if (!isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = getAuthenticatedUser();
        checkIsUserValid(user);
        checkAdminPermissions(user);

        //System.out.println("tipo: " + request.getParameter());

        if (!Strings.isNullOrEmpty(request.getParameter("channelType"))) {
            CanalResource.create2(HTTPClient.getHttpServletRequestParamsAsJson(request));
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteChannel"))) {
            String id = request.getParameter("deleteChannel");
            if (FenixFramework.isDomainObjectValid(getDomainObject(Canal.class, id))) {
                getDomainObject(Canal.class, id).delete();
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editChannel"))) {
            String id = request.getParameter("editChannel");
            if (FenixFramework.isDomainObjectValid(getDomainObject(Canal.class, id))) {
                CanalResource.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), getDomainObject(Canal.class, id));
            }
        }

        model.addAttribute("canais", getChannelsParams());
        model.addAttribute("classes_canais", CanalResource.getAvailableChannelsNamesAndParams());

        return "notifcenter/canais";
    }

    public String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public List<HashMap<String, String>> getChannelsParams() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Canal c : SistemaNotificacoes.getInstance().getCanaisSet()) {

            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("type", c.getClass().getSimpleName());
            map.put("id", c.getExternalId());
            map.put("email", c.getEmail());

            try {
                AnotacaoCanal annotation = c.getClass().getAnnotation(AnotacaoCanal.class);

                for (String key : annotation.classFields()) {
                    String methodName = "get" + capitalizeFirstLetter(key);
                    String value = (String) c.getClass().getMethod(methodName).invoke(c); //são sempre strings
                    map.put(key, value);
                }
            }
            catch (Exception e) {
                System.out.println("error on getting a channel class param");
            }

            list.add(map);
        }

        return list;
    }


}


/*
for (Map.Entry<String, List<String>> a : CanalResource.getAvailableChannelsNamesAndParams().entrySet()) {
    System.out.println(a.getKey());

    for (String c : a.getValue()) {
        System.out.println(c);
    }
}
*/
