package pt.utl.ist.notifcenter.domain;

//import org.fenixedu.bennu.core.domain.exceptions.BennuCoreDomainException;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;

public class NotifcenterException extends RuntimeException {

    /*final
    private String[] args;
    private String bundle;
    private HttpStatus status;
    */

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

    /*
    protected NotifcenterException(HttpStatus status, String bundle, String key, String... args) {
        super();
        this.status = status;
        this.bundle = bundle;
        this.key = key;
        this.args = args;
    }
    */
    //throw NotifcenterException.resourceNotFound();


    /*
    public NotifcenterException(Response.Status status, String key, String... args) {
        super(status, key, args);
    }

    public static BennuCoreDomainException resourceNotFound(String id) {
        return new BennuCoreDomainException(HttpStatus.NOT_FOUND, "e", id);
    }
    */

}