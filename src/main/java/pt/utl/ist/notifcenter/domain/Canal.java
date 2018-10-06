package pt.utl.ist.notifcenter.domain;

//import org.springframework.util.CollectionUtils;

public class Canal extends Canal_Base {

    public Canal() {
        super();
    }

    public static int getTotalContactos(Canal canal) {
        int totalContactos = 0;
        for (Contacto contac : canal.getContactoSet()) {
            totalContactos += 1;
        }
        return totalContactos;
    }

    /*
    public static void main(String [] args) {

        Canal canal = new Canal();
        Contacto contacto1 = new Contacto();
        contacto1.setDados_contacto("961033");
        Contacto contacto2 = new Contacto();
        contacto2.setDados_contacto("962323");
        canal.addContacto(contacto1);
        canal.addContacto(contacto2);
        System.out.println(getTotalContactos(canal));
    }
    */

}
