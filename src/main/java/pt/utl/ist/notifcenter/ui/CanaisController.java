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
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
import pt.utl.ist.notifcenter.utils.NotifcenterException;
import pt.utl.ist.notifcenter.utils.Utils;

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
        UtilsResource.checkAdminPermissions(user);

        //System.out.println("tipo: " + request.getParameter());

        if (!Strings.isNullOrEmpty(request.getParameter("createChannel"))) {
            CanalAdapter.create2(HTTPClient.getHttpServletRequestParamsAsJson(request));
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteChannel"))) {
            String id = request.getParameter("deleteChannel");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Canal.class, id))) {
                UtilsResource.getDomainObject(Canal.class, id).delete();
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editChannel"))) {
            String id = request.getParameter("editChannel");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Canal.class, id))) {
                CanalAdapter.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), UtilsResource.getDomainObject(Canal.class, id));
            }
        }

        model.addAttribute("canais", getExistingChannels());
        model.addAttribute("classes_canais", CanalResource.getAvailableChannelsNamesAndParams());

        return "notifcenter/canais";
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

    //returns a list of hashmaps with channels names and respective params
    public static List<HashMap<String, String>> getExistingChannels() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Canal c : SistemaNotificacoes.getInstance().getCanaisSet()) {

            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", c.getExternalId());
            map.put("type", c.getClass().getSimpleName());
            map.put("email", c.getEmail());

            try {
                //AnotacaoCanal annotation = c.getClass().getAnnotation(AnotacaoCanal.class);
                //for (String key : annotation.classFields()) {
                for (String key : Utils.getDomainClassSlots(c.getClass())) {
                    String methodName = "get" + Utils.capitalizeFirstLetter(key);
                    String value = (String) c.getClass().getMethod(methodName).invoke(c); //são sempre strings
                    map.put(key, value);
                }
            }
            catch (Exception e) {
                System.out.println("error on getting a channel class param");
            }

            list.add(map);
        }

        return list;
    }

}
