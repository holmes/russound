package org.holmes.russound

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test

class RussoundAudioCommanderUnitTest {
  private lateinit var audioQueue: RussoundAudioQueue
  private lateinit var russoundCommands: RussoundCommands
  private lateinit var russoundAudioCommander: RussoundAudioCommander

  val zone1 = Zone(0, 1)
  val zone2 = Zone(0, 2)
  val source0 = Source(0, 1)

  @Before fun setUp() {
    russoundCommands = mock<RussoundCommands> {
      on { turnOn(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}On".toByteArray()
          }

      on { turnOff(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}Off".toByteArray()
          }

      on { listen(any(), any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            val source = it.arguments[1] as Source
            "Zone${zone.zoneNumber}Source${source.sourceNumber}".toByteArray()
          }

      on { volume(any(), any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            val level = it.arguments[1] as Int
            "Zone${zone.zoneNumber}Volume$level".toByteArray()
          }

      on { volumeUp(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}VolumeUp".toByteArray()
          }

      on { volumeDown(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}VolumeDown".toByteArray()
          }
    }

    audioQueue = mock {  }
    russoundAudioCommander = RussoundAudioCommander(audioQueue, russoundCommands)
  }

  @Test fun powerOnTurnsOn() {
    val expected = "Zone1On".toByteArray()
    russoundAudioCommander.power(zone1, PowerChange.ON)
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun powerOffTurnsOff() {
    val expected = "Zone2Off".toByteArray()
    russoundAudioCommander.power(zone2, PowerChange.OFF)
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun volumeUp() {
    val expected = "Zone2VolumeUp".toByteArray()
    russoundAudioCommander.volume(zone2, VolumeChange.Up())
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun volumeDown() {
    val expected = "Zone2VolumeDown".toByteArray()
    russoundAudioCommander.volume(zone2, VolumeChange.Down())
    verify(audioQueue).sendCommand(expected)
  }
  @Test fun volumeSet() {
    val expected = "Zone2Volume22".toByteArray()
    russoundAudioCommander.volume(zone2, VolumeChange.Set(22))
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun changeSourceChangesSource() {
    val zone = zone1
    val source = source0
    val expected = "Zone${zone.zoneNumber}Source${source.sourceNumber}".toByteArray()

    russoundAudioCommander.changeSource(zone, source)
    verify(audioQueue).sendCommand(expected)
  }
}
