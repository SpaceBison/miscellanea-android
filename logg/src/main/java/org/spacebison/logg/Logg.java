package org.spacebison.logg;


import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.Iterator;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * Created by cmb on 24.04.16.
 */
public class Logg {
    /**
     * Send a {@link Log#VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        log(Log.VERBOSE, tag, msg);
    }

    private static void log(int priority, String tag, String msg) {
        if (Fabric.isInitialized()) {
            Crashlytics.log(priority, tag, msg);
        } else {
            Log.println(Log.VERBOSE, tag, msg);
        }
    }

    /**
     * Send a {@link Log#WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        log(Log.WARN, tag, msg);
    }

    /**
     * Send a {@link Log#WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void w(String tag, String msg, Throwable tr) {
        Crashlytics.log(Log.WARN, tag, msg + ": " + tr);
        Crashlytics.logException(tr);
    }

    /**
     * Send a {@link Log#ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        Crashlytics.log(Log.ERROR, tag, msg + ": " + tr);
        Crashlytics.logException(tr);
    }

    public static void wtf(String tag, String msg) {
        Crashlytics.log(Log.ERROR, tag, msg);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        Crashlytics.log(Log.DEBUG, tag, msg);
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void println(int priority, String tag, String msg) {
        log(priority, tag, msg);
    }

    public static void w(String tag, Throwable tr) {
        Crashlytics.log(Log.WARN, tag, tr.toString());
        Crashlytics.logException(tr);
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void v(String tag, String msg, Throwable tr) {
        Crashlytics.log(Log.VERBOSE, tag, msg + ": " + tr);
        Crashlytics.logException(tr);
    }

    public static void wtf(String tag, Throwable tr) {
        Crashlytics.log(Log.ERROR, tag, tr.toString());
        Crashlytics.logException(tr);
    }

    /**
     * Send a {@link Log#DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        Crashlytics.log(Log.DEBUG, tag, msg + ": " + tr);
        Crashlytics.logException(tr);
    }

    /**
     * Send an {@link Log#ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        log(Log.ERROR, tag, msg);
    }

    /**
     * Send an {@link Log#INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        log(Log.INFO, tag, msg);
    }

    /**
     * Send a {@link Log#INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        Crashlytics.log(Log.INFO, tag, msg + ": " + tr);
        Crashlytics.logException(tr);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        Crashlytics.log(Log.ERROR, tag, msg + ": " + tr);
        Crashlytics.logException(tr);
    }

    public static String toMultiLineString(Iterable iterable) {
        if (iterable == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder(iterable.getClass().getSimpleName());
        sb.append('{');

        Iterator iterator = iterable.iterator();
        if (iterator.hasNext()) {
            sb.append('\n').append(iterator.next());

            while (iterator.hasNext()) {
                sb.append(",\n  ").append(iterator.next());
            }
        }

        return sb.append('}').toString();
    }

    public static String toMultiLineString(Map<?, ?> map) {
        if (map == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder(map.getClass().getSimpleName());
        sb.append('{');

        if (!map.isEmpty()) {
            sb.append('\n');

            for (Map.Entry entry : map.entrySet()) {
                sb.append("  ").append(entry.getKey()).append(" -> ").append(entry.getValue()).append(",\n");
            }
        }

        return sb.append('}').toString();
    }
}
