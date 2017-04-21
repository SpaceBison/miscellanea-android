package org.spacebison.androidutils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by cmb on 25.11.16.
 */

public class ResourceUtils {
    @Nullable
    public static Drawable getDrawable(Context context, String packageName, String resourceName) {
        Resources updaterResources;

        try {
            updaterResources = context.getPackageManager().getResourcesForApplication(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

        int backgroundResId = updaterResources.getIdentifier(resourceName, "drawable", packageName);

        if (backgroundResId == 0) {
            return null;
        }

        return ResourcesCompat.getDrawable(updaterResources, backgroundResId, null);
    }


}
