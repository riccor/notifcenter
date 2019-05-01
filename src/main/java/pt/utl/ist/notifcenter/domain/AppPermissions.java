package pt.utl.ist.notifcenter.domain;

public enum AppPermissions {
	/*
		NONE - API access for application is blocked
		RREQUIRES_APPROVAL - application requests to add sender, recipient groups permissions and notification channels require administration approval
		ALLOW_ALL - all application requests are accepted immediately
	*/
	NONE, RREQUIRES_APPROVAL, ALLOW_ALL;

	public static AppPermissions getAppPermissionsFromString(String str) {
		for (AppPermissions ap : AppPermissions.values()) {
			if (ap.name().equalsIgnoreCase(str)) {
				return ap;
			}
		}
		return null;
	}

}
