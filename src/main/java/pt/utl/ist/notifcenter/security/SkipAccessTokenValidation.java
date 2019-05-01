/*
    This annotation may be used in API methods in order to disable OAuth authentication (NotifcenterInterceptor must be enabled!)
*/

package pt.utl.ist.notifcenter.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkipAccessTokenValidation {

}
