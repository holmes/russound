package com.thejholmes.russound

import org.slf4j.LoggerFactory

class RussoundCommandHandlers(
    private val name: String,
    private val listener: RussoundZoneInfoListener,
    private val actionHandlers: Set<RussoundActionHandler>) {

  private val logger = LoggerFactory.getLogger(RussoundCommandHandlers::class.java)

  /** Commands should start with 0xF0 and end with 0xF7. */
  fun parseCommand(command: ByteArray) {
    actionHandlers
        .filter { it.canHandle(command) }
        .forEach {
          val action = it.createAction(command)
          logger.debug("$name received: ${action.description}")
          listener.onNext(action)

          if (action is ReceivedStatusAction) {
            listener.updated(action.zoneInfo)
          }
        }
  }
}

