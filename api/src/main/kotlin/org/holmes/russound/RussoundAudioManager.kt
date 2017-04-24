package org.holmes.russound

import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger(RussoundAudioManager::class.java)

class RussoundAudioManager internal constructor(
    private val russoundAudioCommander: RussoundAudioCommander,
    private val russoundCommandReceiver: RussoundCommandReceiver)
{
  private val zoneInfoUpdates = PublishSubject.create<ZoneInfo>()

  fun receivedCommand(command: ByteArray) {
    russoundCommandReceiver.parseCommand(command)
  }

  fun requestStatus(zone: Zone) {
    russoundAudioCommander.requestStatus(zone)
  }

  fun power(zone: Zone, power: PowerChange){
    russoundAudioCommander.power(zone, power)
    russoundAudioCommander.requestStatus(zone)
  }

  fun source(zone: Zone, source: Source) {
    russoundAudioCommander.changeSource(zone, source)
    russoundAudioCommander.requestStatus(zone)
  }

  /** Sets the initial volume when the zone is turned on. */
  fun initialVolume(zone: Zone, volume: Int) {
    russoundAudioCommander.initialVolume(zone, volume)
    russoundAudioCommander.requestStatus(zone)
  }

  fun volume(zone: Zone, volume: VolumeChange) {
    russoundAudioCommander.volume(zone, volume)
    russoundAudioCommander.requestStatus(zone)
  }

  fun bass(zone: Zone, bass: BassLevel) {
    russoundAudioCommander.bass(zone, bass)
    russoundAudioCommander.requestStatus(zone)
  }

  fun treble(zone: Zone, treble: TrebleLevel) {
    russoundAudioCommander.treble(zone, treble)
    russoundAudioCommander.requestStatus(zone)
  }

  fun balance(zone: Zone, balance: Balance) {
    russoundAudioCommander.balance(zone, balance)
    russoundAudioCommander.requestStatus(zone)
  }

  fun loudness(zone: Zone, loudness: Loudness) {
    russoundAudioCommander.loudness(zone, loudness)
    russoundAudioCommander.requestStatus(zone)
  }
}
