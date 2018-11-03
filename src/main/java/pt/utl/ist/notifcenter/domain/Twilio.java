package pt.utl.ist.notifcenter.domain;

import pt.ist.fenixframework.Atomic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Twilio extends Twilio_Base {

    private Twilio(SistemaNotificacoes sistemaNotificacoes) {
        super();
        this.setSistemaNotificacoes(sistemaNotificacoes);
    }

    @Atomic
    public static Twilio createTwilio(SistemaNotificacoes sistemaNotificacoes, final String accountSID, final String authToken) {
        Twilio twilio = new Twilio(sistemaNotificacoes);
        twilio.setAccountSID(accountSID);
        twilio.setAuthToken(authToken);
        return twilio;
    }

    //"config.properties"
    public static Twilio createTwilioFromFile(SistemaNotificacoes sistemaNotificacoes, final String filename) {
        Map<String, String> propertiesMap = new ConcurrentHashMap<>();
        propertiesMap.put("accountSID", "null");
        propertiesMap.put("authToken", "null");

        LoadPropertiesFromFile(Twilio.class, filename, propertiesMap);

        if (!IsMapFilled(propertiesMap)) {
            System.out.println("Error: Cannot create entity from file.");
            return null;
        }

        return createTwilio(sistemaNotificacoes, propertiesMap.get("accountSID"), propertiesMap.get("authToken"));
    }

    public static boolean IsMapFilled(Map<String, String> propertiesMap) {

        for(Map.Entry<String, String> entry : propertiesMap.entrySet()) {

            if(entry.getValue().equals("null")) {
                return false;
            }

            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue() + ";");
        }

        return true;
    }


    public static <T> void LoadPropertiesFromFile(Class<T> clazz,final String filename, Map<String, String> propertiesMap) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            //clazz.getResourceAsStream() - procura o resource no mesmo diretorio do ficheiro .class
            //clazz.getClassLoader().getResourceAsStream() - procura no CLASSPATH
            input = clazz.getClassLoader().getResourceAsStream(filename);

            System.out.println("clazz: " + clazz);
            System.out.println("clazz.getClassLoader(): " + clazz.getClassLoader());

            if (input == null) {
                System.out.println("Error: Unable to find file " + filename + "!");
                return;
            }

            // load a properties file
            prop.load(input);

            // get the property values
            for(Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    propertiesMap.put(entry.getValue(), prop.getProperty(entry.getValue()));
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
