package pt.utl.ist.notifcenter.domain;

import java.util.HashMap;

public class Email extends Email_Base {
    
    public Email() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

    /*
    public String toString() {
        return "Email";
    }*/

    public HashMap<String, String> getParams() {
        return new HashMap<String, String>();
    }

}
