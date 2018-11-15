package pt.utl.ist.notifcenter.domain;

//import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;

public class NotifcenterException extends RuntimeException {

    private ErrorsAndWarnings errorsAndWarnings;
    private String moreDetails;

    public ErrorsAndWarnings getErrorsAndWarnings() {
        return errorsAndWarnings;
    }

    public NotifcenterException(ErrorsAndWarnings errorsAndWarnings) {
        this.errorsAndWarnings = errorsAndWarnings;
    }

    public NotifcenterException(ErrorsAndWarnings errorsAndWarnings, String moreDetails) {
        this.errorsAndWarnings = errorsAndWarnings;
        this.moreDetails = moreDetails;
    }

}
