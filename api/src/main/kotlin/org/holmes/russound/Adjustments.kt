package org.holmes.russound

enum class PowerChange {
  ON, OFF;
}

sealed class VolumeChange {
  class Up : VolumeChange()
  class Down : VolumeChange()
  class Set(val level: Int) : VolumeChange()
}

sealed class BassLevel {
  class Up : BassLevel()
  class Down : BassLevel()
  class Flat : BassLevel()
  class Set(val level: Int) : BassLevel()
}

sealed class TrebleLevel {
  class Up : TrebleLevel()
  class Down : TrebleLevel()
  class Flat : TrebleLevel()
  class Set(val level: Int) : TrebleLevel()
}

sealed class Balance {
  class Left : Balance()
  class Right : Balance()
  class Center : Balance()
  class Set(val level: Int) : Balance()
}

enum class Loudness {
  ON, OFF;
}
