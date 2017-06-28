package com.thejholmes.russound

import org.slf4j.LoggerFactory
import java.util.Timer
import java.util.concurrent.TimeUnit.SECONDS
import kotlin.concurrent.timer

private val LOG = LoggerFactory.getLogger(StatusRequestTimer::class.java)

/** Once started, it requests status updates periodically. */
class StatusRequestTimer(val statusRequestor: RussoundCommander, val zoneCount: Int) {
  private var timer: Timer? = null
  private var firstRun = true

  fun start() {
    val initialDelay = SECONDS.toMillis(2)
    val period = SECONDS.toMillis(10)

    LOG.info("Starting timer to request audio status in ${initialDelay / 1000}s")
    timer = timer("audio-status-request-timer", false, initialDelay, period) {
      LOG.info("It's time to request audio status")

      if (firstRun) {
        // The matrix doesn't seem to respond to the very first request.
        // It's like it needs to be primed or something.
        firstRun = false
        statusRequestor.requestStatus(Zone(0, 0))
      }

      0.rangeTo(zoneCount - 1)
          .map { Zone(0, it) }
          .forEach { statusRequestor.requestStatus(it) }
    }
  }

  fun stop() {
    timer?.cancel()
  }
}
