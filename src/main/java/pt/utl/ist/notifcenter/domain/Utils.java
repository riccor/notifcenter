package pt.utl.ist.notifcenter.domain;

import org.springframework.util.CollectionUtils;
import java.util.Iterator;

class Utils {

    static <E> void removeElementFromSet(java.util.Set<E> set, E element) {

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

}
