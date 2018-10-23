package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public enum ErrorsAndWarnings {

    INVALID_APP_ERROR ("invalidApp", "Invalid application ID."),
    INVALID_APPNAME_ERROR ("applicationNameAlreadyRegistered", "Such application name is already registered.");

    //add new errors/warnings above this line
    //INVALID_ACCESS_TOKEN_ERROR ("invalidAccessToken", "Invalid access token.")

    private final String codeId;
    private final String details;

    ErrorsAndWarnings(String codeId, String details) {
        this.codeId = codeId;
        this.details = details;
    }

    public JsonElement toJson() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("error", this.codeId);
        jObj.addProperty("error_description", this.details);
        return jObj;
    }

}


