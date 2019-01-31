package pt.utl.ist.notifcenter.domain;

//import org.springframework.http.ResponseEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pt.ist.fenixframework.Atomic;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Canal extends Canal_Base {

    @Atomic
    public static void createNewCanal(Class<?> clazz, String toString) {
        final CanalProvider provider = Canal.CHHANNELS.get(clazz);
        if (entrada == null) { //if no such channel type, then throw exception
            throw Expcetion();
        }

        final Canal canal = provider.getContructor().apply(toString);

    }

    @Override
    public void setConfig(final String config) { //NOTA: equivalente a updateChannel()!! (?)
        super.setConfig();
        CHHANNELS.get(this.getClass()).getConfigExampleJson().entrySet()
                .forEach(); //TODO: cruzar tabela (ou exemplo json) com parametros recebidos
        config()
    }

    @atomic
    setConfigUpdate

    protected abstract String[] configFields();

    public static class CanalProvider {

        private String configExample;
        private Function<String, Canal> contructor;

        public String getConfigExample() {
            return configExample;
        }
        public JsonObject getConfigExampleJson() {
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

    public static Map<Class, CanalProvider> CHHANNELS = Collections.synchronizedMap(new HashMap<>());

    public Canal() {
        super();
        this.setSistemaNotificacoes(SistemaNotificacoes.getInstance());
    }

    ///
    /*
    @Atomic
    public static Canal createChannel(final String email, final String password) {
        Canal canal = new Canal();
        canal.setEmail(email);
        canal.setPassword(password);
        return canal;
    }
    */

    protected JsonObject config() {
        return new JsonParser().parse(getConfig());
    }

    public abstract void sendMessage(Mensagem msg); //{ System.out.println("\n\nshould not see this"); }

    public abstract void checkIsMessageAdequateForChannel(Mensagem msg);

    public abstract UserMessageDeliveryStatus dealWithMessageDeliveryStatusCallback(HttpServletRequest request);

    //TODO: NEM TODOS OS CANAIS TÊM URL (EXEMPLO: ENTRAR MENSAGEM POR CORREIO)
//    public abstract String getUri();

    @Atomic
    public void delete() {
        for (CanalNotificacao cn : this.getCanalNotificacaoSet()) {
            cn.delete();
        }

        for (Contacto c : this.getContactoSet()) {
            c.delete();
        }

        for (UserMessageDeliveryStatus e : this.getUserMessageDeliveryStatusSet()) {
            e.delete();
        }

        this.getSistemaNotificacoes().removeCanais(this);
        this.setSistemaNotificacoes(null);

        this.deleteDomainObject();
    }

}
