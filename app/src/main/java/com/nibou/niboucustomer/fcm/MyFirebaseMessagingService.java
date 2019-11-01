package com.nibou.niboucustomer.fcm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.activitys.ReviewActivity;
import com.nibou.niboucustomer.activitys.SplashActivity;
import com.nibou.niboucustomer.models.AppSessionModel;
import com.nibou.niboucustomer.utils.AppConstant;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.LocalPrefences;

import java.util.HashSet;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        AppUtil.showLog("Token", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            AppUtil.showLog("Push", remoteMessage.getData().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showNotification(String title, String message) {
        int notificationId = (int) System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.putExtra(AppConstant.NOTIFICATION_ID, String.valueOf(notificationId));

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext())
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(message)
                .setSound(soundUri)
                .setSmallIcon(R.mipmap.ic_launcher_foreground);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        }
        mBuilder.setContentIntent(PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        mBuilder.setDefaults(-1);
        mBuilder.setVibrate(new long[]{0, 1000, 1000, 1000, 1000});

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(getString(R.string.fcm_channel_id), getString(R.string.notifications), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.group));
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(getString(R.string.fcm_channel_id));
        }
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
