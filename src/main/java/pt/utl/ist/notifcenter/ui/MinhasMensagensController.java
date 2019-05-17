package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.security.SkipAccessTokenValidation;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/minhasmensagens")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.minhasmensagens")
public class MinhasMensagensController {

    @SkipCSRF
    @SkipAccessTokenValidation
    @RequestMapping
    public String minhasMensagens(Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            ///throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user1 = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user1);

        model.addAttribute("utilizador", user1.getUsername());
        model.addAttribute("messages", MensagensController.getExistingUserMensagens(user1));

        return "notifcenter/usermessages";
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
