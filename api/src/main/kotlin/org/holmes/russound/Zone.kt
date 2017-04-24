package org.holmes.russound

/** @param zoneNumber represents the port on the back, 1-based. */
data class Zone(val controllerId: Int, val zoneNumber: Int)

/** @param sourceNumber represents the port on the back, 1-based. */
data class Source(val controllerId: Int, val sourceNumber: Int)

data class ZoneInfo(val zone: Int, val source: Int, val power: Boolean = false, val volume: Int = 0, val bass: Int = 0, val treble: Int = 0, val balance: Int = 0, val loudness: Boolean = false) {
  fun copy(source: Int = this.source, power: Boolean = this.power, volume: Int = this.volume, bass: Int = this.bass, treble: Int = this.treble, balance: Int = this.balance, loudness: Boolean = this.loudness)
      = ZoneInfo(zone, source, power, volume, bass, treble, balance, loudness)
}
