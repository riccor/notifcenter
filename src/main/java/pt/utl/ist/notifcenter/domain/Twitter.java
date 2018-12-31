package pt.utl.ist.notifcenter.domain;

import java.util.HashMap;

public class Twitter extends Twitter_Base {
    
    public Twitter() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

    /*public String toString() {
        return "Twitter";
    }*/

    public HashMap<String, String> getParams() {
        return new HashMap<String, String>();
    }

}
