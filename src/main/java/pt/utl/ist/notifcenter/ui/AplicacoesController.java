package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@RequestMapping("/aplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.aplicacoes")
public class AplicacoesController {

    @RequestMapping
    public String home(Model model) {
        model.addAttribute("world", "aplicacoes");
        return "notifcenter/home";
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
