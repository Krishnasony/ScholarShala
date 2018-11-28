package com.appteq.ad.appteq.actions;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.appteq.ad.appteq.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static String message = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            //Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getData().get("message"));
        }

        if (remoteMessage.getData().size() > 0) {
            String message = remoteMessage.getData().get("message");
            String notificationtitle = remoteMessage.getData().get("testnotifytitle");
            String notification_desc = remoteMessage.getData().get("testnotifydesc");
            String service = remoteMessage.getData().get("start_service");
            handleDataMessage(notificationtitle,notification_desc,service,remoteMessage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
           /* storeDataMessage(message);
            triggerAlarm();*/

            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                nManager.createNotificationChannel(mChannel);
                ncomp.setContentTitle("Alarm Receiver");
                ncomp.setContentText("Hey there is a new notification for you...");
                ncomp.setTicker("Hurraaaah....");
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                ncomp.setChannelId(CHANNEL_ID);
                ncomp.setVibrate(new long[]{200, 200});
                ncomp.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
            }
            else{
                ncomp.setContentTitle("Alarm Receiver");
                ncomp.setContentText("Hey there is a new notification for you...");
                ncomp.setTicker("Hurraaaaaaah....");
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                ncomp.setChannelId(CHANNEL_ID);
                ncomp.setVibrate(new long[]{200, 200});
                ncomp.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                nManager.notify((int) System.currentTimeMillis(), ncomp.build());
            }
        }
        else{
            // If the app is in background, firebase itself handles the notification
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleDataMessage(String title,String desc,String serviceflag,RemoteMessage remoteMessage) {
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            String message = "";
            if(serviceflag.equalsIgnoreCase("yes")){
                message = remoteMessage.getData().get("message");
                pushNotification.putExtra("message", message);
                triggerAlarm();
                storeDataMessage(message);
            }
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                nManager.createNotificationChannel(mChannel);
                ncomp.setContentTitle(title);
                ncomp.setContentText(desc);
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                ncomp.setChannelId(CHANNEL_ID);
                ncomp.setVibrate(new long[]{200, 200});
                ncomp.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                nManager.notify((int)System.currentTimeMillis(),ncomp.build());
            }
            else {
                ncomp.setContentTitle(title);
                ncomp.setContentText(desc);
                ncomp.setSmallIcon(R.drawable.ic_launcher_background);
                ncomp.setAutoCancel(true);
                ncomp.setChannelId(CHANNEL_ID);
                ncomp.setVibrate(new long[]{200, 200});
                ncomp.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                nManager.notify((int) System.currentTimeMillis(), ncomp.build());
            }
    }

    private void triggerAlarm() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                alarmIntent, 0);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, 0,60000, pendingIntent);
    }

    private void storeDataMessage(String message){
        SharedPrefManager.getInstance(getApplicationContext()).saveDataMesssage(message);
    }
}
