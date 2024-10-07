package com.joer.util

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ProjectManager

object NotificationCreator {
    fun showDevicesErrorNotification(content: String) {
        val notification = Notification(
            "AdbTalkBack",
            "Error",
            content,
            NotificationType.ERROR
        )

        notification.notify(ProjectManager.getInstance().openProjects[0])
    }
}
