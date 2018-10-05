package pt.utl.ist.notifcenter.ui;

import org.fenixedu.bennu.spring.portal.SpringApplication;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/mytest")
@SpringApplication(group = "logged", path = "mytest", title = "myTest") // ../webapp/WEB-INF/mytest
@SpringFunctionality(app = MyTestController.class, title = "myTest Controller")
public class MyTestController {

    @RequestMapping
    public String home(Model model) {
        //model.addAttribute("world", "World");
        return "mytest/pagina";
    }

    /*
    @ResponseBody
    @RequestMapping(value = "/api/{appname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String ola(@PathVariable("appname") String appName) {
        return "nomi: " + appName;
    }
    */
}
