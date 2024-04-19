package ir.kitgroup.inskuappb.firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import ir.kitgroup.inskuappb.R;
import ir.kitgroup.inskuappb.ui.activities.MainActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {

            sendNotification(this,
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    @Override
    @SuppressLint("CommitPrefEdits")
    public void onNewToken(@NonNull String token) {

        try {

            if (token != null && token != "") {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor preferencesEditor = PreferenceManager
                        .getDefaultSharedPreferences(this).edit();

                if (preferences.getString("access_token", "").equals("")) {

                    preferencesEditor.putString("access_token", token).apply();
                } else {

                    preferencesEditor.putString("new_token", token).apply();
                }
            }
        } catch (Exception ignored) {
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void sendNotification(Context context, String title, String messageBody) {

        try {

            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.putExtra("notification", "1");
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent contentIntent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);
            } else {

                contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
            }

            String channelId = "pakhshyab_channel";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setSubText("پنل دود")
                    .setSmallIcon(R.drawable.ic_logo)
                    .setColor(ContextCompat.getColor(context, R.color.color_primary))
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
            }

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel(channelId,
                        "PakhshYab_Channel", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(new Random().nextInt(100000), notificationBuilder.build());
        } catch (Exception ignored) {
        }
    }
}