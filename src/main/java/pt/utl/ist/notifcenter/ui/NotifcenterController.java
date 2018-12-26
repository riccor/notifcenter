package pt.utl.ist.notifcenter.ui;

import com.google.gson.JsonElement;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.domain.Attachment;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

    //TODO - redireccionar para pagina web com interface grafica
    @ResponseBody
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

        if (user == null || !FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        model.addAttribute("world", "World");

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
}
