package pt.utl.ist.notifcenter.domain;

import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {

    /* já existe no bennu e mais eficiente (usa ConcurrentHashMap como cache):
    @Atomic
    public static User findUserByName(String name) {
        Set<User> users = FenixFramework.getDomainRoot().getBennu().getUserSet();
        for (User u : users) {
            if (u.getName().equals(name))
                return u;
        }
        return null;
    }
    */

    //might be useful ...or not:
    public static <E> void removeElementFromSet(java.util.Set<E> set, E element) {

        if (CollectionUtils.isEmpty(set)) {
            Iterator<E> i = set.iterator();
            while (i.hasNext()) {
                E o = i.next();

                if (o.equals(element)) {
                    i.remove();
                    break;
                }
            }
        }
    }


    public static boolean isMapFilled(Map<String, String> propertiesMap) {

        for(Map.Entry<String, String> entry : propertiesMap.entrySet()) {

            if(entry.getValue().equals("null")) {
                return false;
            }

            //System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue() + ";");
        }

        return true;
    }

    public static <T> Map<String, String>  loadPropertiesFromPropertiesFile(Class<T> clazz, final String filename, final String... params) {
        Properties prop = new Properties();
        InputStream input = null;
        Map<String, String> propertiesMap = new ConcurrentHashMap<>();

        for (String s : params) {
            propertiesMap.put(s, "null");
        }

        try {
            //clazz.getResourceAsStream() - procura o resource no mesmo diretorio do ficheiro .class
            //clazz.getClassLoader().getResourceAsStream() - procura no CLASSPATH
            input = clazz.getClassLoader().getResourceAsStream(filename);

            //System.out.println("clazz.getClassLoader().getResource(filename): " + clazz.getClassLoader().getResource(filename));

            if (input == null) {
                System.out.println("Error: Unable to find file " + filename + "!");
                return null;
            }

            // load a properties file
            prop.load(input);

            // get the property values
            for(Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                if (!entry.getKey().isEmpty()) {
                    //System.out.println("key: " + entry.getKey());
                    propertiesMap.put(entry.getKey(), prop.getProperty(entry.getKey()));
                }
            }
        }
        catch (NullPointerException | IOException ex) {
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

        return propertiesMap;
    }


}
