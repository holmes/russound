package org.holmes.russound

import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger(RussoundCommandHandlers::class.java)

class RussoundCommandHandlers(
    private val name: String,
    private val listener: RussoundZoneInfoListener,
    private val actionHandlers: Set<RussoundActionHandler>) {

  /** Commands should start with 0xF0 and end with 0xF7. */
  fun parseCommand(command: ByteArray) {
    actionHandlers
        .filter { it.canHandle(command) }
        .forEach {
          val action = it.createAction(command)
          LOG.info("$name received: ${action.description}")
          listener.onNext(action)
        }
  }
}

