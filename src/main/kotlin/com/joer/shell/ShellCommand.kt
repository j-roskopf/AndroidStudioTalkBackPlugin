package com.joer.shell

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.util.Key
import com.joer.util.Logger
import java.util.concurrent.ExecutionException

class ShellCommand {

    fun executeCommand(command: String, callback: Callback? = null) {
        val outContent = StringBuilder()
        val errContent = StringBuilder()
        val splitCommand: MutableList<String> = if (command.contains(" ")) command.split(" ").toMutableList() else mutableListOf(command)
        val commandLine = GeneralCommandLine(splitCommand)
        val handler = OSProcessHandler(commandLine)

        try {
            handler.addProcessListener(object : ProcessListener {

                override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {
                    // no-op
                }

                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    when {
                        outputType === ProcessOutputTypes.STDERR -> {
                            errContent.append(event.text)
                        }
                        outputType === ProcessOutputTypes.SYSTEM -> {
                            // skip
                        }
                        else -> {
                            outContent.append(event.text)
                        }
                    }
                }

                override fun processTerminated(event: ProcessEvent) {
                    callback?.terminatedWithContent(outContent.toString())
                    handler.removeProcessListener(this)
                }

                override fun startNotified(event: ProcessEvent) {
                    // no-op
                }
            })
            handler.startNotify()
        } catch (e: ExecutionException) {
            Logger.e(this, outputErrorLog("error", e, outContent, errContent))
        } catch (e: InterruptedException) {
            Logger.e(this, outputErrorLog("timeout", e, outContent, errContent))
        } catch (e: IllegalThreadStateException) {
            Logger.e(this, outputErrorLog("timeout", e, outContent, errContent))
        }
    }

    private fun outputErrorLog(cause: String, exception: Exception, output: StringBuilder, errorOutput: StringBuilder): String {
        val str = "Command $cause: ${exception.message} \n" +
            "${exception.stackTrace.joinToString("\n")}\n" +
            " output:$output\n" +
            " error:$errorOutput"
        Logger.e(this, str)
        return str
    }
}

interface Callback {
    fun terminatedWithContent(content: String)
}
