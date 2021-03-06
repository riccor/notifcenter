/*
    Used to deal with thrown exceptions due to errors and warnings
*/

package pt.utl.ist.notifcenter.utils;

public class NotifcenterException extends RuntimeException {

    private ErrorsAndWarnings errorsAndWarnings;
    private String moreDetails;

    public ErrorsAndWarnings getErrorsAndWarnings() {
        return errorsAndWarnings;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public NotifcenterException(ErrorsAndWarnings errorsAndWarnings) {
        this.errorsAndWarnings = errorsAndWarnings;
        this.moreDetails = null;
    }

    public NotifcenterException(ErrorsAndWarnings errorsAndWarnings, String moreDetails) {
        this.errorsAndWarnings = errorsAndWarnings;
        this.moreDetails = moreDetails;
    }

}

