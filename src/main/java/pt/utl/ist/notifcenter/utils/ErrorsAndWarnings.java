package pt.utl.ist.notifcenter.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;

public enum ErrorsAndWarnings {

    INVALID_JSON_ERROR ("invalidJson", "Invalid JSON data.", HttpStatus.PRECONDITION_FAILED),
    INVALID_DATETIME_ERROR ("invalidDatetime", "Invalid datetime. Valid pattern is dd.MM.yyyy HH:mm:ss.SSS", HttpStatus.PRECONDITION_FAILED),
    INTERNAL_SERVER_ERROR ("internalServerError", "Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_APP_ERROR ("invalidApp", "Invalid application ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_APPNAME_ERROR ("applicationNameAlreadyInUse", "Such application name cannot be used.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CHANNEL_ERROR ("invalidCanal", "Invalid channel ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CHANNEL_NAME_ERROR ("invalidCanalName", "Invalid channel name.", HttpStatus.PRECONDITION_FAILED),
    INVALID_REMETENTE_ERROR ("invalidRemetente", "Invalid remetente ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CONTACT_ERROR ("invalidContacto", "Invalid contacto ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_USER_ERROR ("invalidUser", "Invalid user ID.", HttpStatus.PRECONDITION_FAILED),
    PLEASE_LOG_IN ("logInRequired", "You need to log in to access this content.", HttpStatus.FORBIDDEN),
    INVALID_GROUP_ERROR ("invalidGroup", "Invalid group ID.", HttpStatus.PRECONDITION_FAILED),
    INVALID_CANALNOTIFICACAO_ERROR ("invalidCanalNotificacao", "Invalid notification channel.", HttpStatus.PRECONDITION_FAILED),
    INVALID_MESSAGE_ERROR ("invalidMessage", "Invalid message.", HttpStatus.PRECONDITION_FAILED),
    NOTALLOWED_CANALNOTIFICACAO_ERROR ("notAllowedCanalNotificacao", "No permissions to use such notification channel.", HttpStatus.FORBIDDEN),
    NOTALLOWED_GROUP_ERROR ("notAllowedGroup", "No permissions to send messages to group.", HttpStatus.FORBIDDEN),
    COULD_NOT_DELIVER_MESSAGE ("couldNotDeliverMessage", "Could not deliver message.", HttpStatus.REQUEST_TIMEOUT),
    INVALID_ATTACHMENT_ERROR ("attachmentNotFound", "No such attachment was found.", HttpStatus.NOT_FOUND),
    INVALID_ENTITY_ERROR ("cannotCreateEntity", "Invalid entity parameters.", HttpStatus.PRECONDITION_FAILED),
    NOTALLOWED_VIEWMESSAGE_ERROR ("notAllowedToViewMessage", "No permissions to view such message.", HttpStatus.FORBIDDEN),
    SUCCESS_THANKS ("none", "Thank you.", HttpStatus.OK),
    MISSING_PARAMETER_ERROR ("missingParameter", "Missing parameter.", HttpStatus.PRECONDITION_FAILED),
    UNKNOWN_MESSAGE_SID ("unknownMessageSid", "Unknown message SID.", HttpStatus.NOT_FOUND),
    ALREADY_EXISTING_RESOURCE ("resourceAlreadyExists", "Resource already exists.", HttpStatus.CONFLICT),
    ALREADY_EXISTING_RELATION ("relationAlreadyExists", "Such relation already exists.", HttpStatus.CONFLICT),
    NON_EXISTING_RELATION ("nonExistingRelation", "Such relation does not exist.", HttpStatus.CONFLICT);


    //add new errors/warnings above this line
    //INVALID_ACCESS_TOKEN_ERROR ("invalidAccessToken", "Invalid access token.")

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
        jObj.addProperty("error_description", this.errorDescription);
        return jObj;
    }

    public JsonElement toJsonWithDetails(String details) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("error", this.codeId);
        jObj.addProperty("error_description", this.errorDescription);
        jObj.addProperty("details", details);
        return jObj;
    }

}
