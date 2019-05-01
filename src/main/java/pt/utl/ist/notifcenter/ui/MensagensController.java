package pt.utl.ist.notifcenter.ui;

import com.google.common.base.Strings;
import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/mensagens")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.ui.mensagens")
public class MensagensController {

    @SkipCSRF
    @RequestMapping
    public String mensagens(Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            //throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkNotifcenterAdminsGroupPermissions(user);

        if (!Strings.isNullOrEmpty(request.getParameter("deleteMensagem"))) {
            String id = request.getParameter("deleteMensagem");
            if (FenixFramework.isDomainObjectValid(UtilsResource.getDomainObject(Mensagem.class, id))) {
                UtilsResource.getDomainObject(Mensagem.class, id).delete();
            }
        }

        model.addAttribute("messages", getExistingMensagens());

        return "notifcenter/messages";
    }


    public List<HashMap<String, String>> getExistingMensagens() {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (Canal c: SistemaNotificacoes.getInstance().getCanaisSet()) {
            for (CanalNotificacao cn : c.getCanalNotificacaoSet()) {
                for (Mensagem m : cn.getMensagemSet()) {
                    list.add(MessageToHashMap(m));
                }
            }
        }

        return list;
    }

    public static List<HashMap<String, String>> getExistingUserMensagens(User user) {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (UserMessageDeliveryStatus umds: user.getUserMessageDeliveryStatusSet()) {
            list.add(UserMessageToHashMap(umds.getMensagem()));
        }

        return list;
    }

    public static HashMap<String, String> MessageToHashMap(Mensagem m){
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", m.getExternalId());
        map.put("canalnotificacao", m.getCanalNotificacao().getExternalId());
        map.put("remetente", m.getCanalNotificacao().getRemetente().getExternalId());
        map.put("gruposDestinatarios", m.getGruposDestinatariosSet().stream().map(PersistentGroup::getExternalId).collect(Collectors.joining(",")));
        map.put("assunto", m.getAssunto());
        map.put("textoCurto", m.getTextoCurto());
        map.put("textoLongo", m.getTextoLongo());
        map.put("dataEntrega", m.getDataEntrega().toString("dd.MM.yyyy HH:mm:ss.SSS"));
        map.put("callbackUrlEstadoEntrega", m.getCallbackUrlEstadoEntrega());
        map.put("attachments", m.getAttachmentsSet().stream().map(Attachment::getExternalId).collect(Collectors.joining(",")));
        //map.put("link", NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/mensagens/" + m.getExternalId());
        return map;
    }

    public static HashMap<String, String> UserMessageToHashMap(Mensagem m){
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("id", m.getExternalId());
        map.put("remetente", m.getCanalNotificacao().getRemetente().getNome());
        map.put("assunto", m.getAssunto());
        map.put("textoCurto", m.getTextoCurto());
        map.put("textoLongo", m.getTextoLongo());
        map.put("attachments", m.getAttachmentsSet().stream().map(Attachment::getExternalId).collect(Collectors.joining(",")));
        //map.put("link", NotifcenterSpringConfiguration.getConfiguration().notifcenterUrl() + "/mensagens/" + m.getExternalId());
        return map;
    }

    //Message delivery status
    @RequestMapping("/{msg}/deliverystatuses")
    public String mensagemDeliveryStatus(@PathVariable("msg") Mensagem msg, Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            //throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);
        UtilsResource.checkNotifcenterAdminsGroupPermissions(user);

        model.addAttribute("message", msg);
        model.addAttribute("deliverystatuses", getExistingDeliveryStatusesFromMessage(msg));

        return "notifcenter/deliverystatuses";
    }

    public List<HashMap<String, String>> getExistingDeliveryStatusesFromMessage(Mensagem msg) {

        List<HashMap<String, String>> list = new ArrayList<>();

        for (UserMessageDeliveryStatus e : msg.getUserMessageDeliveryStatusSet()) {
            HashMap<String, String> map = new LinkedHashMap<>();
            map.put("id", e.getExternalId());
            map.put("channel", e.getMensagem().getCanalNotificacao().getCanal().getExternalId()); //UML SIMPLIFIED
            //map.put("contact", e.getContacto().getExternalId());
            map.put("user", e.getUtilizador().getExternalId());
            map.put("externalId", e.getIdExterno());
            map.put("deliveryStatus", e.getEstadoEntrega());

            list.add(map);
        }

        return list;
    }

    //View message
    @RequestMapping("/{msg}")
    public String mensagem(@PathVariable("msg") Mensagem msg, Model model, HttpServletRequest request) {

        if (!UtilsResource.isUserLoggedIn()) {
            //throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            return "redirect:/login?callback=" + request.getRequestURL();
        }

        if (!FenixFramework.isDomainObjectValid(msg)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_MESSAGE_ERROR);
        }

        User user = UtilsResource.getAuthenticatedUser();
        UtilsResource.checkIsUserValid(user);

        if (!msg.isAccessible(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_MESSAGE_ERROR);
        }

        model.addAttribute("message", msg);
        model.addAttribute("attachments_links", getUserFriendlyMessageAttachments(msg));

        return "notifcenter/view-message";
    }

    private HashMap<String, String> getUserFriendlyMessageAttachments(Mensagem msg) {
        HashMap<String, String> attachmentsLinks = new LinkedHashMap<>();
        for (Attachment at : msg.getAttachmentsSet()) {
            attachmentsLinks.put(at.getDisplayName(), "http://" + NotifcenterSpringConfiguration.getConfiguration().notifcenterUrlForAttachments() + at.getExternalId());
        }
        return attachmentsLinks;
    }

    @RequestMapping(value = "/attachments/{fileId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> downloadAttachment(@PathVariable("fileId") Attachment attachment) {

        if (!UtilsResource.isUserLoggedIn()) {
            throw new NotifcenterException(ErrorsAndWarnings.PLEASE_LOG_IN);
            ///return "redirect:/login?callback=" + request.getRequestURL();
        }

        User user = UtilsResource.getAuthenticatedUser();

        if (user == null || !FenixFramework.isDomainObjectValid(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_USER_ERROR);
        }

        if (!FenixFramework.isDomainObjectValid(attachment)) {
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_ATTACHMENT_ERROR);
        }

        if (!attachment.isAccessible(user)) {
            throw new NotifcenterException(ErrorsAndWarnings.NOTALLOWED_VIEW_ATTACHMENT_ERROR);
        }

        /* Debug
        System.out.println("#############content key: "+ attachment.getContentKey());
        System.out.println("#############checksum algorithm: "+ attachment.getChecksumAlgorithm());
        System.out.println("#############checksum: "+ attachment.getChecksum());
        */

        byte[] fileContent = attachment.getContent();

        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", attachment.getContentType());
        header.add("Content-Disposition", "attachment; filename=" + attachment.getDisplayName().replace(" ", "_"));
        header.add("Content-Length", String.valueOf(fileContent.length));

        return new HttpEntity<>(fileContent, header);
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
