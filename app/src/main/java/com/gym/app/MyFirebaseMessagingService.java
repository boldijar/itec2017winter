package com.gym.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.gym.app.activities.HomeActivity;

import java.util.Map;

import timber.log.Timber;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Map<String, String> map = remoteMessage.getData();
            String message = map.get("message");
            int eventId = Integer.valueOf(map.get("event_id"));
            buildNotification(message, eventId);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void buildNotification(String message, int eventId) {
        Intent resultIntent = HomeActivity.createIntent(this, eventId);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setContentTitle("Yaaas!")
                        .setContentText(message);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify((int) (Math.random() * 10000), mBuilder.build());
    }
}
