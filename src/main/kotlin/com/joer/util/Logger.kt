package com.joer.util

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import java.text.SimpleDateFormat
import java.util.Date

val hhmmddFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
fun Date.toHHMMDD(): String = hhmmddFormat.format(this)

object Logger {

    private const val debug = false

    private val notificationGroup = NotificationGroupManager.getInstance()
        .getNotificationGroup("logger")

    @JvmStatic
    fun e(clazz: Any, message: String) {
        output(clazz, message, LogType.ERROR)
    }

    private fun output(clazz: Any, message: String, logType: LogType) {
        val output = "[${Date().toHHMMDD()}] (${clazz.javaClass.simpleName}/$logType) $message"
        if (debug) {
            val notificationType = when (logType) {
                LogType.ERROR -> NotificationType.ERROR
            }
            Notifications.Bus.notify(
                notificationGroup.createNotification(output, notificationType)
            )
            println(output)
        }
    }
}

enum class LogType {
    ERROR,
}
