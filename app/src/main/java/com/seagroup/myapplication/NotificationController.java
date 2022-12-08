package com.seagroup.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LongSparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import java.util.ArrayList;

public class NotificationController {
    public static final String CHANNEL_MESSAGE = "mesgge_test";
    private static NotificationController controller = new NotificationController();

    public static NotificationController getInstance(Context context) {
        return controller;
    }
    public void createNotification(Context context) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                CHANNEL_MESSAGE);
        builder.setTicker("xm: 姨姨我");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setNumber(4);
        builder.setAutoCancel(true);
        builder.setWhen(1670471481 * 1000);
        int flagSoundOn = Notification.DEFAULT_SOUND;
        int flagVibrateOn = Notification.DEFAULT_VIBRATE;
        int flagLightOn = Notification.DEFAULT_LIGHTS;
        builder.setDefaults(flagSoundOn | flagVibrateOn | flagLightOn);

        NotificationManager notificationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            notificationManager = (NotificationManager) context
                    .getSystemService(NotificationManager.class);
        }
        //fix crash in some device the notification manager may be null (ploytron w8480)
        if (notificationManager == null) {
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                12,
                new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.setContentIntent(pendingIntent);

        builder.setContentTitle("Damien Wang, user0808, HuangXiaomin (黄晓敏) | Android | SeaTalk | 11F-N-T-4");
        builder.setContentText("xm: 姨姨我");
        // build the avatar
//        Bitmap bitmap = buildChatNotificationAvatar(chat.avatar, chat.chatType == ChatMessage.ChatType.BUDDY_RECEIVE);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.seatalk_icon);
        builder.setLargeIcon(bitmap);
        // build the messaging style for Android 7 and higher


        ArrayList<MessageData> messageList = new ArrayList<>(2);
        messageList.add(new MessageData("姨姨我",1670471481, 125825, "xm", R.mipmap.ic_launcher));
        messageList.add(new MessageData("给我回我", 1670473402, 125827, "kl", R.drawable.seatalk_icon));

        builder.setStyle(buildMessagingStyle(messageList, context));

        initMessageChannel(context, CHANNEL_MESSAGE);
        notificationManager.notify(-583089544, builder.build());
    }

    private void initMessageChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel != null) {
                return;
            }

            channel = new NotificationChannel(
                    channelId,
                    context.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(context.getString(R.string.app_name));
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private NotificationCompat.MessagingStyle buildMessagingStyle(ArrayList<MessageData> messageList, Context context) {
        Person user = new Person.Builder().setName(" ").build();
        NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(user);
        style.setConversationTitle("Damien Wang, user0808, HuangXiaomin (黄晓敏) | Android | SeaTalk | 11F-N-T-4");
        style.setGroupConversation(true);

        if (messageList != null && !messageList.isEmpty()) {
            LongSparseArray<Person> cachePerson = new LongSparseArray<>();
            for (MessageData messageData : messageList) {
                if (cachePerson.get(messageData.getUserId()) == null) {
                    Person.Builder personBuilder = new Person.Builder().setKey(String.valueOf(messageData.getUserId())).setName(messageData.getUserName());
//                    Bitmap bitmap = buildChatNotificationAvatar(messageData.getAvatar(), true);
//                    if (bitmap != null) {
//                        personBuilder.setIcon(IconCompat.createWithBitmap(bitmap));
//                    }
                    personBuilder.setIcon(IconCompat.createWithResource(context, messageData.getAvator()));
                    cachePerson.put(messageData.getUserId(), personBuilder.build());
                }
            }

            for (MessageData messageData : messageList) {
                style.addMessage(messageData.getMsg(), messageData.getTimestampSecond() * 1000, cachePerson.get(messageData.getUserId()));
            }
        }

        return style;
    }
}
