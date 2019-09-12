package errormanager.socket

import errormanager.ErrorManagerRunner
import errormanager.socket.interfaces.IErrorSocketHandler
import errormanager.storage.clients.Clients
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import propertystorage.PortProperties
import java.net.ServerSocket
import kotlin.concurrent.thread

object ErrorSocketHandler: IErrorSocketHandler {
    private lateinit var serverSocket: ServerSocket
    private lateinit var errorSocket: ErrorSocket
    private val port: Int = PortProperties.getErrorPort()
    private val logger: Logger = LoggerFactory.getLogger(ErrorSocketHandler::class.java)

    init {
        thread {
            try {
                openSockets()
                acceptClients()
            } catch (ex: Exception) {
                logger.error("Error occurred while running socket handler!\n${ex.message}")
            } finally {
                closeSockets()
            }
        }
    }

    private fun acceptClients() {
        while (ErrorManagerRunner.isRunnable()) {
            logger.info("Waiting for clients ...")
            errorSocket = ErrorSocket(clientSocket = serverSocket.accept())
            errorSocket.start()
            Clients.addClient(client = errorSocket)
            logger.info("Client added")
        }
    }

    private fun openSockets() {
        logger.info("Opening socket on port '$port' ...")
        serverSocket = ServerSocket(port)
        logger.info("Socket opened")
    }

    @Synchronized
    override fun closeSockets() {
        try {
            logger.info("Closing sockets ...")

            if (::serverSocket.isInitialized) {
                serverSocket.close()
            }

            logger.info("Sockets closed")
        } catch (ex: Exception) {
            logger.error("Error occurred while closing sockets!\n${ex.message}")
        }
    }
}