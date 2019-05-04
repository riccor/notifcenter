package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Canal extends Canal_Base {

    public static Map<Class<?>, CanalProvider> CHANNELS = Collections.synchronizedMap(new HashMap<>());

    public static class CanalProvider {
        private String configExample;
        private Function<String, Canal> contructor;

        public String getConfigExample() {
            return configExample;
        }

        public JsonObject getConfigExampleAsJson() {
            return new JsonParser().parse(configExample).getAsJsonObject();
        }

        public Function<String, Canal> getContructor() {
            return contructor;
        }

            public CanalProvider(final String configExample, Function<String, Canal> contructor) {
            this.configExample = configExample;
            this.contructor = contructor;
        }
    }

    @Atomic
    public static Canal createChannel(Class<?> clazz, String toString) {
        final CanalProvider provider = Canal.CHANNELS.get(clazz);
        if (provider == null) { //if no such channel type, then throw exception
            throw new NotifcenterException(ErrorsAndWarnings.INVALID_CHANNEL_NAME_ERROR);
        }

        return provider.getContructor().apply(toString);
    }

    @Override
    @Atomic
    public void setConfig(final String config) { //equivalent to "updateChannel"
        JsonObject jObj = new JsonParser().parse(config).getAsJsonObject();

        //check if all necessary channel params are given
        CHANNELS.get(this.getClass()).getConfigExampleAsJson().entrySet().forEach(e -> {
            UtilsResource.getRequiredValue(jObj, e.getKey());
        });

        super.setConfig(config);
    }

    public JsonObject getConfigAsJson() {
        return new JsonParser().parse(this.getConfig()).getAsJsonObject();
    }

    public Canal() {
        super();
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    //This method must send a message to a group via this channel
    public abstract void sendMessage(Mensagem msg);

    //This method is invoked when a message delivery status is received from a channel via HTTP
    public abstract UserMessageDeliveryStatus dealWithMessageDeliveryStatusNotificationsFromChannel(HttpServletRequest request);

    //IMPORTANT: the following method can only be called from inside sendMessage()
    //This method:
    // - Collects all contacts from message recipient users;
    // - prevents duplicated messages sent for the same user;
    // - automatically create UserMessageDeliveryStatus entities for users who don't have contacts for the message channel
    public ArrayList<Contacto> getContactsFromMessageRecipientUsers(Mensagem msg){

        ArrayList<Contacto> contacts = new ArrayList<>();

        //Get all user contacts for this channel
        for (PersistentGroup group : msg.getGruposDestinatariosSet()) {
            group.getMembers().forEach(user -> {

                //Debug
                ///System.out.println("LOG: user: " + user.getUsername() + " with email: " + user.getEmail());

                boolean userHasNoContactForThisChannel = true;

                //prevent duplicated message for same user:
                if (user.getUserMessageDeliveryStatusSet().stream().anyMatch(e -> e.getMensagem().equals(msg))) {
                    System.out.println("DEBUG: Prevented duplicated message for user " + user.getUsername());
                    userHasNoContactForThisChannel = false;
                }
                else {
                    for (Contacto contacto : user.getContactosSet()) {
                        if (contacto.getCanal().equals(this)) {

                            //Debug
                            //System.out.println("has dadosContacto " + contacto.getDadosContacto());

                            userHasNoContactForThisChannel = false;

                            contacts.add(contacto);

                            break; //no need to search more contacts for this user on this channel.
                        }
                    }
                }

                if (userHasNoContactForThisChannel) {
                    System.out.println("WARNING: user " + user.getUsername() + " has no contact for " + this.getClass().getSimpleName());
                    UserMessageDeliveryStatus edm = UserMessageDeliveryStatus.createUserMessageDeliveryStatus(msg, user, "userHasNoContactForSuchChannel", "userHasNoContactForSuchChannel");
                }

            });
        }

        return contacts;
    }

    @Atomic
    public void delete() {
        for (CanalNotificacao cn : this.getCanalNotificacaoSet()) {
            cn.delete();
        }

        for (Contacto c : this.getContactoSet()) {
            c.delete();
        }

        this.getSistemaNotificacoes().removeCanais(this);
        this.setSistemaNotificacoes(null);

        this.deleteDomainObject();
    }

}
