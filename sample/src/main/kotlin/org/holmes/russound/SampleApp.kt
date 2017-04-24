package org.holmes.russound

import org.holmes.russound.serial.SerialCommandSender
import java.io.File

class SampleApp : RussoundZoneInfoListener {
  fun init() {
    val sender = SerialCommandSender.fromFile(File("/dev/ttyUSB0"))
    val config = RussoundConfig(zoneCount = 6, sourceCount = 4, commandSender = sender, zoneInfoListener = this)
    TODO("implement me")
  }

  override fun updated(zoneInfo: ZoneInfo) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}

fun main(args: Array<String>) {
  SampleApp().init()
}
