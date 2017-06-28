package com.thejholmes.russound


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
  fun initialVolume(zone: Zone, volume: VolumeChange.Set) {
    senderQueue.sendCommand(russoundCommands.turnOnVolume(zone, volume.level))
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
      is BassLevel.Up -> russoundCommands.bassUp(zone)
      is BassLevel.Down -> russoundCommands.bassDown(zone)
      is BassLevel.Flat -> russoundCommands.bassSet(zone, 0)
      is BassLevel.Set -> russoundCommands.bassSet(zone, bass.level)
    })
  }

  fun treble(zone: Zone, treble: TrebleLevel) {
    senderQueue.sendCommand(when (treble) {
      is TrebleLevel.Up -> russoundCommands.trebleUp(zone)
      is TrebleLevel.Down -> russoundCommands.trebleDown(zone)
      is TrebleLevel.Flat -> russoundCommands.trebleSet(zone, 0)
      is TrebleLevel.Set -> russoundCommands.trebleSet(zone, treble.level)
    })
  }

  fun balance(zone: Zone, balance: Balance) {
    senderQueue.sendCommand(when (balance) {
      is Balance.Left -> russoundCommands.balanceLeft(zone)
      is Balance.Right -> russoundCommands.balanceRight(zone)
      is Balance.Center -> russoundCommands.balanceSet(zone, 0)
      is Balance.Set -> russoundCommands.balanceSet(zone, balance.level)
    })
  }

  fun loudness(zone: Zone, loudness: Loudness) {
    senderQueue.sendCommand(russoundCommands.loudness(zone, loudness))
  }
}
