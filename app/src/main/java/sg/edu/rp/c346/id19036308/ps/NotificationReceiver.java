package sg.edu.rp.c346.id19036308.ps;

import android.app.Notification;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.core.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;


public class NotificationReceiver extends BroadcastReceiver {

    int reqCode = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

         long id = intent.getLongExtra("id",0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, reqCode, i , PendingIntent.FLAG_CANCEL_CURRENT);

        // This NotificationCompat Action code is for Watch Notification
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Launch Task Manager", pIntent).build();

        Intent intentReply = new Intent(context, MainActivity.class);
        intent.putExtra("id", id);

        // Get Notification
        PendingIntent pendingIntentReply = PendingIntent.getActivity(context, 0, intentReply, PendingIntent.FLAG_UPDATE_CURRENT);

        // Design the reply area
        RemoteInput ri = new RemoteInput.Builder("status").setLabel("Status report").setChoices(new String[] {"Completed", "Not yet"}).build();

        //Passing intent reply over to next activity
        NotificationCompat.Action action1 = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Reply", pendingIntentReply).addRemoteInput(ri).build();


        Intent intentReply2 = new Intent(context, AddActivity.class);
        intent.putExtra("id", id);

        PendingIntent pendingIntentReply2 = PendingIntent.getActivity(context, 0, intentReply2, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteInput ri2 = new RemoteInput.Builder("status2").setLabel("Status report").build();
        NotificationCompat.Action action2 = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Add", pendingIntentReply2).addRemoteInput(ri2).build();


        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
        extender.addAction(action);
        extender.addAction(action1);
        extender.addAction(action2);




//        String text = context.getString(R.string.basic_notify_msg);
//        String title = context.getString(R.string.notification_title);
        // Intermediate enhancement
        Bitmap picture = BitmapFactory.decodeResource(context.getResources(), R.drawable.friday);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.directdownload);
        NotificationCompat.BigPictureStyle bigPicture = new NotificationCompat.BigPictureStyle();
        bigPicture.setBigContentTitle("Task");
        bigPicture.bigPicture(picture);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }

        //Build Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setContentTitle("Task");
        DBHelper dbHelper = new DBHelper(context.getApplicationContext());
        ArrayList<Task> data = dbHelper.getTasks();
//        for (int a = 0; a < data.size(); a++) {
//            builder.setContentText(dbHelper.getTasks().get(a).getName());
//
//        }

        builder.setContentText(dbHelper.getTasks().get(data.size() - 1).getName() + "\n" +dbHelper.getTasks().get(data.size() - 1).getDescription());

        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
//        builder.setStyle(bigPicture);
//        builder.setLargeIcon(largeIcon);
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);
        Notification notification = new Notification();
        notification.ledARGB = 0xff0000ff; // Blue color light flash
        notification.ledOnMS = 2000; // LED is on for 2 second
        notification.ledOffMS = 500; // LED is off for 0.5 second
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
//        notificationManager.notify(0, notification);
        builder.extend(extender);
        builder.setLights(Color.CYAN, 5000, 5000);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        builder.setPriority(Notification.PRIORITY_HIGH);

        notification = builder.build();
        notificationManager.notify(123,notification);
    }
}