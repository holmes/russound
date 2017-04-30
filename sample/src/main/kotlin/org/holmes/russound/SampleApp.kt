package org.holmes.russound

import org.holmes.russound.serial.SerialCommandReceiver
import org.holmes.russound.serial.SerialCommandSender
import java.io.File

class SampleApp : RussoundZoneInfoListener {
  fun init() {
    // Auto-discover the file or use /dev/null.
    val file = Russound.autoDiscoverTTY() ?: File("/dev/null")

    val sender = SerialCommandSender.fromFile(file)
    val config = RussoundConfig(zoneCount = 6, sourceCount = 6, commandSender = sender, zoneInfoListener = this)

    val audioManager = Russound.build(config)
    val receiver = SerialCommandReceiver(audioManager, file.inputStream())

    receiver.start()
    audioManager.initialize()

    // Get them status updates!
    StatusRequestTimer(audioManager, config).start()
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
