package com.addindev.pastopasto.notifications.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.addindev.pastopasto.MainBotNavActivity
import com.addindev.pastopasto.R
import java.util.*

class DailyAlarmReceiver : BroadcastReceiver(){

    var CHANNEL_ID = "channel_01"
    var CHANNEL_NAME: CharSequence = "addin_channel"


    override fun onReceive(context: Context?, intent: Intent?) {
        val message = context?.getString(R.string.txt_repeat_notif)
        val title = context?.getString(R.string.app_name)
        showAlarmNotification(context!!, title!!, message!!, NOTIF_ID_REPEATING)
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(context, MainBotNavActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel)
            }
        }


        notificationManagerCompat.notify(notifId, builder.build())


    }

    fun setRepeatingAlarm(context: Context) {
        cancelAlarm(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, DailyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.set(Calendar.HOUR_OF_DAY, 7)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    getPendingIntent(context)
            )
        }

        Toast.makeText(context, context.getString(R.string.daily_open_reminder), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        alarmManager.cancel(pendingIntent)
    }

    companion object {

        private const val NOTIF_ID_REPEATING = 101

        private fun getPendingIntent(context: Context): PendingIntent {

            val intent = Intent(context, DailyAlarmReceiver::class.java)
            return PendingIntent.getBroadcast(context, NOTIF_ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        }
    }

}