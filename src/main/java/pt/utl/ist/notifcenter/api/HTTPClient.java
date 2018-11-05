package pt.utl.ist.notifcenter.api;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HTTPClient {

    public static String base64Encode(String token) {
        return new String(Base64.getEncoder().encode(token.getBytes()));
    }

    public static String base64Decode(String encodedToken) {
        return new String(Base64.getDecoder().decode(encodedToken), StandardCharsets.UTF_8);
    }

    public static String createBasicAuthString(String username, String password){
        return String.format("Basic %s", base64Encode(username + ":" + password));
    }

    public static HttpHeaders createBasicAuthHeader(String username, String password){
        return new HttpHeaders() {{
            String auth = createBasicAuthString(username, password);
            set("Authorization", auth);
        }};
    }


    //SYNC client

    public static ResponseEntity<String> restSyncClientHeaders(final HttpMethod method,
                                                              final String uri,
                                                              final HttpHeaders headers,
                                                              final MultiValueMap<String, String> bodyParameters) {
        ///HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.setAccept(Arrays.asList(headerAcceptParameters));

        ///HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headers);
        //instead of:
        //HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);
        //String result = restTemplate.getForObject(uri, String.class);
        //System.out.println(result);

        //JsonParser parser = new JsonParser();
        //JsonObject jObj = parser.parse(response.getBody()).getAsJsonObject();
        //return jObj;

        return response;
    }


    //SYNC client

    public static ResponseEntity<String> restSyncClient(final HttpMethod method,
                                              final String uri,
                                              final MultiValueMap<String, String> headerParameters,
                                              final MultiValueMap<String, String> bodyParameters) {
        ///HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.setAccept(Arrays.asList(headerAcceptParameters));

        ///HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);
        //String result = restTemplate.getForObject(uri, String.class);
        //System.out.println(result);

        //JsonParser parser = new JsonParser();
        //JsonObject jObj = parser.parse(response.getBody()).getAsJsonObject();
        //return jObj;

        return response;
    }


    //ASYNC client

    public static void restASyncClient(final HttpMethod method,
                                        final String uri,
                                        final MultiValueMap<String, String> headerParameters,
                                        final MultiValueMap<String, String> bodyParameters,
                                        DeferredResult<ResponseEntity<String>> deferredResult) {
        ///HttpHeaders headers = new HttpHeaders();
        //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //headers.setAccept(Arrays.asList(headerAcceptParameters));

        ///HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        AsyncRestTemplate restTemplate = new AsyncRestTemplate();
        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.exchange(uri, method, entity, String.class);
        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                deferredResult.setResult(result);
            }

            @Override
            public void onFailure(Throwable ex) {
                deferredResult.setErrorResult(ex);
            }
        });
    }

    public static void printResponseEntity(ResponseEntity<String> response) {
        System.out.println(" ");
        System.out.println("RESPONSE ENTITY:");
        System.out.println("status code: " + response.getStatusCode());
        System.out.println("header: " + response.getHeaders());
        System.out.println("body: " + response.getBody());
        System.out.println(" ");
    }

}


/*

//ASYNC client 2

private static void restASyncClient2(final HttpMethod method,
                                     final String uri,
                                     final MultiValueMap<String, String> headerParameters,
                                     final MultiValueMap<String, String> bodyParameters,
                                     final String callbackURL,
                                     final AsyncResponseHandler asyncResponseHandler) {
    ///HttpHeaders headers = new HttpHeaders();
    //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    //headers.setAccept(Arrays.asList(headerAcceptParameters));

    ///HttpEntity<String> entity = new HttpEntity<String>(bodyParameters, headers);
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

    AsyncRestTemplate restTemplate = new AsyncRestTemplate();
    ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.exchange(uri, method, entity, String.class);
    futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
        @Override
        public void onSuccess(ResponseEntity<String> result) {
            //System.out.println(" ");
            //System.out.println("GOT A RESPONSE:");
            //System.out.println("response status code: " + result.getStatusCode());
            //System.out.println("response header: " + result.getHeaders());
            //System.out.println("response body: " + result.getBody());
            //System.out.println(" ");
            asyncResponseHandler.setResponseEntity(result);
        }

        @Override
        public void onFailure(Throwable ex) {
            asyncResponseHandler.setError(ex.getMessage());
            System.out.println("erro no onFailure(): " + ex.getMessage());
        }
    });
}


//AsyncResponseHandler.java (para o cliente restASyncClient2):

package pt.utl.ist.notifcenter.api;

import org.springframework.http.ResponseEntity;

public class AsyncResponseHandler {

    public void setResponseEntity(ResponseEntity<String> responseEntity) {
        this.OnAsyncResponseSuccess(responseEntity);
    }

    public void setError(String error) {
        this.OnAsyncResponseFailure(error);
    }

    public void OnAsyncResponseSuccess(ResponseEntity<String> result){

    }

    public void OnAsyncResponseFailure(Object error){

    }
}

*/
