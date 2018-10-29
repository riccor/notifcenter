// Pedidos disponíveis:
//POST http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_77&redirect_uri=http://app77_site.com/code&description=descricao_app77
//GET http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715746?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//POST http://localhost:8080/notifcenter/oauth/refresh_token?client_id=281736969715746&client_secret=HaEPQ/D6JhIUltRl4MiEvhKIQR52cJuOhQHlCey0ZC/uX8le/LftpRkN9M/4SjslzO6RqRyrYS03QifOLFY%2BsA==&refresh_token=NTYzMTYwNDA2ODE4ODIwOjY3OGI2MTRhOGViYWI2ZDQyYzljZDEwYWJlYTdmNzM4OWMyZTZkN2U5MTgyNjlkODFmMzk1N2QxNWIzMjhlMDM4MWNmZWZlNDBjY2U0M2I1ZWE5ZDNlNmI1Yjc4YWY3NmU5OWQ3MjU0YjIwYjRkNDE3YTVmZDFiNzQ4ODM3YWNk&grant_type=refresh_token
//POST http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/addremetente?name=ric&access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi
//GET http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/listremetentes?access_token=NTYzMTYwNDA2ODE4ODIwOjYwNWJiYTg4OGViMTAwYzdmMTc3ZjQ1OWVlZmM3MjE2NmMyZGY4MGNiOGVlNDk4NDI0Mzc0MmNhMzZiYTk0YmY0MDRkMGI3MDYzYzAzMzE2NTJjYzRhZDRmMzI1NzUyZDUyNzk1MjQ5YzdkNWNhZWMyZTI3MDQ2NTUxMzc1Mjdi

package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.rest.BennuRestResource;

//import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.api.json.ExemploIdentidadeAdapter;
import pt.utl.ist.notifcenter.api.json.RemetenteAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/apiaplicacoes")
@SpringFunctionality(app = NotifcenterController.class, title = "title.Notifcenter.api")
public class AplicacaoResource extends BennuRestResource {

    /*
    Numa API REST, neste caso o resource é aplicação, e queremos adicionar
    remetentes a essa aplicação. Para mim faria mais sentido ter algo do género:

       /adicionarAplicacao
       /listarAplicacoes
       /{app}
       /{app}/update
       /{app}/listarRemetentes
       /{app}/adicionarRemetente
       /{app}/{remetente}
       /{app}/{remetente}/update
       /{app}/{remetente}/remover

    O que fazer: orientar a API ao recurso.
    */


    //Adicionar aplicacao

    //ver cd ./notifcenter/bennu-5.2.1/bennu-spring/src/main/java/org/fenixedu/bennu/spring/security //CSRFToken token = new CSRFToken("awd");
    //exemplo de pedido: http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=app_2&redirect_uri=http://app2_site.com/codedescription=descricao_app2
    //@SkipAccessTokenValidation //diz ao método preHandler em "NotifcenterInterceptor.java" para aceitar pedidos sem access_token
    @SkipCSRF ///INDIFERENTE USAR ISTO SE USAR O MEU INTERCEPTOR
    @RequestMapping(value = "/oauth/addaplicacao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addAplicacao(@RequestParam(value="description") String description,
                               @RequestParam(value="name") String name, 
                               @RequestParam(value="redirect_uri") String redirectUrl, 
                               @RequestParam(value="author", defaultValue = "none") String authorName, 
                               @RequestParam(value="site_url", defaultValue = "none") String siteUrl) {

        if (Aplicacao.findByAplicacaoName(name) != null) {
           return ErrorsAndWarnings.INVALID_APPNAME_ERROR.toJson().toString();
        }

        Aplicacao app = Aplicacao.createAplicacao(name, redirectUrl, description, authorName, siteUrl);
        return view(app, AplicacaoAdapter.class).toString();
    }

    //exemplo GET: http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/281736969715746?access_token=
    @RequestMapping(value = "/oauth/viewaplicacao/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacao(@PathVariable("app") Aplicacao app) {

        if (app == null || !FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson().toString();
        }

        return view(app, AplicacaoAdapter.class).toString();
    }

