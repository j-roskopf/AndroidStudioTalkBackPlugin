package com.joer.util

import com.joer.shell.Callback
import com.joer.shell.ShellCommand
import java.util.regex.Matcher
import java.util.regex.Pattern

object ConnectedDevice {
    private val shellCommand = ShellCommand()

    fun checkConnectedDevices(callback: ConnectedDeviceCallback) {
        shellCommand.executeCommand("adb devices", object : Callback {
            override fun terminatedWithContent(content: String) {
                val pattern: Pattern = Pattern.compile("\\bdevice\\b", Pattern.CASE_INSENSITIVE)
                val matcher: Matcher = pattern.matcher(content)
                var count = 0
                while (matcher.find()) count++
                when {
                    count == 1 -> {
                        callback.deviceConnected()
                    }
                    count > 1 -> {
                        callback.tooManyDevices()
                    }
                    count == 0 -> {
                        callback.notEnoughDevices()
                    }
                    else -> {
                        callback.generalError()
                    }
                }
            }
        })
    }
}

interface ConnectedDeviceCallback {
    fun deviceConnected()
    fun tooManyDevices()
    fun notEnoughDevices()
    fun generalError()
}