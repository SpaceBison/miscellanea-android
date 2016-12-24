package org.spacebison.androidutils;

import android.accessibilityservice.AccessibilityService;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.job.JobScheduler;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutManager;
import android.hardware.ConsumerIrManager;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.inputmethodservice.InputMethodService;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSession;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.HardwarePropertiesManager;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.health.SystemHealthManager;
import android.os.storage.StorageManager;
import android.print.PrintManager;
import android.service.carrier.CarrierService;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.CaptioningManager;
import android.view.textservice.TextServicesManager;

public class SystemServiceUtils {
    private static final ArrayMap<Class, String> SERVICE_NAMES = new ArrayMap<>();
    static {
        registerService(AccessibilityService.class, Context.ACCESSIBILITY_SERVICE);
        registerService(AlarmManager.class, Context.ALARM_SERVICE);
        registerService(ActivityManager.class, Context.ACTIVITY_SERVICE);
        registerService(AccountManager.class, Context.ACCOUNT_SERVICE);
        registerService(AppWidgetManager.class, Context.APPWIDGET_SERVICE);
        registerService(AppOpsManager.class, Context.APP_OPS_SERVICE);
        registerService(AudioManager.class, Context.AUDIO_SERVICE);
        registerService(BatteryManager.class, Context.BATTERY_SERVICE);
        registerService(BluetoothManager.class, Context.BLUETOOTH_SERVICE);
        registerService(CameraManager.class, Context.CAMERA_SERVICE);
        registerService(CaptioningManager.class, Context.CAPTIONING_SERVICE);
        registerService(CarrierService.class, Context.CARRIER_CONFIG_SERVICE);
        registerService(ClipboardManager.class, Context.CLIPBOARD_SERVICE);
        registerService(ConnectivityManager.class, Context.CONNECTIVITY_SERVICE);
        registerService(ConsumerIrManager.class, Context.CONSUMER_IR_SERVICE);
        registerService(DevicePolicyManager.class, Context.DEVICE_POLICY_SERVICE);
        registerService(DisplayManager.class, Context.DISPLAY_SERVICE);
        registerService(DownloadManager.class, Context.DOWNLOAD_SERVICE);
        registerService(DropBoxManager.class, Context.DROPBOX_SERVICE);
        registerService(FingerprintManager.class, Context.FINGERPRINT_SERVICE);
        registerService(HardwarePropertiesManager.class, Context.HARDWARE_PROPERTIES_SERVICE);
        registerService(InputMethodService.class, Context.INPUT_METHOD_SERVICE);
        registerService(InputManager.class, Context.INPUT_SERVICE);
        registerService(JobScheduler.class, Context.JOB_SCHEDULER_SERVICE);
        registerService(KeyguardManager.class, Context.KEYGUARD_SERVICE);
        registerService(LauncherApps.class, Context.LAUNCHER_APPS_SERVICE);
        registerService(LayoutInflater.class, Context.LAYOUT_INFLATER_SERVICE);
        registerService(LocationManager.class, Context.LOCATION_SERVICE);
        registerService(MediaProjectionManager.class, Context.MEDIA_PROJECTION_SERVICE);
        registerService(MediaRouter.class, Context.MEDIA_ROUTER_SERVICE);
        registerService(MediaSession.class, Context.MEDIA_SESSION_SERVICE);
        registerService(MidiManager.class, Context.MIDI_SERVICE);
        registerService(NetworkStatsManager.class, Context.NETWORK_STATS_SERVICE);
        registerService(NfcManager.class, Context.NFC_SERVICE);
        registerService(NotificationManager.class, Context.NOTIFICATION_SERVICE);
        registerService(NsdManager.class, Context.NSD_SERVICE);
        registerService(PowerManager.class, Context.POWER_SERVICE);
        registerService(PrintManager.class, Context.PRINT_SERVICE);
        registerService(RestrictionsManager.class, Context.RESTRICTIONS_SERVICE);
        registerService(SearchManager.class, Context.SEARCH_SERVICE);
        registerService(SensorManager.class, Context.SENSOR_SERVICE);
        registerService(ShortcutManager.class, Context.SHORTCUT_SERVICE);
        registerService(StorageManager.class, Context.STORAGE_SERVICE);
        registerService(SystemHealthManager.class, Context.SYSTEM_HEALTH_SERVICE);
        registerService(TelecomManager.class, Context.TELECOM_SERVICE);
        registerService(TelephonyManager.class, Context.TELEPHONY_SERVICE);
        registerService(SubscriptionManager.class, Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        registerService(TextServicesManager.class, Context.TEXT_SERVICES_MANAGER_SERVICE);
        registerService(TvInputManager.class, Context.TV_INPUT_SERVICE);
        registerService(UiModeManager.class, Context.UI_MODE_SERVICE);
        registerService(UsageStatsManager.class, Context.USAGE_STATS_SERVICE);
        registerService(UsbManager.class, Context.USB_SERVICE);
        registerService(UserManager.class, Context.USER_SERVICE);
        registerService(Vibrator.class, Context.VIBRATOR_SERVICE);
        registerService(WallpaperManager.class, Context.WALLPAPER_SERVICE);
        registerService(WifiP2pManager.class, Context.WIFI_P2P_SERVICE);
        registerService(WifiManager.class, Context.WIFI_SERVICE);
        registerService(WindowManager.class, Context.WINDOW_SERVICE);
    }

    private static void registerService(Class serviceClass, String serviceName) {
        try {
            SERVICE_NAMES.put(serviceClass, serviceName);
        } catch (NoClassDefFoundError ignored) {}
    }

    @SuppressWarnings({"WrongConstant", "unchecked"})
    @SuppressLint("ServiceCast")
    public static <T> T getSystemService(@NonNull Context context, Class<T> serviceClass) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getSystemService(serviceClass);
        } else {
            return (T) context.getSystemService(SERVICE_NAMES.get(serviceClass));
        }
    }
}
