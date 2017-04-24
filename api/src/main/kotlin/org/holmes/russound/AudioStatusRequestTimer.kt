package org.holmes.russound

import org.slf4j.LoggerFactory
import java.util.Timer
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.concurrent.timer

private val LOG = LoggerFactory.getLogger(AudioStatusRequestTimer::class.java)

/** Once started, it requests status updates periodically. */
class AudioStatusRequestTimer(val russoundConfig: RussoundConfig, val russoundAudioManager: RussoundAudioManager) {
  var timer: Timer? = null
  var firstRun = true

  fun start() {
    val initialDelay = SECONDS.toMillis(5)
    val period = MINUTES.toMillis(5)

    LOG.info("Starting timer to request audio status in ${initialDelay / 1000}s")
    timer = timer("audio-status-request-timer", true, initialDelay, period) {
      LOG.info("It's time to request audio status")

      if (firstRun) {
        // The matrix doesn't seem to respond to the very first request.
        // It's like it needs to be primed or something.
        firstRun = false
        russoundAudioManager.requestStatus(Zone(0, 0))
      }

      0.rangeTo(russoundConfig.zoneCount)
          .map { Zone(0, it) }
          .forEach { russoundAudioManager.requestStatus(it) }
    }
  }

  fun stop() {
    timer?.cancel()
  }
}
