package pt.utl.ist.notifcenter.ui;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.HTTPClient;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;
import pt.utl.ist.notifcenter.api.json.CanalNotificacaoAdapter;
import pt.utl.ist.notifcenter.api.json.RemetenteAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/aplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.aplicacoes")
public class AplicacoesController {

    @SkipCSRF
    @RequestMapping("/{app}/{remetente}/gruposdestinatarios")
    public String gruposDestinatarios(Model model, HttpServletRequest request,
                                    @PathVariable("app") Aplicacao app,
                                    @PathVariable("remetente") Remetente remetente) {

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkAdminPermissions(user);

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!Strings.isNullOrEmpty(request.getParameter("addGrupoDestinatario"))) {
            if (!Strings.isNullOrEmpty(request.getParameter("group"))) {
                PersistentGroup gd = UtilsResource.getDomainObject(PersistentGroup.class, request.getParameter("group"));

                //not needed
                /*if (remetente.getGruposSet().contains(gd)) {
                    throw new NotifcenterException(ErrorsAndWarnings.ALREADY_EXISTING_RELATION_ERROR);
                }*/
                remetente.addGroupToSendMesssages(gd);
            }
            else {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR);
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("removeGrupoDestinatario"))) {
            PersistentGroup gd = UtilsResource.getDomainObject(PersistentGroup.class, request.getParameter("removeGrupoDestinatario"));
            //not needed
            /*if (!remetente.getGruposSet().contains(gd)) {
                throw new NotifcenterException(ErrorsAndWarnings.INVALID_GROUP_ERROR);
            }*/
            remetente.removeGroupToSendMesssages(gd);
        }

        model.addAttribute("application", app);
        model.addAttribute("sender", remetente);
        model.addAttribute("gruposdestinatarios", getExistingGruposDestinatariosFromRemetente(remetente));
        //model.addAttribute("parametros_gruposdestinatarios", new String[]{});
        model.addAttribute("grupos", getExistingGruposDestinatarios());

        return "notifcenter/gruposdestinatarios";
    }

    public List<HashMap<String, String>> getExistingGruposDestinatariosFromRemetente(Remetente remetente) {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (PersistentGroup pg : remetente.getGruposSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", pg.getExternalId());
            map.put("name", pg.getPresentationName());
            map.put("members", pg.getMembers().map(e -> e.getUsername() + " (" + e.getExternalId() + ")").collect(Collectors.joining(",")));
            list.add(map);
        }

        return list;
    }

    public static List<HashMap<String, String>> getExistingGruposDestinatarios() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (PersistentGroup pg : FenixFramework.getDomainRoot().getBennu().getGroupSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", pg.getExternalId());
            map.put("name", pg.getPresentationName());
            map.put("members", pg.getMembers().map(e -> e.getUsername() + " (" + e.getExternalId() + ")").collect(Collectors.joining(",")));
            list.add(map);
        }

        return list;
    }

    @SkipCSRF
    @RequestMapping("/{app}/{remetente}/canaisnotificacao")
    public String canaisNotificacao(Model model, HttpServletRequest request,
                                    @PathVariable("app") Aplicacao app,
                                    @PathVariable("remetente") Remetente remetente) {

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkAdminPermissions(user);

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(remetente) || !app.getRemetentesSet().contains(remetente)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
        }

        if (!Strings.isNullOrEmpty(request.getParameter("createCanalNotificacao"))) {
            JsonObject jsonObject = HTTPClient.getHttpServletRequestParamsAsJson(request, "app", "remetente"); //avoid hacks
            jsonObject.addProperty("app", app.getExternalId());
            jsonObject.addProperty("remetente", remetente.getExternalId());
            CanalNotificacaoAdapter.create2(jsonObject);
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteCanalNotificacao"))) {
            String id = request.getParameter("deleteCanalNotificacao");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(CanalNotificacao.class, id))) {
                if (!remetente.getCanaisNotificacaoSet().contains(UtilsResource.getDomainObject(CanalNotificacao.class, id))) {
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_CANALNOTIFICACAO_ERROR);
                }
                else {
                    UtilsResource.getDomainObject(CanalNotificacao.class, id).delete();
                }
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editCanalNotificacao"))) {
            String id = request.getParameter("editCanalNotificacao");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(CanalNotificacao.class, id))) {
                //approve/disapprove only here in admin panel
                if (!Strings.isNullOrEmpty(request.getParameter("aguardandoAprovacao"))) {
                    String aguardandoAprovacao = request.getParameter("aguardandoAprovacao");

                    if (aguardandoAprovacao.equalsIgnoreCase("true")) {
                        UtilsResource.getDomainObject(CanalNotificacao.class, id).approveCanalNotificacao();
                    }
                    else if (aguardandoAprovacao.equalsIgnoreCase("false")) {
                        UtilsResource.getDomainObject(CanalNotificacao.class, id).disapproveCanalNotificacao();
                    }
                }
            }
        }

        model.addAttribute("application", app);
        model.addAttribute("sender", remetente);
        model.addAttribute("canaisnotificacao", getExistingCanaisNotificacaoFromRemetente(remetente));
        ///model.addAttribute("parametros_canalnotificacao", new String[]{"canal"});
        model.addAttribute("canais", CanaisController.getExistingChannels());

        return "notifcenter/canaisnotificacao";
    }

    public List<HashMap<String, String>> getExistingCanaisNotificacaoFromRemetente(Remetente remetente) {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (CanalNotificacao cn : remetente.getCanaisNotificacaoSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", cn.getExternalId());
            map.put("channel", cn.getCanal().getClass().getSimpleName() + " (" + cn.getCanal().getExternalId() + ")");
            map.put("approved", cn.isApproved() ? "true" : "false");
            list.add(map);
        }

        return list;
    }

    @SkipCSRF
    @RequestMapping("/{app}")
    public String remetentes(Model model, HttpServletRequest request, @PathVariable("app") Aplicacao app) {

        if (!UtilsResource.isUserLoggedIn()) {
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkAdminPermissions(user);

        if (!FenixFramework.isDomainObjectValid(app)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_APP_ERROR);
        }

        if (!Strings.isNullOrEmpty(request.getParameter("createRemetente"))) {
            JsonObject jsonObject = HTTPClient.getHttpServletRequestParamsAsJson(request, "app"); //avoid hacks
            jsonObject.addProperty("app", app.getExternalId());
            RemetenteAdapter.create2(jsonObject);
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("deleteRemetente"))) {
            String id = request.getParameter("deleteRemetente");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Remetente.class, id))) {
                if (!app.getRemetentesSet().contains(UtilsResource.getDomainObject(Remetente.class, id))) {
                    throw new NotifcenterException(ErrorsAndWarnings.INVALID_REMETENTE_ERROR);
                }
                else {
                    UtilsResource.getDomainObject(Remetente.class, id).delete();
                }
            }
        }
        else if (!Strings.isNullOrEmpty(request.getParameter("editRemetente"))) {
            String id = request.getParameter("editRemetente");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Remetente.class, id))) {
                RemetenteAdapter.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), UtilsResource.getDomainObject(Remetente.class, id));
            }
        }

        model.addAttribute("application", app);
        model.addAttribute("remetentes", getExistingAppRemetentes(app));
        model.addAttribute("parametros_remetente", new String[]{"name"});

        return "notifcenter/remetentes";
    }

    public List<HashMap<String, String>> getExistingAppRemetentes(Aplicacao app) {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Remetente r : app.getRemetentesSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", r.getExternalId());
            map.put("name", r.getNome());
            list.add(map);
        }

        return list;
    }

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
            Aplicacao newApp = AplicacaoAdapter.create2(HTTPClient.getHttpServletRequestParamsAsJson(request));

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
                AplicacaoAdapter.update2(HTTPClient.getHttpServletRequestParamsAsJson(request), UtilsResource.getDomainObject(Aplicacao.class, id));

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
