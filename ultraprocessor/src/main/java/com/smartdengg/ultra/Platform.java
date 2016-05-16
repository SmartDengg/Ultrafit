package com.smartdengg.ultra;

/**
 * Created by SmartDengg on 2016/4/24.
 */
public class Platform {

    private Platform() {
        throw new IllegalStateException("No instance");
    }

    public static final boolean HAS_RX_OBSERVABLE = hasRxObservableOnClasspath();
    public static final boolean HAS_RX_SINGLE = hasRxSingleOnClasspath();
    public static final boolean HAS_RX_COMPLETABLE = hasRxCompletableOnClasspath();

    private static boolean hasRxObservableOnClasspath() {

        boolean hasObservable = false;

        try {
            Class.forName("rx.Observable");
            hasObservable = true;
        } catch (ClassNotFoundException ignored) {
        }
        return hasObservable;
    }

    private static boolean hasRxSingleOnClasspath() {

        boolean hasSingle = false;
        try {
            Class.forName("rx.Single");
            hasSingle = true;
        } catch (ClassNotFoundException ignored) {
        }
        return hasSingle;
    }

    private static boolean hasRxCompletableOnClasspath() {

        boolean hasCompletable = false;
        try {
            Class.forName("rx.Completable");
            hasCompletable = true;
        } catch (ClassNotFoundException ignored) {
        }
        return hasCompletable;
    }
}
