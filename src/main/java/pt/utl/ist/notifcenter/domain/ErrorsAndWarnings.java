package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;

public enum ErrorsAndWarnings {

    INVALID_APP_ERROR ("invalidApp", "Invalid application ID.", HttpStatus.NOT_FOUND),
    INVALID_APPNAME_ERROR ("applicationNameAlreadyRegistered", "Such application name is already registered.", HttpStatus.NOT_FOUND),
    INVALID_CHANNEL_ERROR ("invalidCanal", "Invalid channel ID.", HttpStatus.NOT_FOUND),
    INVALID_REMETENTE_ERROR ("invalidRemetente", "Invalid remetente ID.", HttpStatus.NOT_FOUND),
    INVALID_USER_ERROR ("invalidUser", "Invalid user ID.", HttpStatus.NOT_FOUND),
    INVALID_GROUP_ERROR ("invalidGroup", "Invalid group ID.", HttpStatus.NOT_FOUND),
    INVALID_CANALNOTIFICACAO_ERROR ("invalidCanalNotificacao", "Invalid notification channel.", HttpStatus.NOT_FOUND),
    INVALID_MESSAGE_ERROR ("invalidMessage", "Invalid message.", HttpStatus.NOT_FOUND),
    NOTALLOWED_CANALNOTIFICACAO_ERROR ("notAllowedCanalNotificacao", "No permissions to use such notification channel.", HttpStatus.FORBIDDEN),
    COULD_NOT_DELIVER_MESSAGE ("couldNotDeliverMessage", "Could not deliver message.", HttpStatus.REQUEST_TIMEOUT),
    INVALID_ATTACHMENT_ERROR ("attachmentNotFound", "No such attachment was found.", HttpStatus.NOT_FOUND);

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

    /*
    public JsonElement toJsonWithDetails(String details) {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("error", this.codeId);
        jObj.addProperty("error_description", this.errorDescription);
        jObj.addProperty("details", details);
        return jObj;
    }
    */

}
