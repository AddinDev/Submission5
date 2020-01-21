package com.addindev.pastopasto.notifications.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.addindev.pastopasto.MainBotNavActivity
import com.addindev.pastopasto.MovieCatalogue
import com.addindev.pastopasto.R
import com.addindev.pastopasto.notifications.services.pojo.ResponseMovieRelease
import com.addindev.pastopasto.ui.movie.pojo.ResultsItem
import java.text.SimpleDateFormat
import java.util.*

class ReleaseMovieReminder : BroadcastReceiver(){

    var CHANNEL_ID = "channel_02"
    var CHANNEL_NAME: CharSequence = "addin channel"

    override fun onReceive(context: Context?, intent: Intent?) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        val currentDate = sdf.format(date)

        getReleaseMovie(currentDate, context!!)
        notifId = intent!!.getIntExtra("id", 0)
    }

    fun getReleaseMovie(date:String, context: Context){
            AndroidNetworking.get("https://api.themoviedb.org/3/discover/movie")
                    .addQueryParameter("api_key",MovieCatalogue.MOVIE_DB_API_KEY)
                    .addQueryParameter("primary_release_date.gte",date)
                    .addQueryParameter("primary_release_date.lte",date)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(ResponseMovieRelease::class.java, object : ParsedRequestListener<ResponseMovieRelease> {
                        override fun onResponse(response: ResponseMovieRelease?) {

                            Log.d("IDN", response?.results?.size.toString())
                            if (response?.results?.size == 0) {
                                showAlarmNotification(context, "Tidak Ada Data", notifId)
                            } else {
                                for (i in response?.results!!) {
                                    showAlarmNotification(context, i.title, i.id)
                                }
                            }
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("IDN",anError.toString())
                        }
                    })
    }

    fun setRepeatingAlarm(context: Context) {

        cancelAlarm(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, ReleaseMovieReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    getPendingIntent(context)
            )
        }

        Toast.makeText(context, context.getString(R.string.daily_release_reminder), Toast.LENGTH_SHORT).show()

    }

    private fun showAlarmNotification(context: Context, title: String?, notifId: Int) {

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d(ContentValues.TAG, "showAlarmNotification: $notifId")
        val intent = Intent(context, MainBotNavActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_local_movies_black_24dp)
                .setContentTitle(title)
                .setContentText("Today $title release")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(getPendingIntent(context))
    }

    companion object {

        private val NOTIF_ID_REPEATING = 101
        private var notifId: Int = 0

        private fun getPendingIntent(context: Context): PendingIntent {

            val intent = Intent(context, ReleaseMovieReminder::class.java)
            return PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        }
    }

}