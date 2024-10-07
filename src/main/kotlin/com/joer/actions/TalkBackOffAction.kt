package com.joer.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.joer.shell.ShellCommand
import com.joer.util.ConnectedDevice
import com.joer.util.ConnectedDeviceCallback
import com.joer.util.NotificationCreator

class TalkBackOffAction : AnAction() {

    private val shellCommand = ShellCommand()

    override fun actionPerformed(e: AnActionEvent) {
        ConnectedDevice.checkConnectedDevices(object : ConnectedDeviceCallback {
            override fun deviceConnected() {
                val command =
                    "adb shell settings put secure enabled_accessibility_services com.android.talkback/com.google.android.marvin.talkback.TalkBackService"
                shellCommand.executeCommand(command)
            }

            override fun tooManyDevices() {
                NotificationCreator.showDevicesErrorNotification("It looks like there are too many devices connected")
            }

            override fun notEnoughDevices() {
                NotificationCreator.showDevicesErrorNotification("It doesn't look like any devices are connected")
            }

            override fun generalError() {
                NotificationCreator.showDevicesErrorNotification("Something went wrong")
            }
        })
    }
}
