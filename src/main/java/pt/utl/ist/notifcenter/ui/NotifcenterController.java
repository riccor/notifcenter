package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/notifcenter")
@SpringApplication(group = "logged", path = "notifcenter", title = "title.Notifcenter")
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

}
