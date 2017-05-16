package org.holmes.russound

import org.holmes.russound.serial.SerialCommandReceiver
import org.holmes.russound.serial.SerialCommandSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.Socket

private val LOG = LoggerFactory.getLogger(SampleApp::class.java)

class SampleApp : RussoundZoneInfoListener {
  val zones = HashMap<Int, ZoneInfo>()
  val logger: Logger = LoggerFactory.getLogger(SampleApp::class.java)

  fun initReceiver() {
    MockRussoundReceiverThread().start()
  }

  fun initApplication(): RussoundCommander {
    // Connect to a local matrix over a socket.
    val socket = Socket("127.0.0.1", MATRIX_CONNECTION_PORT)
    LOG.info("Connected to socket on localhost:$MATRIX_CONNECTION_PORT")

    val sender = SerialCommandSender(socket.getOutputStream())
    val commander = Russound.sender(sender)

    val translator = Russound.receiver(this)
    val receiver = SerialCommandReceiver(translator, socket.getInputStream())

    // Start listening and then get status updates!
    receiver.start()
    StatusRequestTimer(commander, zoneCount = 6).start()

    return commander
  }

  override fun onNext(action: RussoundAction) {
    logger.info("Received action: ${action.description}")
  }

  override fun updated(zoneInfo: ZoneInfo) {
    zones.put(zoneInfo.zone, zoneInfo)
  }
}

fun main(args: Array<String>) {
  val app = SampleApp()

  app.initReceiver()
  Thread.sleep(1000)
  app.initApplication()
}
