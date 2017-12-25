package io.github.izzyleung.zhihudailypurify.support;

import android.content.Intent;
import android.content.pm.PackageManager;

import io.github.izzyleung.zhihudailypurify.ZhihuDailyPurifyApplication;

public final class Check {
    private Check() {

    }

    public static boolean isZhihuClientInstalled() {
        try {
            return preparePackageManager().getPackageInfo(Constants.Information.ZHIHU_PACKAGE_ID, PackageManager.GET_ACTIVITIES) != null;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    public static boolean isIntentSafe(Intent intent) {
        return preparePackageManager().queryIntentActivities(intent, 0).size() > 0;
    }

    private static PackageManager preparePackageManager() {
        return ZhihuDailyPurifyApplication.getInstance().getPackageManager();
    }
}
