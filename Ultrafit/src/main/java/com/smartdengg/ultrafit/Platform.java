package com.smartdengg.ultrafit;

/**
 * Created by SmartDengg on 2016/4/24.
 */
public class Platform {

    static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();

    private static boolean hasRxJavaOnClasspath() {
        try {
            Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }

}