    @RequestMapping(value = "/oauth/viewaplicacaodelayed/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacaoDelayed(@PathVariable("app") Aplicacao app) {

        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException e) { }

        if (app == null || !FenixFramework.isDomainObjectValid(app)) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson().toString();
        }

        return view(app, AplicacaoAdapter.class).toString();
    }




    // Adicionar remetente

    //exemplo pedido POST: http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/addremetente?name=pessoa2&access_token=
    @SkipCSRF ///INDIFERENTE USAR ISTO SE USAR O MEU INTERCEPTOR
    @RequestMapping(value = "/{app}/addremetente", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addRemetente(@PathVariable("app") Aplicacao app, @RequestParam(value="name") String nomeRemetente) {

        if (app == null) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        Remetente remetente = Remetente.createRemetente(app, nomeRemetente);
        return view(remetente, RemetenteAdapter.class);
    }
    
    //exemplo pedido GET: http://localhost:8080/notifcenter/apiaplicacoes/281736969715746/listremetentes?access_token=
    @RequestMapping(value = "/{app}/listremetentes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement listRemetentes(@PathVariable("app") Aplicacao app) {

        if (app == null) {
            return ErrorsAndWarnings.INVALID_APP_ERROR.toJson();
        }

        JsonObject jObj = new JsonObject();
        jObj.addProperty("aplicacao", app.getName());

        String names = "";
        for (Remetente remetente : app.getRemetentesSet()) {
            names = names + ", " + remetente.getNome();
        }
        jObj.addProperty("remetentes", names);

        return jObj;
    }


    //SYNC client

    private static JsonElement restSyncClient(final HttpMethod method,
                                              final String uri,
                                              final MultiValueMap<String, String> headerParameters,
                                              final MultiValueMap<String, String> bodyParameters) {
        RestTemplate restTemplate = new RestTemplate();
        ///HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.setAccept(Arrays.asList(headerAcceptParameters));

        ///HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);
        //String result = restTemplate.getForObject(uri, String.class);
        //System.out.println(result);

        JsonParser parser = new JsonParser();
        JsonObject jObj = parser.parse(response.getBody()).getAsJsonObject();

        return jObj;
    }

    @RequestMapping(value = "viewaplicacaoclientsync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewAplicacaoClientSync(@RequestParam(value = "app", defaultValue = "281736969715746") Aplicacao app) {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacaodelayed/" + app.getExternalId() + "/?access_token=NTYz";

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("headerParam1", Arrays.asList("headerParamValue1")); //Collections.singletonList

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("bodyParam1", Arrays.asList("bodyParamValue1")); //Collections.singletonList

        return restSyncClient(HttpMethod.GET, uri, header, body);
    }


    //ASYNC client

    private static void restASyncClient(final HttpMethod method,
                                        final String uri,
                                        final MultiValueMap<String, String> headerParameters,
                                        final MultiValueMap<String, String> bodyParameters,
                                        final String callbackURL) {
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        ///HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.setAccept(Arrays.asList(headerAcceptParameters));

        ///HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.exchange(uri, method, entity, String.class);

        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                System.out.println(" ");
                System.out.println("FINALLY GOT A RESPONSE:");
                System.out.println("response status code: " + result.getStatusCode());
                System.out.println("response header: " + result.getHeaders());
                System.out.println("response body: " + result.getBody());
                System.out.println(" ");

                /////////////////////////////////////////////////////////////////////////
                //think about this soon:
                final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
                header.put("header fwd", Arrays.asList(result.getHeaders().toString())); //Collections.singletonList

                final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.put("body fwd", Arrays.asList(result.getBody())); //Collections.singletonList

                //"generate some code" simulation:
                String queryParams = "?code=1223456789";

                System.out.println(restSyncClient(HttpMethod.GET, callbackURL + queryParams, header, body).toString());

                /////////////////////////////////////////////////////////////////////////

                //ou fazer algo diferente aqui conforme o pretendido!
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("erro no onFailure(): " + ex.getMessage());
            }
        });
    }

    @RequestMapping(value = "viewaplicacaoclientasync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewAplicacaoClientASync(@RequestParam(value = "app", defaultValue = "281736969715746") Aplicacao app) {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacaodelayed/" + app.getExternalId() + "/?access_token=NTYz";
        String callbackURL = "http://localhost:8080/notifcenter/apiaplicacoes/notifcentercallback";

        final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("headerParam1", Arrays.asList("headerParamValue1")); //Collections.singletonList

        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("bodyParam1", Arrays.asList("bodyParamValue1")); //Collections.singletonList

        restASyncClient(HttpMethod.GET, uri, header, body, callbackURL);

        JsonObject jObj = new JsonObject();
        jObj.addProperty("info", "waiting/processing answer...");
        return jObj;
    }


    //Notifcenter callback:

    @RequestMapping(value = "notifcentercallback", produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement notifcenterCallback(HttpServletRequest request) {
        List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
        JsonObject jObj = new JsonObject();
        jObj.addProperty("response", "elements are these:");

        for (String name : parameterNames) {
            jObj.addProperty(name, request.getParameter(name));
            //System.out.println(name + "=" + request.getParameter(name));
        }

        return jObj;
    }


    // IGNORAR (são apenas testes):

    @OAuthEndpoint("scope2")
    @RequestMapping(value = "test4", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test4() {
        String t4 = "test4";
        return t4;
    }

    @OAuthEndpoint("scope3")
    @RequestMapping(value = "test5", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test5() {
        String t4 = "test5";
        return t4;
    }

    @RequestMapping(value = "/oauth/viewaplicacao2/{app}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> viewAplicacao2(@PathVariable("app") Aplicacao app) {
        return new ResponseEntity<String>(view(app, AplicacaoAdapter.class).toString(), HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{app}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement updateAplic(@PathVariable("app") Aplicacao app, JsonElement json) {
        return updateApp(app, json);
    }

    protected JsonElement updateApp(Aplicacao app, JsonElement json) {
        app = update(json, app);
        return view(app, AplicacaoAdapter.class);
    }



    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="oi!") String name) {
        ///return new Greeting(1234, name);
        return new Greeting();
    }

    @RequestMapping(value = "test1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test1() {
        JsonObject jObj = new JsonObject();
        jObj.addProperty("campo1", "valor1");
        return jObj;
    }

    @RequestMapping(value = "test7", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement test7(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return view(ExemploIdentidade.createExemploIdentidade(name), ExemploIdentidadeAdapter.class);
    }

    @RequestMapping(value = "test8", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ExemploIdentidade test8(@RequestParam(value="name", defaultValue="exemplo de param1") String name) {
        return ExemploIdentidade.createExemploIdentidade(name);
    }

    @RequestMapping(value = "test9", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test9() {
        User user = User.findByUsername("admin");
        if(user != null)
            return "username '" + user.getName() + "' exists!";
        else
            return "non-existing user name";
    }


}

//core -> CSRFApiProtectionFilter nao chamado #1 #2 #3
//spring -> CSRFInterceptor nao chamado #7 #8 #9 //POR CAUSA DE ESTAR OVERRIDDEN PELO MEU INTERCEPTOR
//core -> CSRFFeature nao chamado (e.g. test4 nao tem a anotacao @skipCSRF e devia ser chamado) #5
//oauth -> BennuOAuthFeature

    /*
    @RequestMapping(value = "notifcentercallback2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String notifcenterCallback2(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        return "ok! Here: " + Arrays.toString(map.entrySet().toArray());
    }*/


    /*
    @RequestMapping(value = "/restURL")
    public String serveRest(@RequestBody String body, @RequestHeader HttpHeaders headers) {
        etc...
    }*/

    /*
    @ResponseBody
    @RequestMapping(value = urlPattern , method = RequestMethod.POST)
    public Test addNewWorker(@RequestBody Test jsonString) {
        //do business logic
        return test;
    }
    @RequestBody -Covert Json object to java
    @ResponseBody - convert Java object to json
    */

    /*
    Spring will use the same HttpMessageConverter objects to convert the User object as it
    does with @ResponseBody, except now you have more control over the status code and headers
    you want to return in the response.
    @RequestMapping(value = "/user?${id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = ...;
        if (user != null) {
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    */

