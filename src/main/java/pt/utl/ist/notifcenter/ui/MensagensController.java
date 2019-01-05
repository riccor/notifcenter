package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping
    public String mensagens(Model model) {
        model.addAttribute("world", "mensagens");
        return "notifcenter/home";
    }

    //View message:
    @RequestMapping("/{msg}")
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
                    String link = "http://" + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrlForAttachments() + at.getExternalId();
                    attachmentsLinks.add(link);
                }
                model.addAttribute("attachments_links", attachmentsLinks);

                return "notifcenter/messages";
            }
        }

        throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_MESSAGE_ERROR);
        //return "mytest/messages";
    }

    @RequestMapping(value = "/attachments/{fileId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadAttachment(@PathVariable("fileId") Attachment attachment) {

        if (!UtilsResource.isUserLoggedIn()) {
            throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            ///return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();

        if (user == null || !FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(attachment)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ATTACHMENT_ERROR);
        }

        if (!attachment.isAccessible(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_ATTACHMENT_ERROR);
        }

        //debug
        /*
        System.out.println("#############content key: "+ attachment.getContentKey());
        System.out.println("#############checksum algorithm: "+ attachment.getChecksumAlgorithm());
        System.out.println("#############checksum: "+ attachment.getChecksum());
        */

        byte[] fileContent = attachment.getContent();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", attachment.getContentType());
        header.add("Content-Disposition", "attachment; filename=" + attachment.getDisplayName().replace(" ", "_"));
        header.add("Content-Length", String.valueOf(fileContent.length));
        ///header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + attachment.getDisplayName().replace(" ", "_"));
        ///header.setContentLength(fileContent.length);

        return new HttpEntity<>(fileContent, header);
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
