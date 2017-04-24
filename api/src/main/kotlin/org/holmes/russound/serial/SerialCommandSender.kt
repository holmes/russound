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
    fun fromFile(file: File): RussoundCommandSender {
      return SerialCommandSender(file.outputStream())
    }
  }
}
