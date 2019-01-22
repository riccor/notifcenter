package pt.utl.ist.notifcenter.utils;

//import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;

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
