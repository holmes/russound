package org.holmes.russound


/**
 * Implement this to receive callbacks about zone info updates. These occur periodically and
 * after each command is sent to the matrix.
 */
interface RussoundZoneInfoListener {
  /** Called for all status updates. */
  fun onNext(action: RussoundAction)

  /** ZoneInfo is really what we care about, so we break this out as a special update. */
  fun updated(zoneInfo: ZoneInfo)
}

/** Translates ByteArray(s) into commands. */
class RussoundTranslator(val commandHandlers: RussoundCommandHandlers) {

  /**
   * When you receive a response from the matrix, pass it to this function. We'll decode and update
   * your listener with the latest information.
   */
  fun processCommand(command: ByteArray) {
    commandHandlers.parseCommand(command)
  }
}
