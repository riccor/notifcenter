package pt.utl.ist.notifcenter.ui;

import com.google.common.base.Strings;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.CanalResource;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.api.json.CanalAdapter;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RequestMapping("/canais")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.canais")
public class CanaisController {

    @SkipCSRF
    @RequestMapping ///(value = "/canais") //, method = RequestMethod.POST)
    public String canais(Model model, HttpServletRequest request){

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkBennuManagersGroupPermissions(user);

        //System.out.println("tipo: " + request.getParameter());

        if (!Strings.isNullOrEmpty(request.getParameter("createChannel"))) {
            Canal c = CanalAdapter.create2(HTTPClient.getHttpServletRequestParamsAsJson(request));
            model.addAttribute("changesNotification", "Channel " + c.getClass().getSimpleName() + " with id " + c.getExternalId() + " was successfully created!");
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteChannel"))) {
            String id = request.getParameter("deleteChannel");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Canal.class, id))) {
                model.addAttribute("changesNotification", "Channel " + UtilsResource.getDomainObject(Canal.class, id).getClass().getSimpleName() + " with id " + id + " was successfully deleted.");
                UtilsResource.getDomainObject(Canal.class, id).delete();
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editChannel"))) {
            String id = request.getParameter("editChannel");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Canal.class, id))) {
                Canal c = CanalAdapter.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), UtilsResource.getDomainObject(Canal.class, id));
                model.addAttribute("changesNotification", "Channel " + c.getClass().getSimpleName() + " with id " + c.getExternalId() + " was successfully updated!");
            }
        }

        model.addAttribute("canais", getExistingChannels());
        model.addAttribute("classes_canais", CanalResource.getAvailableChannelsNamesAndParams());

        return "notifcenter/canais";
    }

    //Returns a list of hashmaps with channels names and respective params
    public static List<HashMap<String, String>> getExistingChannels() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Canal c : SistemaNotificacoes.getInstance().getCanaisSet()) {

            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", c.getExternalId());
            map.put("type", c.getClass().getSimpleName());
            map.put("config", c.getConfig());
            list.add(map);
        }

        return list;
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
