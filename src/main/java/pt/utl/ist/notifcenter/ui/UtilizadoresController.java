package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@RequestMapping("/utilizadores")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.utilizadores")
public class UtilizadoresController {



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


