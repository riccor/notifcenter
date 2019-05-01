package pt.utl.ist.notifcenter.domain;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.notifcenter.api.UtilsResource;
import pt.utl.ist.notifcenter.utils.ErrorsAndWarnings;
import pt.utl.ist.notifcenter.utils.NotifcenterException;

import javax.servlet.http.HttpServletRequest;
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

    public abstract void sendMessage(Mensagem msg);

    //This method allows making restrictions on messages to be sent by a channel (e.g. forbidding message subject lengths greater than 100 characters)
    public abstract void checkIsMessageAdequateForChannel(Mensagem msg);

    public abstract UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request);

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
