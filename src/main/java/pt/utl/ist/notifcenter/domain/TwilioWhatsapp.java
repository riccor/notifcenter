package pt.utl.ist.notifcenter.domain;

import org.fenixedu.bennu.NotifcenterSpringConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.HTTPClient;

import java.util.Arrays;
import java.util.Map;

public class TwilioWhatsapp extends TwilioWhatsapp_Base implements InterfaceDeCanal{
    
    private TwilioWhatsapp() {
        super();
        //this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static TwilioWhatsapp createTwilioWhatsApp(final String accountSID, final String authToken, final String fromPhoneNumber, final String uri) {
        TwilioWhatsapp twilioWhatsapp = new TwilioWhatsapp();
        twilioWhatsapp.setAccountSID(accountSID);
        twilioWhatsapp.setAuthToken(authToken);
        twilioWhatsapp.setFromPhoneNumber(fromPhoneNumber);
        twilioWhatsapp.setUri(uri);

        //Debug
        twilioWhatsapp.setEmail("twiliowhatsapp@notifcenter.com");

        return twilioWhatsapp;
    }

    public static TwilioWhatsapp createTwilioWhatsappFromPropertiesFile(final String file) {
        String filename = String.format(NotifcenterSpringConfiguration.getConfiguration().notifcenterChannelsCredentials(), file);
        Map<String, String> propertiesMap = Utils.loadPropertiesFromPropertiesFile(TwilioWhatsapp.class, filename, "accountSID", "authToken", "fromPhoneNumber", "uri");

        if (!Utils.isMapFilled(propertiesMap)) {
            System.out.println("Error: Cannot create entity from file.");
            return null;
        }

        return createTwilioWhatsApp(propertiesMap.get("accountSID"), propertiesMap.get("authToken"), propertiesMap.get("fromPhoneNumber"), propertiesMap.get("uri"));
    }

    public ResponseEntity<String> sendMessage(final String to, final String message){

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        ///HttpHeaders header = HTTPClient.createBasicAuthHeader(this.getAccountSID(), this.getAuthToken());
        ///header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        header.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.add("Authorization", HTTPClient.createBasicAuthString(this.getAccountSID(), this.getAuthToken()));

        body.put("To", Arrays.asList(to));
        body.put("From", Arrays.asList(this.getFromPhoneNumber()));
        body.put("Body", Arrays.asList(message));

        return HTTPClient.restSyncClient(HttpMethod.POST, this.getUri(), header, body);
    }

}
