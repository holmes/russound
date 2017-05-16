package org.holmes.russound

import org.slf4j.LoggerFactory
import java.net.ServerSocket

private val LOG = LoggerFactory.getLogger("Receiver Thread")
const val MATRIX_CONNECTION_PORT = 8888

class MockRussoundReceiverThread : Thread("server thread") {
  override fun run() {
    val serverSocket = ServerSocket(MATRIX_CONNECTION_PORT)
    LOG.info("Waiting for Ponderosa to connect at ${serverSocket.localPort}")

    while (true) {
      val socket = serverSocket.accept()
      LOG.info("Ponderosa connected on port: ${serverSocket.localPort}")

      Russound
          .mockReceiver(socket.getInputStream(), socket.getOutputStream())
          .start()
    }
  }
}

