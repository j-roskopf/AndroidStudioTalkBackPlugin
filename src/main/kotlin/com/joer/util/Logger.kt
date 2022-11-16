package com.joer.util

import com.intellij.notification.*
import com.intellij.notification.impl.NotificationGroupEP
import java.text.SimpleDateFormat
import java.util.*

val hhmmddFormat: SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
fun Date.toHHMMDD(): String = hhmmddFormat.format(this)

object Logger {

    private const val debug = false

    private val notificationGroup = NotificationGroup(
        displayId = "logger",
        displayType = NotificationDisplayType.NONE,
        isLogByDefault = true,
        toolWindowId = null,
        icon = null
    )


    @JvmStatic
    fun d(clazz: Any, message: String = "") {
        if (!debug) return
        output(clazz, message, "debug")
    }

    @JvmStatic
    fun e(clazz: Any, message: String) {
        output(clazz, message, "error")
    }

    private fun output(clazz: Any, message: String, debugType: String) {
        val output = "[${Date().toHHMMDD()}] (${clazz.javaClass.simpleName}/$debugType) $message"
        if (debug) {
            val notificationType = when (debugType) {
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