/*
*   Note: Mattermost channel was not implemented yet.
*/

package pt.utl.ist.notifcenter.domain;

import javax.servlet.http.HttpServletRequest;

public class Mattermost extends Mattermost_Base {
    
    public Mattermost() {
        super();
    }
    
    @Override
    public void sendMessage(Mensagem msg) {

    }

    //This method is invoked when a message delivery status is received from a channel via HTTP
    @Override
    public UserMessageDeliveryStatus dealWithMessageDeliveryStatusNotificationsFromChannel(HttpServletRequest request) {

        return null;
    }

}
