package errormanager.display

import apibuilder.error.response.ResponseItem
import errormanager.display.interfaces.IDisplaySocketHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DisplaySocketHandler: IDisplaySocketHandler {
    private lateinit var displaySocket: DisplaySocket
    private val logger: Logger = LoggerFactory.getLogger(DisplaySocketHandler::class.java)

    init {
        connect()
    }

    @Synchronized
    override fun send(response: ResponseItem) {
        if (::displaySocket.isInitialized) {
            displaySocket.send(message = response.toJson())
        } else {
            logger.error("Socket isn't initialized! Message '${response.toJson()}' cannot be send!")
        }
    }

    private fun connect() {
        try {
            logger.info("Connecting ...")
            displaySocket = DisplaySocket()
            logger.info("Connected")
        } catch (ex: Exception) {
            logger.error("Error occurred while connecting!\n${ex.message}")
        }
    }
}