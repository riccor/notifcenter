package pt.utl.ist.notifcenter.security;

import com.google.common.base.Strings;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pt.utl.ist.notifcenter.domain.Aplicacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AccessTokenVerificationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String accessToken = request.getParameter("access_token");
        //System.out.println("request->access_token: " +  accessToken);

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String appId = (String) pathVariables.get("app");

        if (Strings.isNullOrEmpty(appId)) {
            System.out.println("app is null... doing nothing.");
        }
        else {

            //como obter um objeto app a partir do appId recebido?
            //Aplicacao app = getAplicacaoFromId(appId);

            if (Strings.isNullOrEmpty(accessToken) /*|| app.isValidAccessToken(accessToken)*/) {
                System.out.println("error! invalid access_token!");

                //redirecionar pedido para outro handler method
                request.getRequestDispatcher("/apiaplicacoes/invalidaccesstoken").forward(request, response);

                return false;
            }
        }

       /* try {
            accessToken = request.getParameter("access_token");
        } catch (NullPointerException e) {
            clientToken = "";
        }

        if (!clientToken.equals("12345")) {
            throw new InvalidClientException();
        }
        */

        return true;
    }
}
