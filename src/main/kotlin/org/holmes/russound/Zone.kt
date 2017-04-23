package org.holmes.russound

/**
 * @param zoneId unique id of this zone
 * @param zoneNumber represents the port on the back, 1-based
 */
data class Zone(val controllerId: Int, val zoneId: Int, val zoneNumber: Int, val name: String)

class Zones {
  val all: Map<Int, org.holmes.russound.Zone>
    get() = listOf(
        org.holmes.russound.Zone(0, 10, 0, "Family Room"),
        org.holmes.russound.Zone(0, 11, 1, "Kitchen"),
        org.holmes.russound.Zone(0, 12, 2, "Outside"),
        org.holmes.russound.Zone(0, 13, 3, "Master"),
        org.holmes.russound.Zone(0, 14, 4, "Nursery"),
        org.holmes.russound.Zone(0, 15, 5, "Unknown")
    ).associateBy(org.holmes.russound.Zone::zoneId)

  fun zone(zoneId: Int): org.holmes.russound.Zone {
    return all.getOrElse(zoneId) {
      throw IllegalArgumentException("Unknown Zone: $zoneId")
    }
  }

  fun zoneAt(zoneNumber: Int): org.holmes.russound.Zone {
    val zone = all.values.firstOrNull { it.zoneNumber == zoneNumber }

    return when (zone) {
      null -> throw IllegalArgumentException("Unknown Zone Number: $zoneNumber")
      else -> zone
    }
  }
}
