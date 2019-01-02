package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

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


}

