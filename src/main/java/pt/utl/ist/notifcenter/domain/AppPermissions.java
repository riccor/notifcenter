package pt.utl.ist.notifcenter.domain;

public enum AppPermissions {

	NONE, RREQUIRES_APPROVAL, ALLOW_ALL;
//TODO - NONE = BLOQUEADA
//TODO - REQUIERES_APPROVAL	= requires approvate to create remetente, grupos_destinatarios, canais_notificacao (ver GUI e rest api)
    //NONE("None"), REQUIRES_APPROVAL("Requires approval"), ALLOW_ALL("Allow all")

	public static AppPermissions getAppPermissionsFromString(String str) {
		for (AppPermissions ap : AppPermissions.values()) {
			if (ap.name().equalsIgnoreCase(str)) {
				return ap;
			}
		}
		return null;
	}

}
