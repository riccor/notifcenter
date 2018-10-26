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
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.avro.data.Json;
import org.codehaus.jackson.map.util.JSONPObject;
import org.fenixedu.bennu.core.rest.BennuRestResource;

//import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.core.security.SkipCSRF;
import org.fenixedu.bennu.oauth.annotation.OAuthEndpoint;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.notifcenter.api.json.AplicacaoAdapter;

import pt.utl.ist.notifcenter.api.json.ExemploIdentidadeAdapter;
import pt.utl.ist.notifcenter.api.json.RemetenteAdapter;
import pt.utl.ist.notifcenter.domain.*;
import pt.utl.ist.notifcenter.security.SkipAccessTokenValidation;
import pt.utl.ist.notifcenter.ui.NotifcenterController;

import org.fenixedu.bennu.core.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

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

    private static JsonElement restSyncClient(final HttpMethod method, final String uri, final String bodyParameters) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.setAccept(Arrays.asList(headerAcceptParameters));
        HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);
        //String result = restTemplate.getForObject(uri, String.class);
        //System.out.println(result);

        JsonParser parser = new JsonParser();
        JsonObject jObj = parser.parse(response.getBody()).getAsJsonObject();
        return jObj;
    }

    @RequestMapping(value = "viewaplicacaoclientsync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement viewAplicacaoClientSync(@RequestParam(value = "app", defaultValue = "281736969715746") Aplicacao app) {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/" + app.getExternalId() + "/?access_token=NTYz";

        return restSyncClient(HttpMethod.GET, uri, "none");
    }

    @RequestMapping(value = "addaplicacaoclientsync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonElement addAplicacaoClientSync(@RequestParam(value = "app") String app) {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/addaplicacao?name=" + app + "&redirect_uri=http://app7777_site.com/code&description=descricao_app7777";

        return restSyncClient(HttpMethod.POST, uri, "none");
    }




    @RequestMapping(value = "viewaplicacaoclientasync", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String viewAplicacaoClientASync(@RequestParam(value = "app", defaultValue = "281736969715746") Aplicacao app) /*throws Exception*/ {

        String uri = "http://localhost:8080/notifcenter/apiaplicacoes/oauth/viewaplicacao/" + app.getExternalId() + "/?access_token=NTYz";

        //CompletableFuture<JsonElement> comp1 =
        AsyncHTTPRequest.restASyncClient(HttpMethod.GET, uri, "none", HttpMethod.GET, app.getRedirectUrl());

        //CompletableFuture.allOf(comp1).join();
        //return comp1.get();

        return "ok!";
    }

    @GetMapping("/async1")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(Model model) {
        System.out.println("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            System.out.println("Processing in separate thread");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
            output.setResult(ResponseEntity.ok("ok"));
        });

        System.out.println("servlet thread freed");
        return output;
    }
    
    
    @RequestMapping(value = "async", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<ResponseEntity<?>> handleReqDefResult() {
        System.out.println("1. Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        //output.onCompletion(() -> System.out.println("4.2 Processing complete"));
        output.onTimeout(() -> output.setErrorResult(ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Request timeout occurred.")));

        //ForkJoinPool.commonPool().submit(() -> {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("3. Processing in separate thread");
            //int result = 0;
            try {
                //result = 5*5*5*5*5;
                Thread.sleep(3*1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ok!";
            //output.setResult(ResponseEntity.ok("4.1 end! here is the result: " + result));
        }).whenCompleteAsync((result, exc) -> output.setResult(ResponseEntity.ok(result)));

        System.out.println("2. servlet thread freed");
        return output;
    }


    //Notifcenter callback:

    @RequestMapping(value="/notifcentercallback")
    public String notifcenterCallback(HttpServletRequest request){
        List<String> parameterNames = new ArrayList<>(request.getParameterMap().keySet());
        for (String name : parameterNames) {
            System.out.println(name + "=" + request.getParameter(name));
        }
        return "param names: " + parameterNames.toString();
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

