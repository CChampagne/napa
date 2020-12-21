package org.cch.napa;

import java.util.Collection;

public class StringUtil {
    public static String join (Collection<Object> coll, String separator) {
        if (coll == null) return null;
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Object o : coll) {
            if (first) {
                sb.append(separator);
            } else {
                first = false;
            }
            sb.append(o);
        }
        return sb.toString();
    }
    public static String join (Object[] coll, String separator) {
        if (coll == null) return null;
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Object o : coll) {
            if (first) {
                sb.append(separator);
            } else {
                first = false;
            }
            sb.append(o);
        }
        return sb.toString();
    }
}
