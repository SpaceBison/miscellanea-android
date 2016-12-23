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
        SERVICE_NAMES.put(AccessibilityService.class, Context.ACCESSIBILITY_SERVICE);
        SERVICE_NAMES.put(AlarmManager.class, Context.ALARM_SERVICE);
        SERVICE_NAMES.put(ActivityManager.class, Context.ACTIVITY_SERVICE);
        SERVICE_NAMES.put(AccountManager.class, Context.ACCOUNT_SERVICE);
        SERVICE_NAMES.put(AppWidgetManager.class, Context.APPWIDGET_SERVICE);
        SERVICE_NAMES.put(AppOpsManager.class, Context.APP_OPS_SERVICE);
        SERVICE_NAMES.put(AudioManager.class, Context.AUDIO_SERVICE);
        SERVICE_NAMES.put(BatteryManager.class, Context.BATTERY_SERVICE);
        SERVICE_NAMES.put(BluetoothManager.class, Context.BLUETOOTH_SERVICE);
        SERVICE_NAMES.put(CameraManager.class, Context.CAMERA_SERVICE);
        SERVICE_NAMES.put(CaptioningManager.class, Context.CAPTIONING_SERVICE);
        SERVICE_NAMES.put(CarrierService.class, Context.CARRIER_CONFIG_SERVICE);
        SERVICE_NAMES.put(ClipboardManager.class, Context.CLIPBOARD_SERVICE);
        SERVICE_NAMES.put(ConnectivityManager.class, Context.CONNECTIVITY_SERVICE);
        SERVICE_NAMES.put(ConsumerIrManager.class, Context.CONSUMER_IR_SERVICE);
        SERVICE_NAMES.put(DevicePolicyManager.class, Context.DEVICE_POLICY_SERVICE);
        SERVICE_NAMES.put(DisplayManager.class, Context.DISPLAY_SERVICE);
        SERVICE_NAMES.put(DownloadManager.class, Context.DOWNLOAD_SERVICE);
        SERVICE_NAMES.put(DropBoxManager.class, Context.DROPBOX_SERVICE);
        SERVICE_NAMES.put(FingerprintManager.class, Context.FINGERPRINT_SERVICE);
        SERVICE_NAMES.put(HardwarePropertiesManager.class, Context.HARDWARE_PROPERTIES_SERVICE);
        SERVICE_NAMES.put(InputMethodService.class, Context.INPUT_METHOD_SERVICE);
        SERVICE_NAMES.put(InputManager.class, Context.INPUT_SERVICE);
        SERVICE_NAMES.put(JobScheduler.class, Context.JOB_SCHEDULER_SERVICE);
        SERVICE_NAMES.put(KeyguardManager.class, Context.KEYGUARD_SERVICE);
        SERVICE_NAMES.put(LauncherApps.class, Context.LAUNCHER_APPS_SERVICE);
        SERVICE_NAMES.put(LayoutInflater.class, Context.LAYOUT_INFLATER_SERVICE);
        SERVICE_NAMES.put(LocationManager.class, Context.LOCATION_SERVICE);
        SERVICE_NAMES.put(MediaProjectionManager.class, Context.MEDIA_PROJECTION_SERVICE);
        SERVICE_NAMES.put(MediaRouter.class, Context.MEDIA_ROUTER_SERVICE);
        SERVICE_NAMES.put(MediaSession.class, Context.MEDIA_SESSION_SERVICE);
        SERVICE_NAMES.put(MidiManager.class, Context.MIDI_SERVICE);
        SERVICE_NAMES.put(NetworkStatsManager.class, Context.NETWORK_STATS_SERVICE);
        SERVICE_NAMES.put(NfcManager.class, Context.NFC_SERVICE);
        SERVICE_NAMES.put(NotificationManager.class, Context.NOTIFICATION_SERVICE);
        SERVICE_NAMES.put(NsdManager.class, Context.NSD_SERVICE);
        SERVICE_NAMES.put(PowerManager.class, Context.POWER_SERVICE);
        SERVICE_NAMES.put(PrintManager.class, Context.PRINT_SERVICE);
        SERVICE_NAMES.put(RestrictionsManager.class, Context.RESTRICTIONS_SERVICE);
        SERVICE_NAMES.put(SearchManager.class, Context.SEARCH_SERVICE);
        SERVICE_NAMES.put(SensorManager.class, Context.SENSOR_SERVICE);
        SERVICE_NAMES.put(ShortcutManager.class, Context.SHORTCUT_SERVICE);
        SERVICE_NAMES.put(StorageManager.class, Context.STORAGE_SERVICE);
        SERVICE_NAMES.put(SystemHealthManager.class, Context.SYSTEM_HEALTH_SERVICE);
        SERVICE_NAMES.put(TelecomManager.class, Context.TELECOM_SERVICE);
        SERVICE_NAMES.put(TelephonyManager.class, Context.TELEPHONY_SERVICE);
        SERVICE_NAMES.put(SubscriptionManager.class, Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        SERVICE_NAMES.put(TextServicesManager.class, Context.TEXT_SERVICES_MANAGER_SERVICE);
        SERVICE_NAMES.put(TvInputManager.class, Context.TV_INPUT_SERVICE);
        SERVICE_NAMES.put(UiModeManager.class, Context.UI_MODE_SERVICE);
        SERVICE_NAMES.put(UsageStatsManager.class, Context.USAGE_STATS_SERVICE);
        SERVICE_NAMES.put(UsbManager.class, Context.USB_SERVICE);
        SERVICE_NAMES.put(UserManager.class, Context.USER_SERVICE);
        SERVICE_NAMES.put(Vibrator.class, Context.VIBRATOR_SERVICE);
        SERVICE_NAMES.put(WallpaperManager.class, Context.WALLPAPER_SERVICE);
        SERVICE_NAMES.put(WifiP2pManager.class, Context.WIFI_P2P_SERVICE);
        SERVICE_NAMES.put(WifiManager.class, Context.WIFI_SERVICE);
        SERVICE_NAMES.put(WindowManager.class, Context.WINDOW_SERVICE);
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
