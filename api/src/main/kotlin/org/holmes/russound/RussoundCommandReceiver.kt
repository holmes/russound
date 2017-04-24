package org.holmes.russound

import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

private val LOG = LoggerFactory.getLogger(RussoundCommandReceiver::class.java)

internal class RussoundCommandReceiver(
    private val name: String,
    private val receivedMessageSubject: PublishSubject<RussoundAction>,
    private val actionHandlers: Set<RussoundActionHandler>) {

  /** Commands should start with 0xF0 and end with 0xF7. */
  fun parseCommand(command: ByteArray) {
    actionHandlers
        .filter { it.canHandle(command) }
        .forEach {
          val action = it.createAction(command)
          LOG.info("$name received: ${action.description}")
          receivedMessageSubject.onNext(action)
        }
  }
}

