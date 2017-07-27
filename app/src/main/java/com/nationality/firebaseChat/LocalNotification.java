package com.nationality.firebaseChat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.nationality.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LocalNotification 
{
    public static void sendmyNotification(Context context, String Tittle, String Subject, String Body, int icon) 
    {
    	/*String tittle="Partha";
        String subject="Test Notification";
        String body="This is a Local Notification for testing";*/
        
        /*

        From Save Solution

        NotificationManager notif=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification(icon,Tittle,System.currentTimeMillis());
        notify.defaults |= Notification.DEFAULT_SOUND;
        notify.defaults |= Notification.DEFAULT_VIBRATE;
        PendingIntent pending= PendingIntent.getActivity(context, 0, new Intent(), 0);
        
        notify.setLatestEventInfo(context,Subject,Body,pending);
        notif.notify(0, notify);*/



        Notification notification = new Notification();
        PendingIntent pending= PendingIntent.getActivity(context, 0, new Intent(), 0);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification();
            notification.icon = R.mipmap.ic_launcher;
            try {
                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                deprecatedMethod.invoke(notification, context, Subject, null, pending);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                e.printStackTrace();
                //Log.w(TAG, "Method not found", e);
            }
        } else {
            // Use new API
            Notification.Builder builder = new Notification.Builder(context)
                    .setContentIntent(pending)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(Subject)
                    .setContentText(Body);
                notification = builder.build();
        }

	}


	// Call
	//LocalNotification.sendmyNotification(this, "Upload In Progress", "", "Thank You For Your Patience..", R.drawable.finish);
}
