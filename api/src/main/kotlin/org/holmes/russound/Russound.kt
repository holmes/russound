package org.holmes.russound

import java.io.File

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

    fun sender(name: String, commandSender: RussoundCommandSender): RussoundCommander {
      val audioQueue = RussoundSenderQueue(name, commandSender)
      val commands = RussoundCommands()

      return RussoundCommander(audioQueue, commands)
    }

    fun receiver(name: String, zoneInfoListener: RussoundZoneInfoListener): RussoundTranslator {
      val actionHandler = ReceivedStatusActionHandler()
      val commandReceiver = RussoundCommandHandlers(name, zoneInfoListener,
          setOf(actionHandler))

      return  RussoundTranslator(commandReceiver)
    }

  }
}
