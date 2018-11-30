package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.domain.Mensagem;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

@RequestMapping("/mytest")
@SpringApplication(group = "logged", path = "mytest", title = "myTest") // ../webapp/WEB-INF/mytest
@SpringFunctionality(app = MyTestController.class, title = "myTest Controller")
public class MyTestController {

    private User getAuthenticatedUser() {
        return Authenticate.getUser();
    }

    @RequestMapping
    public String home(Model model) {
        //model.addAttribute("world", "World");
        return "mytest/pagina";
    }

    @RequestMapping(value = "/{msg}/view")
    public String messages(@PathVariable("msg") Mensagem msg, Model model) {

        model.addAttribute("world", "World");

        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        User user = getAuthenticatedUser();

        if (user == null || !FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        for (PersistentGroup g : msg.getGruposDestinatariosSet()) {
            if (g.isMember(user)) {

                model.addAttribute("message", msg);

                return "mytest/messages";
            }
        }

        throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEWMESSAGE_ERROR);
        //return "mytest/messages";
    }


    /*
    @ResponseBody
    @RequestMapping(value = "/api/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ola(@PathVariable("appname") String appName) {
        return "nomi: " + appName;
    }
    */
}
