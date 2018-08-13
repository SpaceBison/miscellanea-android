import android.annotation.TargetApi;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThisIsTheClass {
    private static ThisIsTheClass instance;
    private final Context context;
    private Bundle bundle;
    private Method setChannelIdMethod;
    private Method getNotificationChannelMethod;
    private final AtomicInteger requestCodes = new AtomicInteger((int)SystemClock.elapsedRealtime());
 
    private ThisIsTheClass(Context context) {
        this.context = context.getApplicationContext();
    }
 
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @TargetApi(value=26)
    private final Notification buildNotification(CharSequence contentTitle, String contentText, int n2, Integer color, Uri sound, PendingIntent pendingIntent, PendingIntent deleteIntent, String string3) {
        Notification.Builder builder = new Notification.Builder(this.context).setAutoCancel(true).setSmallIcon(n2);
        if (!TextUtils.isEmpty(contentTitle)) {
            builder.setContentTitle(contentTitle);
        }
        if (!TextUtils.isEmpty(contentText)) {
            builder.setContentText(contentText);
            builder.setStyle(new Notification.BigTextStyle().bigText(contentText));
        }
        if (color != null) {
            builder.setColor(color.intValue());
        }
        if (sound != null) {
            builder.setSound(sound);
        }
        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }
        if (deleteIntent != null) {
            builder.setDeleteIntent(deleteIntent);
        }
        if (string3 == null) return builder.build();
        if (this.setChannelIdMethod == null) {
            this.setChannelIdMethod = ThisIsTheClass.getMethod("setChannelId");
        }
        if (this.setChannelIdMethod == null) {
            this.setChannelIdMethod = ThisIsTheClass.getMethod("setChannel");
        }
        if (this.setChannelIdMethod == null) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel");
            return builder.build();
        }
        try {
            this.setChannelIdMethod.invoke(builder, string3);
            return builder.build();
        }
        catch (IllegalAccessException e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return builder.build();
        }
        catch (InvocationTargetException e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return builder.build();
        }
        catch (SecurityException e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return builder.build();
        }
        catch (IllegalArgumentException e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return builder.build();
        }
    }
 
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final Bundle getMetadata() {
        ApplicationInfo applicationInfo;
        if (this.bundle != null) {
            return this.bundle;
        }
        applicationInfo = null;
        try {
            ApplicationInfo applicationInfo2;
            applicationInfo = applicationInfo2 = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
        }
        catch (PackageManager.NameNotFoundException var2_3) {}
        if (applicationInfo != null && applicationInfo.metaData != null) {
            this.bundle = applicationInfo.metaData;
            return this.bundle;
        }
        return Bundle.EMPTY;
    }
 
    public static ThisIsTheClass getInstance(Context object) {
        synchronized (ThisIsTheClass.class) {
            if (instance == null) {
                instance = new ThisIsTheClass(object);
            }
            object = instance;
            return object;
        }
    }
 
    public static String getString(Bundle bundle, String string2) {
        String string3 = bundle.getString(string2);
        if (string3 == null) {
            string3 = bundle.getString(string2.replace("gcm.n.", "gcm.notification."));
        }
        return string3;
    }
 
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @TargetApi(value=26)
    private static Method getMethod(String object) {
        try {
            return Notification.Builder.class.getMethod(object, String.class);
        }
        catch (SecurityException var0_1) {
            do {
                return null;
                break;
            } while (true);
        }
        catch (NoSuchMethodException var0_2) {
            return null;
        }
    }
 
    private static void copyGoogleKeys(Intent intent, Bundle bundle) {
        for (String key : bundle.keySet()) {
            if (!key.startsWith("google.c.a.") && !key.equals("from")) continue;
            intent.putExtra(key, bundle.getString(key));
        }
    }
 
    public static boolean a(Bundle bundle) {
        return "1".equals(ThisIsTheClass.getString(bundle, "gcm.n.e")) || ThisIsTheClass.getString(bundle, "gcm.n.icon") != null;
    }
 
    public static Uri getLink(Bundle bundle) {
        String string3 = ThisIsTheClass.getString(bundle, "gcm.n.link_android");
        if (TextUtils.isEmpty(string3)) {
            string3 = ThisIsTheClass.getString(bundle, "gcm.n.link");
        }
        if (!TextUtils.isEmpty(string3)) {
            return Uri.parse(string3);
        }
        return null;
    }
 
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private final Integer getNotificationColor(String string2) {
        int n2;
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        if (!TextUtils.isEmpty(string2)) {
            int n3 = -1;
            try {
                n3 = Color.parseColor(string2);
            }
            catch (IllegalArgumentException var3_5) {
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(string2).length() + 54).append("Color ").append(string2).append(" not valid. Notification will use default color.").toString());
            }
            return n3;
        }
        if ((n2 = this.getMetadata().getInt("com.google.firebase.messaging.default_notification_color", 0)) == 0) return null;
        try {
            n2 = eh.c(this.context, n2);
        }
        catch (Resources.NotFoundException var1_2) {
            Log.w("FirebaseMessaging", "Cannot find the color resource referenced in AndroidManifest.");
            return null;
        }
        return n2;
    }
 
    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getLocalizedString(Bundle bundle, String string2) {
        string2 = String.valueOf(string2);
        String string3 = String.valueOf("_loc_key");
        if (string3.length() != 0) {
            string2 = string2.concat(string3);
            do {
                return ThisIsTheClass.getString(bundle, string2);
                break;
            } while (true);
        }
        string2 = new String(string2);
        return ThisIsTheClass.getString(bundle, string2);
    }
 
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @TargetApi(value=26)
    private final String getNotificationChannel(String class_) {
        void var2_11;
        if (!acg.h()) {
            return var2_11;
        }
        NotificationManager notificationManager = (NotificationManager)this.context.getSystemService((Class)NotificationManager.class);
        try {
            if (this.getNotificationChannelMethod == null) {
                this.getNotificationChannelMethod = notificationManager.getClass().getMethod("getNotificationChannel", String.class);
            }
            if (!TextUtils.isEmpty(class_)) {
                Class class_2 = class_;
                if (this.getNotificationChannelMethod.invoke(notificationManager, class_) != null) return var2_11;
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(class_).length() + 122).append("Notification Channel requested (").append((String) class_).append(") has not been created by the app. Manifest configuration, or default, value will be used.").toString());
            }
            if (!TextUtils.isEmpty((CharSequence) (class_ = this.getMetadata().getString("com.google.firebase.messaging.default_notification_channel_id")))) {
                Class class_3 = class_;
                if (this.getNotificationChannelMethod.invoke(notificationManager, class_) != null) return var2_11;
                Log.w("FirebaseMessaging", "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.");
            } else {
                Log.w("FirebaseMessaging", "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.");
            }
            if (this.getNotificationChannelMethod.invoke(notificationManager, "fcm_fallback_notification_channel") != null) return "fcm_fallback_notification_channel";
            class_ = Class.forName("android.app.NotificationChannel");
            Object obj = class_.getConstructor(String.class, CharSequence.class, Integer.TYPE).newInstance("fcm_fallback_notification_channel", this.context.getString(R.string.fcm_fallback_notification_channel_label), 3);
            notificationManager.getClass().getMethod("createNotificationChannel", class_).invoke(notificationManager, obj);
            return "fcm_fallback_notification_channel";
        }
        catch (InstantiationException var1_2) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_2);
            return null;
        }
        catch (InvocationTargetException var1_3) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_3);
            return null;
        }
        catch (NoSuchMethodException var1_4) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_4);
            return null;
        }
        catch (IllegalAccessException var1_5) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_5);
            return null;
        }
        catch (ClassNotFoundException var1_6) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_6);
            return null;
        }
        catch (SecurityException var1_7) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_7);
            return null;
        }
        catch (IllegalArgumentException var1_8) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_8);
            return null;
        }
        catch (LinkageError var1_9) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", var1_9);
            return null;
        }
    }
 
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Object[] getLocalizedArgs(Bundle arrobject, String string2) {
        Object object = String.valueOf(string2);
        String string3 = String.valueOf("_loc_args");
        object = string3.length() != 0 ? object.concat(string3) : new String((String)object);
        string3 = ThisIsTheClass.getString(arrobject, (String)object);
        if (TextUtils.isEmpty(string3)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(string3);
            object = new String[jSONArray.length()];
            int n2 = 0;
            do {
                arrobject = object;
                if (n2 >= object.length) return arrobject;
                object[n2] = jSONArray.opt(n2);
                ++n2;
            } while (true);
        }
        catch (JSONException var0_1) {
            String string4 = String.valueOf(string2);
            string2 = String.valueOf("_loc_args");
            string4 = string2.length() != 0 ? string4.concat(string2) : new String(string4);
            string4 = string4.substring(6);
            Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(string4).length() + 41 + String.valueOf(string3).length()).append("Malformed ").append(string4).append(": ").append(string3).append("  Default value will be used.").toString());
            return null;
        }
    }
 
    public static String getSound(Bundle bundle) {
        String string2;
        String string3 = string2 = ThisIsTheClass.getString(bundle, "gcm.n.sound2");
        if (TextUtils.isEmpty(string2)) {
            string3 = ThisIsTheClass.getString(bundle, "gcm.n.sound");
        }
        return string3;
    }
 
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final String d(Bundle object, String string2) {
        String string3 = ThisIsTheClass.getString(object, string2);
        if (!TextUtils.isEmpty(string3)) {
            return string3;
        }
        string3 = ThisIsTheClass.getLocalizedString(object, string2);
        if (TextUtils.isEmpty(string3)) {
            return null;
        }
        Resources resources = this.context.getResources();
        int n2 = resources.getIdentifier(string3, "string", this.context.getPackageName());
        if (n2 == 0) {
            object = String.valueOf(string2);
            string2 = String.valueOf("_loc_key");
            object = string2.length() != 0 ? object.concat(string2) : new String((String)object);
            object = object.substring(6);
            Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(object).length() + 49 + String.valueOf(string3).length()).append((String)object).append(" resource not found: ").append(string3).append(" Default value will be used.").toString());
            return null;
        }
        if ((object = ThisIsTheClass.getLocalizedArgs(object, string2)) == null) {
            return resources.getString(n2);
        }
        try {
            return resources.getString(n2, (Object[])object);
        }
        catch (MissingFormatArgumentException var2_3) {
            object = Arrays.toString((Object[])object);
            Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(string3).length() + 58 + String.valueOf(object).length()).append("Missing format argument for ").append(string3).append(": ").append((String)object).append(" Default value will be used.").toString(), var2_3);
            return null;
        }
    }
 
    /*
     * Enabled aggressive block sorting
     */
    private final PendingIntent e(Bundle iterator) {
        Object object;
        String string2 = ThisIsTheClass.getString(iterator, "gcm.n.click_action");
        if (!TextUtils.isEmpty(string2)) {
            string2 = new Intent(string2);
            string2.setPackage(this.context.getPackageName());
            string2.setFlags(268435456);
        } else {
            object = ThisIsTheClass.getLink(iterator);
            if (object != null) {
                string2 = new Intent("android.intent.action.VIEW");
                string2.setPackage(this.context.getPackageName());
                string2.setData((Uri)object);
            } else {
                string2 = this.context.getPackageManager().getLaunchIntentForPackage(this.context.getPackageName());
                if (string2 == null) {
                    Log.w("FirebaseMessaging", "No activity found to launch app");
                }
            }
        }
        if (string2 == null) {
            return null;
        }
        string2.addFlags(67108864);
        iterator = new Bundle(iterator);
        FirebaseMessagingService.a((Bundle)iterator);
        string2.putExtras((Bundle)iterator);
        iterator = iterator.keySet().iterator();
        while (iterator.hasNext()) {
            object = (String)iterator.next();
            if (!object.startsWith("gcm.n.") && !object.startsWith("gcm.notification.")) continue;
            string2.removeExtra((String)object);
        }
        return PendingIntent.getActivity(this.context, this.requestCodes.incrementAndGet(), (Intent)string2, 1073741824);
    }
 
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final boolean c(Bundle var1_1) {
        if ("1".equals(ThisIsTheClass.getString(var1_1, "gcm.n.noui"))) {
            return true;
        }
        if (((KeyguardManager)this.context.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) ** GOTO lbl-1000
        if (!acg.f()) {
            SystemClock.sleep((long)10);
        }
        var2_2 = Process.myPid();
        var6_3 = ((ActivityManager)this.context.getSystemService("activity")).getRunningAppProcesses();
        if (var6_3 != null) {
            var6_3 = var6_3.iterator();
            while (var6_3.hasNext()) {
                var7_4 = (ActivityManager.RunningAppProcessInfo)var6_3.next();
                if (var7_4.pid != var2_2) continue;
                if (var7_4.importance == 100) {
                    return false;
                }
                var2_2 = 0;
                break;
            }
        } else lbl-1000: // 3 sources:
        {
            var2_2 = 0;
        }
        if (var2_2 != 0) {
            return false;
        }
        var7_4 = var6_3 = this.d(var1_1, "gcm.n.title");
        if (TextUtils.isEmpty((CharSequence)var6_3)) {
            var7_4 = this.context.getApplicationInfo().loadLabel(this.context.getPackageManager());
        }
        var10_5 = this.d(var1_1, "gcm.n.body");
        var6_3 = ThisIsTheClass.getString(var1_1, "gcm.n.icon");
        if (TextUtils.isEmpty((CharSequence)var6_3)) ** GOTO lbl32
        var8_6 = this.context.getResources();
        var2_2 = var8_6.getIdentifier((String)var6_3, "drawable", this.context.getPackageName());
        if (var2_2 == 0) {
            var2_2 = var3_10 = var8_6.getIdentifier((String)var6_3, "mipmap", this.context.getPackageName());
            if (var3_10 == 0) {
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(var6_3).length() + 61).append("Icon resource ").append((String)var6_3).append(" not found. Notification will use default icon.").toString());
lbl32: // 2 sources:
                var2_2 = var3_10 = this.getMetadata().getInt("com.google.firebase.messaging.default_notification_icon", 0);
                if (var3_10 == 0) {
                    var2_2 = this.context.getApplicationInfo().icon;
                }
                var3_10 = var2_2;
                if (var2_2 == 0) {
                    var3_10 = 17301651;
                }
                var2_2 = var3_10;
            }
        }
        var11_7 = this.getNotificationColor(ThisIsTheClass.getString(var1_1, "gcm.n.color"));
        var6_3 = ThisIsTheClass.getSound(var1_1);
        if (TextUtils.isEmpty((CharSequence)var6_3)) {
            var6_3 = null;
        } else if (!"default".equals(var6_3) && this.context.getResources().getIdentifier((String)var6_3, "raw", this.context.getPackageName()) != 0) {
            var8_6 = this.context.getPackageName();
            var6_3 = Uri.parse(new StringBuilder(String.valueOf("android.resource://").length() + 5 + String.valueOf(var8_6).length() + String.valueOf(var6_3).length()).append("android.resource://").append((String)var8_6).append("/raw/").append((String)var6_3).toString());
        } else {
            var6_3 = RingtoneManager.getDefaultUri(2);
        }
        var8_6 = this.e(var1_1);
        if (FirebaseMessagingService.b((Bundle)var1_1)) {
            var9_8 = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
            ThisIsTheClass.copyGoogleKeys(var9_8, (Bundle)var1_1);
            var9_8.putExtra("pending_intent", (Parcelable)var8_6);
            var8_6 = bau.b(this.context, this.requestCodes.incrementAndGet(), var9_8, 1073741824);
            var9_8 = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
            ThisIsTheClass.copyGoogleKeys(var9_8, (Bundle)var1_1);
            var9_8 = bau.b(this.context, this.requestCodes.incrementAndGet(), var9_8, 1073741824);
        } else {
            var9_8 = null;
        }
        if (acg.h() && this.context.getApplicationInfo().targetSdkVersion > 25) {
            var6_3 = this.buildNotification((CharSequence)var7_4, var10_5, var2_2, var11_7, (Uri)var6_3, (PendingIntent)var8_6, (PendingIntent)var9_8, this.getNotificationChannel(ThisIsTheClass.getString(var1_1, "gcm.n.android_channel_id")));
        } else {
            var12_11 = new NotificationCompat.Builder(this.context).setAutoCancel(true).setSmallIcon(var2_2);
            if (!TextUtils.isEmpty((CharSequence)var7_4)) {
                var12_11.setContentTitle((CharSequence)var7_4);
            }
            if (!TextUtils.isEmpty((CharSequence)var10_5)) {
                var12_11.setContentText(var10_5);
                var12_11.setStyle(new NotificationCompat.b().a(var10_5));
            }
            if (var11_7 != null) {
                var12_11.setColor(var11_7);
            }
            if (var6_3 != null) {
                var12_11.setSound((Uri)var6_3);
            }
            if (var8_6 != null) {
                var12_11.setContentIntent((PendingIntent)var8_6);
            }
            if (var9_8 != null) {
                var12_11.setDeleteIntent((PendingIntent)var9_8);
            }
            var6_3 = var12_11.build();
        }
        var7_4 = ThisIsTheClass.getString(var1_1, "gcm.n.tag");
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            Log.d("FirebaseMessaging", "Showing notification");
        }
        var8_6 = (NotificationManager)this.context.getSystemService("notification");
        var1_1 = var7_4;
        if (TextUtils.isEmpty((CharSequence)var7_4)) {
            var4_9 = SystemClock.uptimeMillis();
            var1_1 = new StringBuilder(37).append("FCM-Notification:").append(var4_9).toString();
        }
        var8_6.notify((String)var1_1, 0, (Notification)var6_3);
        return true;
    }
}