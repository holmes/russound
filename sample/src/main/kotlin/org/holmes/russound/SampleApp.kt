package org.holmes.russound

import org.holmes.russound.serial.SerialCommandReceiver
import org.holmes.russound.serial.SerialCommandSender
import java.io.File

class SampleApp : RussoundZoneInfoListener {
  fun init() {
    // Auto-discover the file or use /dev/null.
    val file = Russound.autoDiscoverTTY() ?: File("/dev/null")

    val sender = SerialCommandSender.fromFile(file)
    val config = RussoundConfig(zoneCount = 6, sourceCount = 4, commandSender = sender, zoneInfoListener = this)

    val audioManager = Russound.build(config)
    val receiver = SerialCommandReceiver(audioManager, file.inputStream())

    receiver.start()
    audioManager.initialize()
  }

  override fun onNext(action: RussoundAction) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun updated(zoneInfo: ZoneInfo) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

fun main(args: Array<String>) {
  SampleApp().init()
}
