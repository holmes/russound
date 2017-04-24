package org.holmes.russound

/** Configuration object used to build a Russound. */
data class RussoundConfig(val zoneCount: Int,
                          val sourceCount: Int,
                          val commandSender: RussoundCommandSender,
                          val zoneInfoListener: RussoundZoneInfoListener
)

/** Sends data from the app to the Russound matrix. */
interface RussoundCommandSender {
  fun send(byteArray: ByteArray)
}

/**
 * Implement this to receive callbacks about zone info updates. These occur periodically and
 * after each command is sent to the matrix.
 */
interface RussoundZoneInfoListener {
  fun updated(zoneInfo: ZoneInfo)
}

class Russound {
  fun build(config: RussoundConfig): RussoundAudioManager {
    TODO("implement me")
  }
}
