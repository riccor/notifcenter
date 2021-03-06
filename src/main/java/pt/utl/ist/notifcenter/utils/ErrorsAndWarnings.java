/*
    Used to deal with thrown exceptions due to errors and warnings
*/

package pt.utl.ist.notifcenter.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;

public enum ErrorsAndWarnings {

    INVALID_ASSUNTO_ERROR ("invalidMessageAssunto", "Invalid message param: assunto", HttpStatus.PRECONDITION_FAILED),
    INVALID_TEXTO_CURTO_ERROR ("invalidMessageTextoCurto", "Invalid message param: textoCurto", HttpStatus.PRECONDITION_FAILED),
    INVALID_TEXTO_LONGO_ERROR ("invalidMessageTextoLongo", "Invalid message param: textoLongo", HttpStatus.PRECONDITION_FAILED),
    NOTALLOWED_VIEW_PAGE_ERROR ("notAllowedPage", "No permissions to view this page.", HttpStatus.FORBIDDEN),
    INVALID_JSON_ERROR ("invalidJson", "Invalid JSON data.", HttpStatus.PRECONDITION_FAILED),
    INVALID_DATETIME_ERROR ("invalidDatetime", "Invalid datetime. Valid pattern is dd.MM.yyyy HH:mm:ss.SSS", HttpStatus.PRECONDITION_FAILED),
    INTERNAL_SERVER_ERROR ("internalServerError", "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_APP_ERROR ("invalidApp", "Invalid application ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_APPNAME_ERROR ("applicationNameAlreadyInUse", "Such application name cannot be used.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CHANNEL_ERROR ("invalidCanal", "Invalid channel ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CHANNEL_NAME_ERROR ("invalidCanalName", "Invalid channel name.", HttpStatus.PRECONDITION_FAILED),
    INVALID_REMETENTE_ERROR ("invalidRemetente", "Invalid sender ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CONTACT_ERROR ("invalidContacto", "Invalid contacto ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_USER_ERROR ("invalidUser", "Invalid user ID.", HttpStatus.PRECONDITION_FAILED),
    PLEASE_LOG_IN ("logInRequired", "You need to log in to access this content.", HttpStatus.FORBIDDEN),
    INVALID_GROUP_ERROR ("invalidGroup", "Invalid group ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CANALNOTIFICACAO_ERROR ("invalidCanalNotificacao", "Invalid notification channel.", HttpStatus.PRECONDITION_FAILED),
    INVALID_MESSAGE_ERROR ("invalidMessage", "Invalid message.", HttpStatus.PRECONDITION_FAILED),
    INVALID_MESSAGE_ATTACHMENT_SIZE_ERROR ("invalidMessageAttachmentSize", "Invalid message attachment size.", HttpStatus.PRECONDITION_FAILED),
    NOTALLOWED_CANALNOTIFICACAO_ERROR ("notAllowedCanalNotificacao", "No permissions to use such notification channel.", HttpStatus.FORBIDDEN),
    NOTALLOWED_REMETENTE_ERROR ("notAllowedRemetente", "No permissions to use such notification channel.", HttpStatus.FORBIDDEN),
    NOTALLOWED_GROUP_ERROR ("notAllowedGroup", "No permissions to send messages to group.", HttpStatus.FORBIDDEN),
    BLOCKED_APP_ERROR ("blockedApp", "Application is blocked.", HttpStatus.FORBIDDEN),
    NOTALLOWED_TO_ADD_GROUP_ERROR ("notAllowedToAddGroup", "No permissions to add message receiving groups.", HttpStatus.FORBIDDEN),
    COULD_NOT_DELIVER_MESSAGE ("couldNotDeliverMessage", "Could not deliver message.", HttpStatus.REQUEST_TIMEOUT),
    INVALID_ATTACHMENT_ERROR ("attachmentNotFound", "No such attachment was found.", HttpStatus.NOT_FOUND),
    INVALID_ENTITY_ERROR ("cannotCreateEntity", "Invalid entity parameters.", HttpStatus.PRECONDITION_FAILED),
    NOTALLOWED_VIEW_MESSAGE_ERROR ("notAllowedToViewMessage", "No permissions to view such message.", HttpStatus.FORBIDDEN),
    NOTALLOWED_VIEW_ATTACHMENT_ERROR ("notAllowedToViewAttachment", "No permissions to view such attachment.", HttpStatus.FORBIDDEN),
    SUCCESS_THANKS ("none", "Thank you.", HttpStatus.OK),
    SUCCESS ("none", "definable", HttpStatus.OK),
    MISSING_PARAMETER_ERROR ("missingParameter", "Missing parameter.", HttpStatus.PRECONDITION_FAILED),
    UNKNOWN_MESSAGE_ID ("unknownMessageId", "Unknown message ID.", HttpStatus.NOT_FOUND),
    ALREADY_EXISTING_RESOURCE ("resourceAlreadyExists", "Resource already exists.", HttpStatus.CONFLICT),
    ALREADY_EXISTING_RELATION_ERROR ("relationAlreadyExists", "Such relation already exists.", HttpStatus.CONFLICT),
    ALREADY_EXISTING_CONTACT_ERROR ("contactAlreadyExists", "Contact already exists.", HttpStatus.FORBIDDEN),
    ALREADY_EXISTING_PERMISSIONS_ERROR ("permissionsAlreadyExist", "Such permissions already exist.", HttpStatus.CONFLICT),
    NON_EXISTING_PERMISSIONS_ERROR ("nonExistingPermissions", "Such permissions don't exist.", HttpStatus.CONFLICT),
    NON_EXISTING_RELATION ("nonExistingRelation", "Such relation does not exist.", HttpStatus.CONFLICT);

    private final String codeId;
    private final String errorDescription;
    private final HttpStatus httpStatus;

    ErrorsAndWarnings(String codeId, String errorDescription, HttpStatus httpStatus) {
        this.codeId = codeId;
        this.errorDescription = errorDescription;
        this.httpStatus = httpStatus;
    }

    public String getCodeId() {
        return codeId;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public JsonElement toJson() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("error", this.codeId);
        jObj.addProperty("errorDescription", this.errorDescription);
        return jObj;
    }

    public JsonElement toJsonWithDetails(String details) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("error", this.codeId);
        jObj.addProperty("errorDescription", this.errorDescription);
        jObj.addProperty("details", details);
        return jObj;
    }

    public String toHTML() {
        String str = "<br><b>HTTP Status " + this.httpStatus + "</b></br>";
        str = str + "<br>error: " + this.codeId + "</br>";
        str = str + "<br>errorDescription: " + this.errorDescription + "</br>";
        return str;
    }

    public String toHTMLWithDetails(String details) {
        String str = "<br><b>HTTP Status " + this.httpStatus + "</b></br>";
        str = str + "<br>error: " + this.codeId + "</br>";
        str = str + "<br>errorDescription: " + this.errorDescription + "</br>";
        str = str + "<br>details: " + details + "</br>";
        return str;
    }
}
