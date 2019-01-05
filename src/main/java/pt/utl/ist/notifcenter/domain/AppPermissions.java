package pt.utl.ist.notifcenter.domain;

public enum AppPermissions {

	NONE, RREQUIRES_APPROVAL, ALLOW_ALL;

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
