package org.holmes.russound

import org.holmes.russound.serial.SerialCommandReceiver
import org.holmes.russound.serial.SerialCommandSender
import org.slf4j.LoggerFactory
import java.io.File

class SampleApp : RussoundZoneInfoListener {
  val zones = HashMap<Int, ZoneInfo>()
  val logger = LoggerFactory.getLogger(SampleApp::class.java)

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
    logger.info("Received action: ${action.description}")
  }

  override fun updated(zoneInfo: ZoneInfo) {
    zones.put(zoneInfo.zone, zoneInfo)
  }
}

fun main(args: Array<String>) {
  SampleApp().init()
}
