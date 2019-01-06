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
import pt.utl.ist.notifcenter.api.AplicacaoResource;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.Aplicacao;
import pt.utl.ist.notifcenter.domain.AppPermissions;
import pt.utl.ist.notifcenter.domain.SistemaNotificacoes;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/aplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.aplicacoes")
public class AplicacoesController {

    @SkipCSRF
    @RequestMapping
    public String aplicacoes(Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkAdminPermissions(user);

        if (!Strings.isNullOrEmpty(request.getParameter("createApp"))) {
            Aplicacao newApp = AplicacaoResource.create2(HTTPClient.getHttpServletRequestParamsAsJson(request));

            //app permissions can only be changed here (not in create2())
            if (!Strings.isNullOrEmpty(request.getParameter("permissoes"))) {
                String permissions = request.getParameter("permissoes");

                if (AppPermissions.getAppPermissionsFromString(permissions) != null) {
                    newApp.setAppPermissions(AppPermissions.getAppPermissionsFromString(permissions));
                }
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteApp"))) {
            String id = request.getParameter("deleteApp");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Aplicacao.class, id))) {
                UtilsResource.getDomainObject(Aplicacao.class, id).delete();
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editApp"))) {
            String id = request.getParameter("editApp");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Aplicacao.class, id))) {
                AplicacaoResource.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), UtilsResource.getDomainObject(Aplicacao.class, id));

                //app permissions can only be changed here (not in update2())
                if (!Strings.isNullOrEmpty(request.getParameter("permissoes"))) {
                    String permissions = request.getParameter("permissoes");

                    if (AppPermissions.getAppPermissionsFromString(permissions) != null) {
                        UtilsResource.getDomainObject(Aplicacao.class, id).setAppPermissions(AppPermissions.getAppPermissionsFromString(permissions));
                    }
                }
            }
        }

        model.addAttribute("aplicacoes", getExistingApps());
        model.addAttribute("parametros_app", new String[]{"name", "redirect_uri", "description", "author", "site_url"});
        model.addAttribute("app_permissions_values", AppPermissions.values());

        return "notifcenter/aplicacoes";
    }

    public List<HashMap<String, String>> getExistingApps() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Aplicacao a : SistemaNotificacoes.getInstance().getAplicacoesSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", a.getExternalId());
            map.put("name", a.getName());
            map.put("author", a.getAuthorName());
            map.put("permissoes", a.getPermissoesAplicacao().name());
            map.put("description", a.getDescription());
            map.put("site_url", a.getSiteUrl());
            map.put("redirect_uri", a.getRedirectUrl());
            map.put("client_secret", a.getSecret());
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
