package pt.utl.ist.notifcenter.domain;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.User;
import pt.ist.fenixframework.Atomic;

public class UserMessageDeliveryStatus extends UserMessageDeliveryStatus_Base {

    private UserMessageDeliveryStatus(Canal canal, Mensagem msg, User utilizador) {
        super();
        this.setCanal(canal);
        this.setMensagem(msg);
        this.setUtilizador(utilizador);
    }

    @Atomic
    public static UserMessageDeliveryStatus createUserMessageDeliveryStatus(Canal canal, Mensagem msg, User utilizador, String idExterno, String estadoEntrega) {
        UserMessageDeliveryStatus UserMessageDeliveryStatus = new UserMessageDeliveryStatus(canal, msg, utilizador);
        UserMessageDeliveryStatus.setIdExterno(idExterno);
        UserMessageDeliveryStatus.setEstadoEntrega(estadoEntrega);
        return UserMessageDeliveryStatus;
    }

    @Atomic
    public void changeIdExterno(String idExterno) {
        this.setIdExterno(idExterno);
    }

    @Atomic
    public void changeEstadoEntrega(String estadoEntrega) {
        this.setEstadoEntrega(estadoEntrega);
    }

    @Atomic
    public void changeIdExternoAndEstadoEntrega(@Nullable String idExterno, @Nullable String estadoEntrega) {
        this.setIdExterno(idExterno);
        this.setEstadoEntrega(estadoEntrega);
    }

    @Atomic
    public void delete() {
        this.getCanal().removeUserMessageDeliveryStatus(this);
        this.setCanal(null); ///

        this.getMensagem().removeUserMessageDeliveryStatus(this);
        this.setMensagem(null); ///

        this.getUtilizador().removeUserMessageDeliveryStatus(this);
        this.setUtilizador(null);

        this.deleteDomainObject();
    }

}
