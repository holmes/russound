package org.holmes.russound

import java.io.File

/** Configuration object used to build a Russound. */
data class RussoundConfig(val zoneCount: Int,
                          val sourceCount: Int,
                          val commandSender: RussoundCommandSender,
                          val zoneInfoListener: RussoundZoneInfoListener
)

/** Sends data from the app to the Russound matrix. */
interface RussoundCommandSender {
  fun send(byteArray: ByteArray)
}

/**
 * Implement this to receive callbacks about zone info updates. These occur periodically and
 * after each command is sent to the matrix.
 */
interface RussoundZoneInfoListener {
  fun onNext(action: RussoundAction)

  fun updated(zoneInfo: ZoneInfo)
}

class Russound {
  companion object {
    /**
     * Attempts to auto-discover a serial-usb converter at /dev/ttyUSBXX
     * (or whatever template you pass).
     *
     * TODO: should we use serial/manufacturer number to search?
     *
     * @return a SerialCommandSender if it finds something we can use, otherwise null.
     */
    fun autoDiscoverTTY(pathInDev: String = "ttyUSB"): File? {
      return File("/dev")
          .walkTopDown()
          .firstOrNull { it.name.contains(pathInDev) }
    }

    fun build(config: RussoundConfig): RussoundAudioManager {
      // The output.
      val audioQueue = RussoundAudioQueue("app-queue", config.commandSender)
      val commands = RussoundCommands()

      // And set up the input.
      val actionHandler = ReceivedStatusActionHandler()
      val commandReceiver = RussoundCommandHandlers("app-receiver", config.zoneInfoListener,
          setOf(actionHandler))

      // Return the audioManager which is what the end-user wants.
      return RussoundAudioManager(audioQueue, commands, commandReceiver)
    }
  }
}
