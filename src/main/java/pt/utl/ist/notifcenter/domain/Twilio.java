package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Twilio extends Twilio_Base {

    private Twilio(SistemaNotificacoes sistemaNotificacoes) {
        super();
        this.setSistemaNotificacoes(sistemaNotificacoes);
    }

    @Atomic
    public static Twilio createTwilio(SistemaNotificacoes sistemaNotificacoes, final String accountSID, final String authToken) {
        Twilio twilio = new Twilio(sistemaNotificacoes);
        twilio.setAccountSID(accountSID);
        twilio.setAuthToken(authToken);

        //Debug
        twilio.setEmail("example3@defaultemail.com");

        return twilio;
    }

    public static Twilio createTwilioFromPropertiesFile(SistemaNotificacoes sistemaNotificacoes, final String file) {
        Map<String, String> propertiesMap = new ConcurrentHashMap<>();
        propertiesMap.put("accountSID", "null");
        propertiesMap.put("authToken", "null");

        String filename = String.format(NotifcenterSpringConfiguration.getConfiguration().notifcenterChannelsCredentials(), file);
        LoadPropertiesFromFile(Twilio.class, filename, propertiesMap);

        if (!IsMapFilled(propertiesMap)) {
            System.out.println("Error: Cannot create entity from file.");
            return null;
        }

        return createTwilio(sistemaNotificacoes, propertiesMap.get("accountSID"), propertiesMap.get("authToken"));
    }

    public static boolean IsMapFilled(Map<String, String> propertiesMap) {

        for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {

            if (entry.getValue().equals("null")) {
                return false;
            }

            //System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue() + ";");
        }

        return true;
    }


    public static <T> void LoadPropertiesFromFile(Class<T> clazz, final String filename, Map<String, String> propertiesMap) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            //clazz.getResourceAsStream() - procura o resource no mesmo diretorio do ficheiro .class
            //clazz.getClassLoader().getResourceAsStream() - procura no CLASSPATH
            input = clazz.getClassLoader().getResourceAsStream(filename);

            //System.out.println("clazz.getClassLoader().getResource(filename): " + clazz.getClassLoader().getResource(filename));

            if (input == null) {
                System.out.println("Error: Unable to find file " + filename + "!");
                return;
            }

            // load a properties file
            prop.load(input);

            // get the property values
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                if (!entry.getKey().isEmpty()) {
                    //System.out.println("key: " + entry.getKey());
                    propertiesMap.put(entry.getKey(), prop.getProperty(entry.getKey()));
                }
            }
        } catch (NullPointerException | IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //SEND WHATSAPP MESSAGE:
    public ResponseEntity<String> sendWhatsAppSMSOriginal(final String to, final String from, final String message) {

        //String uri = "https://api.twilio.com/2010-04-01/Accounts/" + this.getAccountSID() + "/Messages.json";

        ///String uri = "http://localhost:8080/notifcenter/apiaplicacoes/notifcentercallback";

        String uri = "http://localhost:8000/myapp/post";


        ///MultiValueMap<String, String> header = new LinkedMultiValueMap<>(); //
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpHeaders header = HTTPClient.createAuthHeader(this.getAccountSID(), this.getAuthToken());
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        body.put("To", Arrays.asList(to));
        body.put("From", Arrays.asList(from));
        body.put("Body", Arrays.asList(message));

        /*
        try {
            ///header.put("Content-Type", Arrays.asList("application/x-www-form-urlencoded"));
            //header.put("Authorization", Arrays.asList("Basic QUM2Y2JiZDdkNmViMjZkOGRjMzRmY2U0NGE0YmVhOGExYzoyM2M1ZjhiNzUxZWJmZjgxNjNhYTBjNTZmMWVhZGU1Yg=="));
            //header.put("Authorization", Arrays.asList("Basic " + HTTPClient.base64Encode(this.getAccountSID() + ":" + this.getAuthToken())));

            //body.put("To", Arrays.asList(URLEncoder.encode(to, "UTF-8")));
            //body.put("From", Arrays.asList(URLEncoder.encode(from, "UTF-8")));
            //body.put("Body", Arrays.asList(URLEncoder.encode(message, "UTF-8")));

        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
        */

        return HTTPClient.restSyncClientHeaders(HttpMethod.POST, uri, header, body);
        ///return HTTPClient.restSyncClient(HttpMethod.POST, uri, header, body);
    }


    public ResponseEntity<String> sendWhatsAppSMS(final String to, final String from, final String message) {

        //String uri = "https://api.twilio.com/2010-04-01/Accounts/" + this.getAccountSID() + "/Messages.json";

        //String uri = "http://localhost:8080/notifcenter/apiaplicacoes/notifcentercallback";
        String uri = "https://trollspt.club";

    final MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.put("to",Arrays.asList(to)); //Collections.singletonList

    final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.put("from",Arrays.asList(from)); //Collections.singletonList

        //DA ERRO AO METER "POST"!!
        return HTTPClient.restSyncClient(HttpMethod.POST,uri,header,body);
    }
}
