package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Attachment;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/mensagens")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.mensagens")
public class MensagensController {

    //View message:
    @RequestMapping(value = "/{msg}")
    public String messages(@PathVariable("msg") Mensagem msg, Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            ///throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);

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
