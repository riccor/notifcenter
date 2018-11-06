package pt.utl.ist.notifcenter.domain;

import org.springframework.http.ResponseEntity;

public interface InterfaceDeCanal {

    ResponseEntity<String> sendMessage(final String to, final String message);

}
