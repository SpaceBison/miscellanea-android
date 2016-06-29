package org.spacebison.persistentalarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wmatuszewski on 6/28/16.
 */
public class PersistentAlarmManager {
    private final Context mContext;
    private final SharedPreferences mSharedPreferences;
    private final AlarmManager mAlarmManager;

    public PersistentAlarmManager(Context context) {
        this(context, context.getSharedPreferences("com.n7mobile.misc.PersistentAlarmManager.Prefs", Context.MODE_PRIVATE));
    }

    public PersistentAlarmManager(Context context, SharedPreferences sharedPreferences) {
        mContext = context;
        mSharedPreferences = sharedPreferences;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void set(int type, long triggerAtMillis, PendingIntent operation) {

        mAlarmManager.set(type, triggerAtMillis, operation);
    }

    public void setRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        mAlarmManager.setRepeating(type, triggerAtMillis, intervalMillis, operation);
    }
}
