package com.thejholmes.russound.serial

import com.thejholmes.russound.RussoundCommandSender
import java.io.File
import java.io.OutputStream

class SerialCommandSender(private val outputStream: OutputStream) : RussoundCommandSender {
  override fun send(byteArray: ByteArray) {
    outputStream.write(byteArray)
    outputStream.flush()
  }

  companion object Factory {
    fun fromFile(file: File): RussoundCommandSender = SerialCommandSender(file.outputStream())
  }
}
