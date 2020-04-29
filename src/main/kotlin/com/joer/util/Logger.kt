package com.joer.util

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import java.text.SimpleDateFormat
import java.util.*

val hhmmddFormat : SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
fun Date.toHHMMDD() : String = hhmmddFormat.format(this)

object Logger {
    private val debug = false
    val notificationGroup = NotificationGroup("logger", NotificationDisplayType.NONE, true)

    @JvmStatic
    fun d(clazz: Any, message: String = ""){
        if(!debug) return
        output(clazz, message, "debug")
    }

    @JvmStatic
    fun e(clazz: Any, message: String){
        output(clazz, message, "error")
    }

    private fun output(clazz: Any, message: String, debugType: String){
        val output = "[${Date().toHHMMDD()}] (${clazz.javaClass.simpleName}/$debugType) $message"
        if (debug) {
            val notificationType = when(debugType) {
                "debug" -> NotificationType.INFORMATION
                "error" -> NotificationType.ERROR
                else -> NotificationType.INFORMATION
            }
            Notifications.Bus.notify(
                notificationGroup.createNotification(output, notificationType)
            )
        }
        println(output)
    }
}