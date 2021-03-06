/*
    Synchronous and asynchronous web client methods and other utilities
*/

package pt.utl.ist.notifcenter.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import pt.utl.ist.notifcenter.utils.Utils;

public class HTTPClient {

    //Taken from http://optimumbrew.com/blog/2015/03/oauth/how-to-generate-oauth-signature-for-twitter-in-core-java :
    public static String percentEncode(String value) {
        String encoded = "";

        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String sb = "";
        char focus;

        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            }
            else if (focus == '+') {
                sb += "%20";
            }
            else if (focus == '%' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            }
            else {
                sb += focus;
            }
        }
        return sb.toString();
    }

    public static String bytesBase64Encode(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static void printResponseEntity(ResponseEntity<String> response) {
        System.out.println(" ");
        System.out.println(Utils.WHITE + "HTTP RESPONSE:");
        System.out.println("status code: " + response.getStatusCode());
        System.out.println("header: " + response.getHeaders());
        System.out.println("body: " + response.getBody());
        System.out.println(" ");
    }

    public static JsonElement stringToJson(String str) {
        return new JsonParser().parse(str);
    }

    public static String base64Encode(String token) {
        return new String(Base64.getEncoder().encode(token.getBytes()));
    }

    public static String base64Decode(String encodedToken) {
        return new String(Base64.getDecoder().decode(encodedToken), StandardCharsets.UTF_8);
    }

    public static String createBasicAuthString(String username, String password) {
        ///URL encode username and password first (according to RFC 1738):
        return String.format("Basic %s", base64Encode(tryUrlEncode(username) + ":" + tryUrlEncode(password)));
    }

    public static String tryUrlEncode(String value) {
        try {
            String encoded = URLEncoder.encode(value, "UTF-8");
            return encoded;
        }
        catch (Exception e) {
            //e.printStackTrace();
            return value;
        }
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

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                System.out.println(Utils.WHITE + "\nHTTP sync request status code: " + statusCode);
                return false;
            }
        });

        ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);

        return response;
    }

    //SYNC client
    public static ResponseEntity<String> restSyncClient(final HttpMethod method,
                                              final String uri,
                                              final MultiValueMap<String, String> headerParameters,
                                              final MultiValueMap<String, String> bodyParameters) {

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                System.out.println(Utils.WHITE + "\nHTTP sync request status code: " + statusCode);
                return false;
            }
        });
        
        ResponseEntity<String> response = restTemplate.exchange(uri, method, entity, String.class);

        return response;
    }

    //ASYNC client
    public static void restASyncClientMultiValueMap(final HttpMethod method,
                                        final String uri,
                                        final MultiValueMap<String, String> headerParameters,
                                        final MultiValueMap<String, String> bodyParameters,
                                        DeferredResult<ResponseEntity<String>> deferredResult) {

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(bodyParameters, headerParameters);

        AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                System.out.println(Utils.WHITE + "\nHTTP async request status code: " + statusCode);
                return false;
            }
        });
        
        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.exchange(uri, method, entity, String.class);
        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                deferredResult.setResult(result);
            }

            @Override
            public void onFailure(Throwable ex) {
                ///deferredResult.setErrorResult(ex);
                ResponseEntity<String> re = new ResponseEntity<>("error", new HttpHeaders() {{ this.set("error", "service is unavailable this moment"); }}, HttpStatus.SERVICE_UNAVAILABLE);
                deferredResult.setResult(re);
            }
        });
    }

    //ASYNC client JSON
    public static void restASyncClientJSON(final HttpMethod method,
                                       final String uri,
                                       final JsonObject json,
                                       DeferredResult<ResponseEntity<String>> deferredResult) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                System.out.println(Utils.WHITE + "\nHTTP async request status code: " + statusCode);
                return false;
            }
        });

        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.exchange(uri, method, entity, String.class);
        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                deferredResult.setResult(result);
            }

            @Override
            public void onFailure(Throwable ex) {
                ///deferredResult.setErrorResult(ex);
                ResponseEntity<String> re = new ResponseEntity<>("error", new HttpHeaders() {{ this.set("error", "service is unavailable this moment"); }}, HttpStatus.SERVICE_UNAVAILABLE);
                deferredResult.setResult(re);
            }
        });
    }

    public static void restASyncClient(final HttpMethod method,
                                           final String uri,
                                           final HttpHeaders headers,
                                           final String body,
                                           DeferredResult<ResponseEntity<String>> deferredResult) {

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            protected boolean hasError(HttpStatus statusCode) {
                System.out.println(Utils.WHITE + "\nHTTP async request status code: " + statusCode);
                return false;
            }
        });

        ListenableFuture<ResponseEntity<String>> futureEntity = restTemplate.exchange(uri, method, entity, String.class);
        futureEntity.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
            @Override
            public void onSuccess(ResponseEntity<String> result) {
                deferredResult.setResult(result);
            }

            @Override
            public void onFailure(Throwable ex) {
                ///deferredResult.setErrorResult(ex);
                ResponseEntity<String> re = new ResponseEntity<>("error", new HttpHeaders() {{ this.set("error", "service is unavailable this moment"); }}, HttpStatus.SERVICE_UNAVAILABLE);
                deferredResult.setResult(re);
            }
        });
    }
    
    public static JsonObject getHttpServletRequestParamsAsJson(HttpServletRequest request, String... fieldsToIgnore) {
        MultiValueMap<String, String> list = getHttpServletRequestParams(request);
        JsonObject jObj = new JsonObject();

        list.forEach((k, v) -> {

            if (Arrays.stream(fieldsToIgnore).noneMatch(e -> e.equals(k))) {

                JsonArray jA = new JsonArray();
                v.forEach(i -> {
                    jA.add(i);
                });

                jObj.add(k, jA);
            }
        });

        return jObj;
    }

    public static MultiValueMap<String, String> getHttpServletRequestParams(HttpServletRequest request) {

        MultiValueMap<String, String> list = new LinkedMultiValueMap<>();

        //header
        Enumeration<String> headerParams = request.getHeaderNames();
        while (headerParams.hasMoreElements()) {
            String headerParam = headerParams.nextElement();
            list.add(headerParam, request.getHeader(headerParam));
        }

        //body
        Enumeration<String> bodyParams = request.getParameterNames();
        while (bodyParams.hasMoreElements()) {
            String bodyParam = bodyParams.nextElement();
            list.add(bodyParam, request.getParameter(bodyParam));
        }

        return list;
    }

}

