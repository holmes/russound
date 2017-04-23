package org.holmes.russound

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test

class AudioCommanderUnitTest {
  lateinit var audioQueue: AudioQueue
  lateinit var russoundCommands: RussoundCommands
  lateinit var audioCommander: AudioCommander

  val zone0 = Zone(0, 10, 1, "Place")
  val zone1 = Zone(0, 11, 2, "Place")
  val source0 = Source(0, 10, 1, "TV")

  @Before fun setUp() {
    russoundCommands = mock<RussoundCommands> {
      on { turnOn(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneId}On".toByteArray()
          }

      on { turnOff(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneId}Off".toByteArray()
          }

      on { listen(any(), any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            val source = it.arguments[1] as Source
            "Zone${zone.zoneId}Source${source.sourceId}".toByteArray()
          }

      on { volume(any(), any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            val level = it.arguments[1] as Int
            "Zone${zone.zoneId}Volume$level".toByteArray()
          }

      on { volumeUp(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneId}VolumeUp".toByteArray()
          }

      on { volumeDown(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneId}VolumeDown".toByteArray()
          }
    }

    audioQueue = mock {  }
    audioCommander = AudioCommander(audioQueue, russoundCommands)
  }

  @Test fun powerOnTurnsOn() {
    val expected = "Zone10On".toByteArray()
    audioCommander.power(zone0, PowerChange.ON)
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun powerOffTurnsOff() {
    val expected = "Zone11Off".toByteArray()
    audioCommander.power(zone1, PowerChange.OFF)
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun volumeUp() {
    val expected = "Zone11VolumeUp".toByteArray()
    audioCommander.volume(zone1, VolumeChange.Up())
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun volumeDown() {
    val expected = "Zone11VolumeDown".toByteArray()
    audioCommander.volume(zone1, VolumeChange.Down())
    verify(audioQueue).sendCommand(expected)
  }
  @Test fun volumeSet() {
    val expected = "Zone11Volume22".toByteArray()
    audioCommander.volume(zone1, VolumeChange.Set(22))
    verify(audioQueue).sendCommand(expected)
  }

  @Test fun changeSourceChangesSource() {
    val zone = zone0
    val source = source0
    val expected = "Zone${zone.zoneId}Source${source.sourceId}".toByteArray()

    audioCommander.changeSource(zone, source)
    verify(audioQueue).sendCommand(expected)
  }
}
