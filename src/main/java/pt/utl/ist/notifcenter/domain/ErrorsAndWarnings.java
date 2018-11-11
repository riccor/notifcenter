package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum ErrorsAndWarnings {

    INVALID_APP_ERROR ("invalidApp", "Invalid application ID."),
    INVALID_APPNAME_ERROR ("applicationNameAlreadyRegistered", "Such application name is already registered."),
    INVALID_CHANNEL_ERROR ("invalidCanal", "Invalid channel ID."),
    INVALID_REMETENTE_ERROR ("invalidRemetente", "Invalid remetente ID."),
    INVALID_USER_ERROR ("invalidUser", "Invalid user ID."),
    INVALID_GROUP_ERROR ("invalidGroup", "Invalid group ID."),
    INVALID_CANALNOTIFICACAO_ERROR ("invalidCanalNotificacao", "Invalid notification channel."),
    INVALID_MESSAGE_ERROR ("invalidMessage", "Invalid message."),
    NOTALLOWED_CANALNOTIFICACAO_ERROR ("notAllowedCanalNotificacao", "No permissions to use such notification channel."),
    COULD_NOT_DELIVER_MESSAGE ("couldNotDeliverMessage", "Could not deliver message.");

    //add new errors/warnings above this line
    //INVALID_ACCESS_TOKEN_ERROR ("invalidAccessToken", "Invalid access token.")

    private final String codeId;
    private final String errorDescription;

    ErrorsAndWarnings(String codeId, String errorDescription) {
        this.codeId = codeId;
        this.errorDescription = errorDescription;
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
