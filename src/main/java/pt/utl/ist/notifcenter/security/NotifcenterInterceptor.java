/*
*   Due to incompatibilities between Spring and FenixEdu, this class was created to allow API access tokens to work
*   BUG: Website won't work properly while using this class
*/

package pt.utl.ist.notifcenter.security;

import com.google.common.base.Strings;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.oauth.domain.ApplicationUserSession;
import org.fenixedu.bennu.oauth.domain.ExternalApplication;
import org.fenixedu.bennu.oauth.domain.ExternalApplicationScope;
import org.fenixedu.bennu.oauth.util.OAuthUtils;
import org.fenixedu.bennu.spring.security.CSRFInterceptor;
import org.fenixedu.bennu.spring.security.CSRFTokenRepository;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class NotifcenterInterceptor implements HandlerInterceptor {

    private final static boolean isAccessTokenRequired = true; //debugging purposes

    //Imported from https://github.com/FenixEdu/bennu/blob/master/bennu-oauth/src/main/java/org/fenixedu/bennu/oauth/jaxrs/BennuOAuthAuthorizationFilter.java
    private Optional<ApplicationUserSession> extractUserSession(String accessToken) {
        if (Strings.isNullOrEmpty(accessToken)) {
            return Optional.empty();
        }
        try {
            String fullToken = new String(Base64.getDecoder().decode(accessToken), StandardCharsets.UTF_8);
            String[] accessTokenBuilder = fullToken.split(":");
            if (accessTokenBuilder.length != 2) {
                return Optional.empty();
            }

            return OAuthUtils.getDomainObject(accessTokenBuilder[0], ApplicationUserSession.class);
        } catch (IllegalArgumentException iea) {
            return Optional.empty();
        }
    }

    //Taken from https://github.com/FenixEdu/bennu/blob/master/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security/CSRFInterceptor.java
    private boolean doesHandlerHasAnnotation(Object handler, Class<? extends Annotation> clazz) {
        return ((HandlerMethod) handler).getMethod().isAnnotationPresent(clazz);
    }

    private String findToken(String token, HttpServletRequest request) {
        String value = request.getParameter(token);
        if (Strings.isNullOrEmpty(value)) {
            value = request.getHeader(token);
        }
        return value;
    }

    //Taken from https://github.com/FenixEdu/bennu/blob/master/bennu-oauth/src/main/java/org/fenixedu/bennu/oauth/jaxrs/BennuOAuthAuthorizationFilter.java
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ///super.preHandle(request, response, handler);

        if (isAccessTokenRequired && !doesHandlerHasAnnotation(handler, SkipAccessTokenValidation.class)) {
            String accessToken = findToken(OAuthUtils.ACCESS_TOKEN, request);

            //System.out.println("scopes: " + FenixFramework.getDomainRoot().getBennu().getScopesSet().toString());

            Optional<ApplicationUserSession> session = extractUserSession(accessToken);
            if (!session.isPresent()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN , "Access token not recognized or not present!");
                return false;
            }
            else {
                ApplicationUserSession appUserSession = session.get();
                ExternalApplication app = session.get().getApplicationUserAuthorization().getApplication();

                if (app.isDeleted()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Application was deleted!");
                    return false;
                }

                if (app.isBanned()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Application was banned!");
                    return false;
                }

                //Module scopes (not used)
                if (doesHandlerHasAnnotation(handler, OAuthEndpoint.class)) {
                    final OAuthEndpoint endpoint = ((HandlerMethod) handler).getMethod().getAnnotation(OAuthEndpoint.class);
                    Optional<ExternalApplicationScope> scope = ExternalApplicationScope.forKey(endpoint.value());

                    if (scope.isPresent()) {
                        if (!app.getScopesSet().contains(scope.get())) {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Application doesn't have scope permissions to this endpoint!");
                            return false;
                        }
                    }
                    else{
                        System.out.println("WARNING: Scope '" + endpoint.value() + "' on handler method '" + ((HandlerMethod) handler).getMethod().toString() + "' is not registered in Bennu!");
                    }
                }

                if (!appUserSession.matchesAccessToken(accessToken)) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Access token doesn't match!");
                    return false;
                }

                if (!appUserSession.isAccessTokenValid()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access token has expired! Use refresh token endpoint to generate a new one.");
                    return false;
                }

                User foundUser = appUserSession.getApplicationUserAuthorization().getUser();

                if (foundUser.isLoginExpired()) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access token not recognized!");
                    return false;
                }

                Authenticate.mock(foundUser, "OAuth Access Token");
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
