package pt.utl.ist.notifcenter.domain;

import java.util.HashMap;

public class Mattermost extends Mattermost_Base {
    
    public Mattermost() {
        super();
    }

    @Override
    public void sendMessage(Mensagem msg) {

    }

    public HashMap<String, String> getParams() {
        return new HashMap<String, String>();
    }

}
