package org.holmes.russound.serial

import org.holmes.russound.RussoundAudioManager
import org.slf4j.LoggerFactory
import java.io.InputStream
import kotlin.concurrent.thread

private val LOG = LoggerFactory.getLogger(SerialCommandReceiver::class.java)

class SerialCommandReceiver (
    private val audioManager: RussoundAudioManager,
    private val inputStream: InputStream
) {
  private enum class State {
    LOOKING_FOR_START, LOOKING_FOR_END
  }

  private var shouldRun: Boolean = true
  private var state: State

  init {
    state = State.LOOKING_FOR_START
  }

  fun start() {
    thread(true, true, null, "audio-status-receiver") {
      try {
        runReadLoop()
      } catch(e: Exception) {
        LOG.error("Error receiving bytes", e)
        runReadLoop()
      } finally {
        inputStream.close()
      }
    }
  }

  private fun runReadLoop() {
    var buffer = mutableListOf<Byte>()

    while (shouldRun) {
      val read = inputStream.read()

      when (state) {
        State.LOOKING_FOR_START -> {
          if (0xF0 == read) {
            buffer = mutableListOf()
            buffer.add(read.toByte())
            state = State.LOOKING_FOR_END
          }
        }
        State.LOOKING_FOR_END -> {
          buffer.add(read.toByte())
          if (0xF7 == read) {
            val byteArray = buffer.toByteArray()
            audioManager.processCommand(byteArray)

            // Begin looking for the start of the next message.
            state = State.LOOKING_FOR_START
          }
        }
      }
    }
  }

  fun stop() {
    shouldRun = false
  }
}

