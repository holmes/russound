package com.thejholmes.russound

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

class RussoundCommanderUnitTest {
  private lateinit var senderQueue: RussoundSenderQueue
  private lateinit var commands: RussoundCommands
  private lateinit var commander: RussoundCommander

  val zone1 = Zone(0, 1)
  val zone2 = Zone(0, 2)
  val zone3 = Zone(0, 3)
  val source0 = Source(0, 1)

  @Before fun setUp() {
    commands = mock<RussoundCommands> {
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

      on { bassUp(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}BassUp".toByteArray()
          }

      on { bassDown(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}BassDown".toByteArray()
          }

      on { bassSet(any(), eq(0)) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}BassFlat".toByteArray()
          }

      on { trebleUp(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}TrebleUp".toByteArray()
          }

      on { trebleDown(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}TrebleDown".toByteArray()
          }

      on { trebleSet(any(), eq(0)) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}TrebleFlat".toByteArray()
          }

      on { balanceLeft(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}BalanceLeft".toByteArray()
          }

      on { balanceRight(any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}BalanceRight".toByteArray()
          }

      on { balanceSet(any(), eq(0)) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            "Zone${zone.zoneNumber}BalanceCenter".toByteArray()
          }

      on { turnOnVolume(any(), anyInt()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            val level = it.arguments[1] as Int
            "Zone${zone.zoneNumber}InitialVolume$level".toByteArray()
          }

      on { loudness(any(), any()) }
          .doAnswer {
            val zone = it.arguments[0] as Zone
            val loudness = it.arguments[1] as Loudness
            "Zone${zone.zoneNumber}Loudness${loudness.name}".toByteArray()
          }
    }

    senderQueue = mock { }
    commander = RussoundCommander(senderQueue, commands)
  }

  @Test fun powerOnTurnsOn() {
    val expected = "Zone1On".toByteArray()
    commander.power(zone1, PowerChange.ON)
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun powerOffTurnsOff() {
    val expected = "Zone2Off".toByteArray()
    commander.power(zone2, PowerChange.OFF)
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun volumeUp() {
    val expected = "Zone2VolumeUp".toByteArray()
    commander.volume(zone2, VolumeChange.Up())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun volumeDown() {
    val expected = "Zone2VolumeDown".toByteArray()
    commander.volume(zone2, VolumeChange.Down())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun volumeSet() {
    val expected = "Zone2Volume22".toByteArray()
    commander.volume(zone2, VolumeChange.Set(22))
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun changeSourceChangesSource() {
    val zone = zone1
    val source = source0
    val expected = "Zone${zone.zoneNumber}Source${source.sourceNumber}".toByteArray()

    commander.source(zone, source)
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun bassUp() {
    val expected = "Zone2BassUp".toByteArray()
    commander.bass(zone2, BassLevel.Up())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun bassDown() {
    val expected = "Zone2BassDown".toByteArray()
    commander.bass(zone2, BassLevel.Down())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun bassFlat() {
    val expected = "Zone2BassFlat".toByteArray()
    commander.bass(zone2, BassLevel.Flat())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun trebleUp() {
    val expected = "Zone2TrebleUp".toByteArray()
    commander.treble(zone2, TrebleLevel.Up())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun trebleDown() {
    val expected = "Zone2TrebleDown".toByteArray()
    commander.treble(zone2, TrebleLevel.Down())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun trebleFlat() {
    val expected = "Zone2TrebleFlat".toByteArray()
    commander.treble(zone2, TrebleLevel.Flat())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun balanceLeft() {
    val expected = "Zone3BalanceLeft".toByteArray()
    commander.balance(zone3, Balance.Left())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun balanceRight() {
    val expected = "Zone3BalanceRight".toByteArray()
    commander.balance(zone3, Balance.Right())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun balanceCenter() {
    val expected = "Zone3BalanceCenter".toByteArray()
    commander.balance(zone3, Balance.Center())
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun initialVolume() {
    val expected = "Zone3InitialVolume44".toByteArray()
    commander.initialVolume(zone3, VolumeChange.Set(44))
    verify(senderQueue).sendCommand(expected)
  }

  @Test fun loudness() {
    val expected = "Zone3LoudnessON".toByteArray()
    commander.loudness(zone3, Loudness.ON)
    verify(senderQueue).sendCommand(expected)
  }
}
