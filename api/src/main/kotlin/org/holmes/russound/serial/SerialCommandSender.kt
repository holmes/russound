package org.holmes.russound.serial

import org.holmes.russound.RussoundCommandSender
import java.io.File
import java.io.OutputStream

class SerialCommandSender(val outputStream: OutputStream) : RussoundCommandSender {
  override fun send(byteArray: ByteArray) {
    outputStream.write(byteArray)
    outputStream.flush()
  }

  companion object Factory {
    /**
     * Attempts to auto-discover a serial-usb converter at /dev/ttyUSBXX
     * (or whatever template you pass).
     *
     * TODO: should we use serial/manufacturer number to search?
     *
     * @return a SerialCommandSender if it finds something we can use, otherwise null.
     */
    fun autoDiscoverTTY(pathInDev: String = "ttyUSB"): RussoundCommandSender? {
      val file = File("/dev")
          .walkTopDown()
          .firstOrNull { it.name.contains(pathInDev) }

      return if (file == null) null else fromFile(file)
    }

    fun fromFile(file: File): RussoundCommandSender {
      return SerialCommandSender(file.outputStream())
    }
  }
}
