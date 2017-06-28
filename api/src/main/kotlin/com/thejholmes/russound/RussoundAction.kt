package com.thejholmes.russound

import java.util.Arrays.equals

interface RussoundActionHandler {
  val command: ByteArray
  val infoOffsets: Set<Int>

  fun canHandle(input: ByteArray): Boolean {
    val pureInput = input
        .mapIndexed { index, byte ->
          if (infoOffsets.contains(index)) 0x00 else byte
        }
        .toByteArray()

    // Clear out the checksum as well.
    pureInput[input.size - 2] = 0

    return equals(pureInput, command)
  }

  fun createAction(input: ByteArray): RussoundAction
}

interface RussoundAction {
  val input: ByteArray

  val zoneOffset: Int

  val zone: Int
    get() = input[zoneOffset].toInt()

  val description: String
    get() = "${this::class.java.typeName} for Zone $zone"

  fun applyTo(currentZoneInfo: ZoneInfo): ZoneInfo

  fun generateResponse(updatedZoneInfo: ZoneInfo): ByteArray
}
