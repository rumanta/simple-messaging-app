package id.ac.ui.cs.williamrumanta.directnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Random;

public class ChatNotificationManager {
    Context ctx;
    NotificationManager notificationManager;
    String CHANNEL_ID = "id.ac.ui.cs.directnotification";
    String KEY_REPLY = "key_reply";
    String KEY_REPLY_HISTORY = "key_reply_history";
    public static final int NOTIFICATION_ID = 1;


    public ChatNotificationManager(Context context) {
        ctx = context;
        notificationManager =
                (NotificationManager)
                        ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(CHANNEL_ID,
                "Direct reply", "Reply channel", notificationManager);
    }

    public void handleIntent(Intent intent, List<UserMessage> messageHistory) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {

            CharSequence charSequence = remoteInput.getCharSequence(
                    KEY_REPLY);

            if (charSequence != null) {
                String reply = charSequence.toString();
                addMessageBot(messageHistory, reply);
                notificationManager.cancel(NOTIFICATION_ID);

            } else {
                String reply = remoteInput.getCharSequence(KEY_REPLY_HISTORY).toString();
                addMessageBot(messageHistory, reply);

                notificationManager.cancel(NOTIFICATION_ID);
            }
            System.out.println(messageHistory.toString());

        }
    }

    public void addMessageBot(List<UserMessage> messageHistory, String msg) {
        messageHistory.add(new UserMessage("Klaud", msg));
    }

    public void clearExistingNotifications(Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", 0);
        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    protected void createNotificationChannel(String id, String name, String description, NotificationManager notificationManager) {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel =
                new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100});


        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification(String message) {

        //Create notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setColor(ContextCompat.getColor(ctx,
                        R.color.colorPrimary))
                .setSmallIcon(
                        android.R.drawable.ic_dialog_info)
                .setContentTitle(message);

        String replyLabel = "Enter your reply here";

        //Initialise RemoteInput
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_REPLY_HISTORY)
                .setLabel(replyLabel)
                .build();


        int randomRequestCode = new Random().nextInt(54325);

        //PendingIntent that restarts the current activity instance.
        Intent resultIntent = new Intent(ctx, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Set a unique request code for this pending intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(ctx, randomRequestCode, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Notification Action with RemoteInput instance added.
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_dialog_info, "Reply", resultPendingIntent)
                .addRemoteInput(remoteInput)
                .setAllowGeneratedReplies(true)
                .build();

        //Notification.Action instance added to Notification Builder.
        builder.addAction(replyAction);

        Intent intent = new Intent(ctx, MainActivity.class);
        intent.putExtra("notificationId", NOTIFICATION_ID);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //Create Notification.
        NotificationManager notificationManager =
                (NotificationManager)
                        ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(CHANNEL_ID, "Direct Reply", "Reply channel", notificationManager);

        notificationManager.notify(NOTIFICATION_ID,
                builder.build());
    }
}
