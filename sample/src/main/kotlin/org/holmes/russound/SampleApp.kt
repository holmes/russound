package org.holmes.russound

import org.holmes.russound.serial.SerialCommandReceiver
import org.holmes.russound.serial.SerialCommandSender
import java.io.File

class SampleApp : RussoundZoneInfoListener {
  fun init() {
    // Auto-discover the file or use /dev/null.
    val file = Russound.autoDiscoverTTY() ?: File("/dev/null")

    val sender = SerialCommandSender.fromFile(file)
    val commander = Russound.sender(sender)

    val translator = Russound.receiver(this)
    val receiver = SerialCommandReceiver(translator, file.inputStream())

    // Start listening and then get status updates!
    receiver.start()
    StatusRequestTimer(commander, zoneCount = 6).start()
  }

  override fun onNext(action: RussoundAction) {
    TODO("not implemented")
  }

  override fun updated(zoneInfo: ZoneInfo) {
    TODO("not implemented")
  }
}

fun main(args: Array<String>) {
  SampleApp().init()
}
