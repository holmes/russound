package org.holmes.russound

import org.holmes.russound.serial.SerialCommandReceiver
import org.holmes.russound.serial.SerialCommandSender
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream
import java.util.Random

private val LOG = LoggerFactory.getLogger(Russound::class.java)

// Being lazy and just forcing this over serial sockets.
@Suppress("unused")
fun Russound.Companion.mockReceiver(inputStream: InputStream, outputStream: OutputStream): SerialCommandReceiver {
  val commandSender = SerialCommandSender(outputStream)
  val senderQueue = RussoundSenderQueue("RECEIVER", commandSender)

  val currentZoneInfo = CurrentZoneInformation(senderQueue)
  val receiverHandlers = RussoundCommandHandlers("RECEIVER", currentZoneInfo, handlers())

  val translator = RussoundTranslator(receiverHandlers)
  return SerialCommandReceiver(translator, inputStream)
}

fun handlers(): Set<RussoundActionHandler> = setOf(
    PowerAction(),
    RequestStatusAction(),
    SetSourceAction(),
    VolumeDownAction(),
    VolumeUpAction(),
    VolumeSetAction()
)

private class CurrentZoneInformation(val senderQueue: RussoundSenderQueue) : RussoundZoneInfoListener {
  val zoneInfo: Array<ZoneInfo>

  init {
    val random = Random()
    zoneInfo = Array(6) { i ->
      ZoneInfo(i, random.nextInt(6), random.nextBoolean(), random.nextInt(100), random.nextInt(20) - 10, random.nextInt(20) - 10, random.nextInt(20) - 10, random.nextBoolean())
    }

    LOG.info("Started with zones:")
    zoneInfo.forEach {
      LOG.info("Zone: ${it.zone}, Source: ${it.source}, power: ${it.power}, volume: ${it.volume}")
    }
  }

  override fun onNext(action: RussoundAction) {
    // Update and store the zone.
    val currentZoneInfo = zoneInfo[action.zone]
    val newZoneInfo = action.applyTo(currentZoneInfo)
    zoneInfo[action.zone] = newZoneInfo

    // Generate the response and send action.
    val response = action.generateResponse(newZoneInfo)
    senderQueue.sendCommand(response)
  }

  override fun updated(zoneInfo: ZoneInfo) {
    throw IllegalStateException("We're the receiver, we send these updates we don't receive them!")
  }
}
