package pt.utl.ist.notifcenter.utils;

import org.apache.avro.reflect.Nullable;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.security.Authenticate;
import org.springframework.util.CollectionUtils;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Slot;
import pt.utl.ist.notifcenter.domain.AnotacaoCanal;
import pt.utl.ist.notifcenter.domain.Canal;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {
    
    //colors
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String WHITE = "\033[0;37m";   // WHITE
    public static final String MAGENTA = "\033[0;35m";  // MAGENTA
    public static final String CYAN = "\033[0;36m";     // CYAN

    public static long getCurrentEpochTime() {
        return new Date().getTime() / 1000L;
    }

    public static String getCurrentEpochTimeAsString() {
        return Long.toString(getCurrentEpochTime());
    }

    public static String generateRandomLettersString(int length) {

        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }

        return builder.toString();
    }

    public static String splitAndGetLastIndex(String str, String regex) {
        String[] bits = str.split(regex);

        if (bits.length > 0) {
            return bits[bits.length-1];
        }

        return str;
    }

    public static <T> boolean isClassAChannel(Class<T> clazz) {
        if (clazz.isAnnotationPresent(AnotacaoCanal.class)) {
            return true;
        }
        return false;
    }

    public static <T> String[] getDomainClassSlots(Class <T> clazz){
        ArrayList<String> arrayList = new ArrayList<>();
        for (DomainClass dc : FenixFramework.getDomainModel().getDomainClasses()) {
            if (dc.getName().equals(clazz.getSimpleName())) {
                for (Slot s : dc.getSlotsList()) {
                    //debug
                    //System.out.println("Class: " + dc.getName() + ": " + splitAndGetLastIndex(s.getTypeName(), "\\.") + " " + s.getName() + ";");
                    arrayList.add(s.getName());
                }
            }
        }
        return arrayList.toArray(new String[0]);
    }

    public static boolean isValidString(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }

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

    public static String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
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

    public static void printMap(Map<String, String> map, String title) {
        System.out.println();
        System.out.println(Utils.MAGENTA + title + Utils.CYAN);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        System.out.println();
    }

}

/*
for (Map.Entry<String, List<String>> a : CanalResource.getAvailableChannelsNamesAndParams().entrySet()) {
        System.out.println(a.getKey());

        for (String c : a.getValue()) {
        System.out.println(c);
        }
        }
*/

