package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
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

    ///RequestMapping("/")
    public String home(Model model) {
        //model.addAttribute("world", "World");
        return "mytest/pagina";
    }

    //TODO pagina para visualizar mensagens
    @RequestMapping(value = "/{msg}/view")
    public String messages(@PathVariable("msg") Mensagem msg, Model model) {

        model.addAttribute("world", "World");

        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        //HOW TO GET USER LOGEGD IN?!

        for (PersistentGroup g : msg.getGruposDestinatariosSet()) {
            g.getMembers().forEach(user -> {


                //BREAK!

            });
        }

        /*

         */

        return "mytest/messages";
    }


    /*
    @ResponseBody
    @RequestMapping(value = "/api/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ola(@PathVariable("appname") String appName) {
        return "nomi: " + appName;
    }
    */
}
