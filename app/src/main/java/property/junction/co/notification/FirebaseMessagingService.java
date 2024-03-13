package property.junction.co.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import property.junction.co.Dashboard;
import property.junction.co.R;
import property.junction.co.methods.InFirebase;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    NotificationManager mNotificationManager;
    int NOTIFICATION_ID = 100;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        if (remoteMessage == null) {
            return;
        }
        Log.e(">>>>>>>>>>>>>", "WORKING === WORKING === WORKING === WORKING === WORKING === WORKING === WORKING === WORKING === WORKING === WORKING === WORKING");


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getMessageId() != null) {
                NOTIFICATION_ID = Integer.parseInt(remoteMessage.getMessageId());
            }

            Uri notification_sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.tap_tone);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification_sound);
            //r.play();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                r.setLooping(false);
            }

            // vibration
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {100, 300, 300, 300};
            v.vibrate(pattern, -1);

        /*int resourceImage = getResources().getIdentifier(
                remoteMessage.getNotification().getIcon(), "drawable", getPackageName());*/
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "regular_notification");
            //builder.setSmallIcon(R.drawable.logo);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setSmallIcon(R.drawable.vector_property_junction_logo);
                builder.setColor(getResources().getColor(R.color.app_theme_color));
            } else {
                builder.setSmallIcon(R.drawable.vector_property_junction_logo);
            }
            Intent resultIntent = new Intent(this, Dashboard.class);
            PendingIntent pendingIntent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pendingIntent = PendingIntent.getActivity(this, 1
                        , resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }

            builder.setContentTitle(remoteMessage.getNotification().getTitle());
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_property_junction_logo));
            builder.setContentIntent(pendingIntent);
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);

            mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "regular_notification";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            // notificationId is a unique int for each notification that you must define
            mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Uri notification_sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.tap_tone);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification_sound);
                //r.play();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    r.setLooping(false);
                }

                // vibration
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100, 300, 300, 300};
                v.vibrate(pattern, -1);

        /*int resourceImage = getResources().getIdentifier(
                remoteMessage.getNotification().getIcon(), "drawable", getPackageName());*/
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "regular_notification");
                //builder.setSmallIcon(R.drawable.logo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setSmallIcon(R.drawable.vector_property_junction_logo);
                    builder.setColor(getResources().getColor(R.color.app_theme_color));
                } else {
                    builder.setSmallIcon(R.drawable.vector_property_junction_logo);
                }
                Intent resultIntent = new Intent(this, Dashboard.class);
                PendingIntent pendingIntent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = PendingIntent.getActivity(this, 1
                            , resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                }
                if (!json.getString("data_message_id").equals("") && !json.getString("data_message_id").equals("null")) {
                    NOTIFICATION_ID = Integer.parseInt(String.valueOf(json.getString("data_message_id")).replace("null", "100"));
                }
                builder.setContentTitle(json.getString("data_title").replaceAll("_", " "));
                builder.setContentText(json.getString("data_body").replaceAll("_", " "));
                int resId = this.getResources().getIdentifier(json.getString("data_large_icon"), "drawable", getPackageName());
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), resId));
                builder.setContentIntent(pendingIntent);
                //builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
                builder.setAutoCancel(true);
                builder.setPriority(Notification.PRIORITY_MAX);

                mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = "regular_notification";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    builder.setChannelId(channelId);
                }
                // notificationId is a unique int for each notification that you must define
                mNotificationManager.notify(NOTIFICATION_ID, builder.build());
            } catch (Exception e) {
                Log.e("-------------------", "-------------------------------Exception: " + e.getMessage());
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}

