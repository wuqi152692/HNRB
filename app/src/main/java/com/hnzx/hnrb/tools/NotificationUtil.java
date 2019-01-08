package com.hnzx.hnrb.tools;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.hnzx.hnrb.MainActivity;
import com.hnzx.hnrb.R;


/**
 * Created by FoAng on 16/11/29 下午2:50；
 */
public class NotificationUtil {

    public static NotificationCompat.Builder showSimpleImNotification(Context mContext, String userName, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setContentTitle("新消息")
                .setContentIntent(getPendingIntent(mContext, userName))
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(isHighVersion() ? R.mipmap.icon : R.mipmap.icon);
        return mBuilder;
    }

    public static boolean isHighVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static PendingIntent getPendingIntent(Context mContext, String userName) {
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        /*resultIntent.putExtra(EaseConstant.EXTRA_USER_ID, userName);*/
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }

    public static void cleanAllNotification(Context mContext) {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Activity.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }
}
