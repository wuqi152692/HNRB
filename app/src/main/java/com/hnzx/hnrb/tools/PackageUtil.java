package com.hnzx.hnrb.tools;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by FoAng on 17/5/10 下午4:45;
 */

public class PackageUtil {

    public static String getVersionName(Context mContext) {
        try {
            final String packageName = mContext.getPackageName();
            return mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
