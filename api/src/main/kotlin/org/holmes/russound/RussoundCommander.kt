package org.holmes.russound


/** Sends data from the app to the Russound matrix. */
interface RussoundCommandSender {
  fun send(byteArray: ByteArray)
}

/**
 * Your central spot to control your Russound Matrix.
 *
 * Note that all calls are asynchronous, you'll get responses if you set up a
 * {@link RussoundTranslator}.
 *
 * You can call {@link #destroy} to stop queuing up commands to the RussoundCommandSender.
 *
 * Note: you need to handle closing streams yourself - this library won't do it for you.
 */
class RussoundCommander internal constructor(
    private val senderQueue: RussoundSenderQueue,
    private val russoundCommands: RussoundCommands) {
  fun destroy() {
    senderQueue.stop()
  }

  fun requestStatus(zone: Zone) {
    senderQueue.sendCommand(russoundCommands.requestStatus(zone))
  }

  fun power(zone: Zone, power: PowerChange) {
    senderQueue.sendCommand(when (power) {
      PowerChange.ON -> russoundCommands.turnOn(zone)
      PowerChange.OFF -> russoundCommands.turnOff(zone)
    })
  }

  fun source(zone: Zone, source: Source) {
    senderQueue.sendCommand(russoundCommands.listen(zone, source))
  }

  /** Sets the initial volume when the zone is turned on. */
  fun initialVolume(zone: Zone, volume: Int) {
    senderQueue.sendCommand(russoundCommands.turnOnVolume(zone, volume))
  }

  fun volume(zone: Zone, volume: VolumeChange) {
    senderQueue.sendCommand(when (volume) {
      is VolumeChange.Up -> russoundCommands.volumeUp(zone)
      is VolumeChange.Down -> russoundCommands.volumeDown(zone)
      is VolumeChange.Set -> russoundCommands.volume(zone, volume.level)
    })
  }

  fun bass(zone: Zone, bass: BassLevel) {
    senderQueue.sendCommand(when (bass) {
      BassLevel.UP -> russoundCommands.bassUp(zone)
      BassLevel.DOWN -> russoundCommands.bassDown(zone)
      BassLevel.FLAT -> russoundCommands.bassFlat(zone)
    })
  }

  fun treble(zone: Zone, treble: TrebleLevel) {
    senderQueue.sendCommand(when (treble) {
      TrebleLevel.UP -> russoundCommands.trebleUp(zone)
      TrebleLevel.DOWN -> russoundCommands.trebleDown(zone)
      TrebleLevel.FLAT -> russoundCommands.trebleFlat(zone)
    })
  }

  fun balance(zone: Zone, balance: Balance) {
    senderQueue.sendCommand(when (balance) {
      Balance.LEFT -> russoundCommands.balanceLeft(zone)
      Balance.RIGHT -> russoundCommands.balanceRight(zone)
      Balance.CENTER -> russoundCommands.balanceCentered(zone)
    })
  }

  fun loudness(zone: Zone, loudness: Loudness) {
    senderQueue.sendCommand(russoundCommands.loudness(zone, loudness))
  }
}
