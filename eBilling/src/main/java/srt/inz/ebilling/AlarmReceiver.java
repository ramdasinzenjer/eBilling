package srt.inz.ebilling;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN) @SuppressLint("NewApi") public class AlarmReceiver extends BroadcastReceiver {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
		
    	
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		// this is it, we'll build the notification!
		// in the addAction method, if you don't want any icon, just set the first param to 0
		Notification mNotification = new Notification.Builder(context)
			
			.setContentTitle("Electricity Bill")
			.setContentText("Pettenn Adaykk Illenkil FUSE Oorum !")
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(pIntent)
			.setSound(soundUri)
			
			.addAction(R.drawable.ic_launcher, "View", pIntent)
			.addAction(0, "Stop", pIntent)
			
			.build();
		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		// If you want to hide the notification after it was selected, do the code below
		// myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notificationManager.notify(0, mNotification);
		
		Toast.makeText(context, "Alarm triggered", Toast.LENGTH_LONG).show();
		
	}

}