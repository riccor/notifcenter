package pt.utl.ist.notifcenter.ui;

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
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.CanalResource;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.domain.Attachment;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
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

    @SkipCSRF
    @RequestMapping(value = "/postcanal", method = RequestMethod.POST)
    public String postCanal(Model model, HttpServletRequest request){

        if (!isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = getAuthenticatedUser();
        checkIsUserValid(user);
        checkAdminPermissions(user);

        //System.out.println("tipo: " + request.getParameter("channelType"));

        CanalResource.create2(HTTPClient.getHttpServletRequestParamsAsJson(request));

        ///model.addAttribute("world", "cheguei!");
        List<Canal> canais = new ArrayList<>(SistemaNotificacoes.getInstance().getCanaisSet());
        model.addAttribute("canais", canais);
        model.addAttribute("classes_canais", CanalResource.getAvailableChannelsNamesAndParams());

        return "notifcenter/canais";
    }

    @RequestMapping(value = "/canais")
    public String canais(Model model, HttpServletRequest request) {

        if (!isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = getAuthenticatedUser();
        checkIsUserValid(user);
        checkAdminPermissions(user);

        List<Canal> canais = new ArrayList<>(SistemaNotificacoes.getInstance().getCanaisSet());
        model.addAttribute("canais", canais);
        model.addAttribute("classes_canais", CanalResource.getAvailableChannelsNamesAndParams());


        return "notifcenter/canais";
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
