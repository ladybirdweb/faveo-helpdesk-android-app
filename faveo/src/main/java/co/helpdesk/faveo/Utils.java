package co.helpdesk.faveo;


import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by narendra on 18/10/16.
 */

public class Utils {

    public static String[] removeDuplicates(String[] arr) {

        LinkedHashSet<String> lhs = new LinkedHashSet<>();

        Collections.addAll(lhs, arr);

        String[] strArr = new String[lhs.size()];

        lhs.toArray(strArr);

        return strArr;
    }
}
