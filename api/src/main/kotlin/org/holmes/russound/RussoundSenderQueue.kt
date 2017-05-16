package org.holmes.russound

import org.holmes.russound.util.toHexString
import org.slf4j.LoggerFactory
import java.util.LinkedList
import java.util.concurrent.Executors

private val LOG = LoggerFactory.getLogger(RussoundSenderQueue::class.java)

class RussoundSenderQueue(val name: String, val commandSender: RussoundCommandSender) {
  private val queue = LinkedList<ByteArray>()
  private val executor = Executors.newSingleThreadExecutor({
    Thread(it, "russound-command-writer")
  })

  private var actionPending = false
  private var destroyed = false

  fun stop() {
    destroyed = true
  }

  @Synchronized fun sendCommand(command: ByteArray) {
    if (destroyed) return

    if (queue.isEmpty() && !actionPending) {
      performAction(command)
    } else {
      queue.add(command)
    }
  }

  private fun performAction(command: ByteArray) {
    executor.submit {
      LOG.info("$name sending: ${command.toHexString()}")
      commandSender.send(command)
      Thread.sleep(150)
      onActionCompleted()
    }
  }

  @Synchronized private fun onActionCompleted() {
    actionPending = false

    val action = queue.poll()
    if (action != null) {
      performAction(action)
    }
  }
}
