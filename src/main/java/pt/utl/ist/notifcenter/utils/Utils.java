/*
    Some methods that might be useful
*/

package pt.utl.ist.notifcenter.utils;

import org.apache.avro.reflect.Nullable;
import org.springframework.util.CollectionUtils;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.dml.DomainClass;
import pt.ist.fenixframework.dml.Slot;

import java.util.*;

public class Utils {
    
    //Colors
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
        }

        return true;
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
