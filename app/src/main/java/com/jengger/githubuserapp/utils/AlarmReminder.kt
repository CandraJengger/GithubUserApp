package com.jengger.githubuserapp.utils

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.jengger.githubuserapp.MainActivity
import com.jengger.githubuserapp.R
import java.util.*

class AlarmReminder : BroadcastReceiver() {

    companion object {
        private const val ID_REMINDER = 101
        private const val CHANNEL_ID = "reminder_01"
        private const val CHANNEL_NAME = "reminder channel"
    }

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun showNotification (context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(context).addParentStack(MainActivity::class.java)
            .addNextIntent(intent)
            .getPendingIntent(ID_REMINDER, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.github_transparent)
            .setContentTitle(context.resources.getString(R.string.reminder_title))
            .setContentText(context.resources.getString(R.string.reminder_message))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setSound(alarmSound)
            .setAutoCancel(true)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            }
            mBuilder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        notificationManagerCompat.notify(ID_REMINDER, notification)
    }

    fun setRepeatingReminder(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent: PendingIntent = Intent(context, AlarmReminder::class.java).let {
            PendingIntent.getBroadcast(context, ID_REMINDER, it, 0)
        }

        val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 7)
            calendar.set(Calendar.MINUTE, 56)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            intent
        )

        Toast.makeText(context, context.getString(R.string.reminder_on), Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context?){
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REMINDER, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, context.getString(R.string.reminder_off), Toast.LENGTH_SHORT).show()
    }
}