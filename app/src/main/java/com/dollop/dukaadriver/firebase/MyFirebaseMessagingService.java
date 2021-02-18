package com.dollop.dukaadriver.firebase;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.activity.HomeActivity;
import com.dollop.dukaadriver.retrofit.ApiClient;
import com.dollop.dukaadriver.retrofit.ApiInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Service activity = MyFirebaseMessagingService.this;
    private String action, title;
    private String notificationTitle;
    private String messageBody;

    String NOTIFICATION_CHANNEL_ID = "Cambio";
    NotificationManager manager;

    Random random = new Random();
    SessionManager sessionManager;
    public static final String PUSH_NOTIFICATIONCASH = "notification";
    int m;
    private String CustomerName;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "From:notification:: " + remoteMessage.getNotification());
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                m = random.nextInt(9999 - 1000) + 1000;
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

    }

    /*   {
           data={
                   "image": null,
               "is_background": null,
               "user_type": null,
               "payload": null,
               "action": "order_accepted",
               "title": "Order Accepted",
               "message": {
           "msg": "Your order is Accepted"
       },
           "timestamp": "2020-10-17 16:08:37"
     }
       }*/
    private void handleDataMessage(JSONObject json) {
        try {

            JSONObject data = json.getJSONObject("data");
            action = data.getString("action");
            title = data.getString("title");
            messageBody = data.getString("message");
            Utils.E("action::" + action);
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("Message", title);
                pushNotification.putExtra("body", messageBody);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                new NotificationUtils(getApplicationContext()).playNotificationSound();

            }
            sendNotification(json);

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    @Override
    public void onNewToken(String token) {
        Utils.E("Refreshed token: service : " + token);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setTokenFCM(token);

        if (sessionManager.isLoggedIn()) {
            //  updateDriverFcm(token,sessionManager.getRegisterUser().getId());

        }

    }

    public static void GenerateToken(final Context context) {

        FirebaseApp.initializeApp(context);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("MSG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // SavedData.saveFirebaseToken(token);
                        Log.e("token", "token>>" + token);
                        //  sessionManager.setTokenFCM(token);


                    }
                });


    }


    private void sendNotification(JSONObject jsonObject) {

        try {
            JSONObject data = jsonObject.getJSONObject("data");
            action = data.getString("action");
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            /*
    {
        data={
                "image": null,
            "is_background": null,
            "user_type": null,
            "payload": null,
            "action": "New order",
            "title": "New order",
            "message": {
        "msg": "New order"
    },
        "timestamp": "2020-10-15 17:09:06"
  }
    }*/

            Utils.E("action::" + action);
            notificationTitle = data.getString("title");
            JSONObject obj = data.getJSONObject("message");
            messageBody = obj.getString("msg");


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "Notification", NotificationManager.IMPORTANCE_MAX);
                notificationChannel.setDescription("Testing");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.YELLOW);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);

                manager.createNotificationChannel(notificationChannel);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                new NotificationCompat.Builder(MyFirebaseMessagingService.this, NOTIFICATION_CHANNEL_ID);

                Intent intent1 = new Intent(getApplicationContext(), HomeActivity.class);
                //   intent1.putExtras(bundle);
                // startActivity(intent1);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123,
                        intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),
                        "id_product")
                        .setSmallIcon(R.drawable.notification_icon) //your app icon//your app icon
                        .setChannelId(NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(notificationTitle)
                        .setContentText(messageBody)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody)).setVibrate(new long[]{0, 1000, 500, 1000})
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                         .setColor(getResources().getColor(R.color.colorBlue))
                        .setWhen(System.currentTimeMillis());
                manager.notify(m, notificationBuilder.build());
            } else {

                Intent notificationIntent = new Intent(MyFirebaseMessagingService.this, HomeActivity.class);
                //  notificationIntent.putExtras(bundle);
                PendingIntent contentIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,
                        0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                        .setContentTitle(notificationTitle)
                        .setContentText(messageBody)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody)).setVibrate(new long[]{0, 1000, 500, 1000})
                        .setSmallIcon(R.drawable.notification_icon)
                        .setColor(getResources().getColor(R.color.colorBlue))
                        .setContentIntent(contentIntent).setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                manager.notify(m, notificationBuilder.build());
            }

        } catch (JSONException e) {
            Utils.E("JSonexception::" + e);
            e.printStackTrace();
        }

    }

}

