package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

import java.util.HashMap;

public class Twilio extends Twilio_Base {

    public Twilio() {
        super();
        //this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    @Atomic
    public static Twilio createTwilio(final String accountSID, final String authToken) {
        Twilio twilio = new Twilio();
        twilio.setAccountSID(accountSID);
        twilio.setAuthToken(authToken);

        //Debug
        twilio.setEmail("twilio-" + twilio.getExternalId() + "@notifcenter.com");

        return twilio;
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

    /*
    public String toString() {
        return "Twilio";
    }*/
    public HashMap<String, String> getParams() {
        return new HashMap<String, String>();
    }

}

