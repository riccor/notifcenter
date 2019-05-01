/*
    Used to deal with thrown exceptions due to errors and warnings
*/

package pt.utl.ist.notifcenter.utils;

public class AnotherNotifcenterException extends RuntimeException {

    private ErrorsAndWarnings errorsAndWarnings;
    private String moreDetails;

    public ErrorsAndWarnings getErrorsAndWarnings() {
        return errorsAndWarnings;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public AnotherNotifcenterException(ErrorsAndWarnings errorsAndWarnings) {
        this.errorsAndWarnings = errorsAndWarnings;
        this.moreDetails = null;
    }

    public AnotherNotifcenterException(ErrorsAndWarnings errorsAndWarnings, String moreDetails) {
        this.errorsAndWarnings = errorsAndWarnings;
        this.moreDetails = moreDetails;
    }

}
