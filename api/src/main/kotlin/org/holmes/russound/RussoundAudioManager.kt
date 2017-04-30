package org.holmes.russound

/**
 * Your central spot to control your Russound Matrix, and process updates that you receive from it.
 *
 * Note that all calls are asynchronous, you'll get responses via the
 * {@link RussoundZoneInfoListener} you used during construction.
 *
 * You need to call #initialize prior to sending commands, and you should call #destroy when you
 * want to tear things down. Note: you need to handle closing streams yourself - this library
 * won't do it for you.
 *
 * @see {RussoundAudioManager#processCommand}
 */
class RussoundAudioManager internal constructor(
    private val audioQueue: RussoundAudioQueue,
    private val russoundCommands: RussoundCommands,
    private val commandHandlers: RussoundCommandHandlers)
{
  fun initialize() {
  }

  fun destroy() {
    audioQueue.stop()
  }

  /**
   * When you receive a response from the matrix, pass it to this function. We'll decode and update
   * your listener with the latest information.
   */
  fun processCommand(command: ByteArray) {
    commandHandlers.parseCommand(command)
  }

  fun requestStatus(zone: Zone) {
    audioQueue.sendCommand(russoundCommands.requestStatus(zone))
  }

  fun power(zone: Zone, power: PowerChange){
    audioQueue.sendCommand(when (power) {
      PowerChange.ON -> russoundCommands.turnOn(zone)
      PowerChange.OFF -> russoundCommands.turnOff(zone)
    })
    requestStatus(zone)
  }

  fun source(zone: Zone, source: Source) {
    audioQueue.sendCommand(russoundCommands.listen(zone, source))
    requestStatus(zone)
  }

  /** Sets the initial volume when the zone is turned on. */
  fun initialVolume(zone: Zone, volume: Int) {
    audioQueue.sendCommand(russoundCommands.turnOnVolume(zone, volume))
    requestStatus(zone)
  }

  fun volume(zone: Zone, volume: VolumeChange) {
    audioQueue.sendCommand(when (volume) {
      is VolumeChange.Up -> russoundCommands.volumeUp(zone)
      is VolumeChange.Down -> russoundCommands.volumeDown(zone)
      is VolumeChange.Set -> russoundCommands.volume(zone, volume.level)
    })
    requestStatus(zone)
  }

  fun bass(zone: Zone, bass: BassLevel) {
    audioQueue.sendCommand(when (bass) {
      BassLevel.UP -> russoundCommands.bassUp(zone)
      BassLevel.DOWN -> russoundCommands.bassDown(zone)
      BassLevel.FLAT -> russoundCommands.bassFlat(zone)
    })
    requestStatus(zone)
  }

  fun treble(zone: Zone, treble: TrebleLevel) {
    audioQueue.sendCommand(when (treble) {
      TrebleLevel.UP -> russoundCommands.trebleUp(zone)
      TrebleLevel.DOWN -> russoundCommands.trebleDown(zone)
      TrebleLevel.FLAT -> russoundCommands.trebleFlat(zone)
    })
    requestStatus(zone)
  }

  fun balance(zone: Zone, balance: Balance) {
    audioQueue.sendCommand(when (balance) {
      Balance.LEFT -> russoundCommands.balanceLeft(zone)
      Balance.RIGHT -> russoundCommands.balanceRight(zone)
      Balance.CENTER -> russoundCommands.balanceCentered(zone)
    })
    requestStatus(zone)
  }

  fun loudness(zone: Zone, loudness: Loudness) {
    audioQueue.sendCommand(russoundCommands.loudness(zone, loudness))
    requestStatus(zone)
  }
}
