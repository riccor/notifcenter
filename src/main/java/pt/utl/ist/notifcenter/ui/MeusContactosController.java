package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/meuscontactos")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.meuscontactos")
public class MeusContactosController {

    @SkipCSRF
    @RequestMapping
    public String meusContactos(Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user1 = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user1);

        model.addAttribute("user", user1);
        model.addAttribute("contacts", UtilizadoresController.getExistingUserContactos(user1));
        model.addAttribute("parametros_contacto", new String[]{"data"});
        model.addAttribute("canais", CanaisController.getExistingChannels());

        return "notifcenter/usercontacts";
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
