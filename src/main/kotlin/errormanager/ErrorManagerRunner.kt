package errormanager

import enumstorage.update.ApplicationName
import errormanager.display.DisplaySocketHandler
import errormanager.socket.ErrorSocketHandler
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ErrorManagerRunner {
    private val logger: Logger = LoggerFactory.getLogger(ErrorManagerRunner::class.java)

    @Volatile
    private var runApplication = true

    fun start() {
        logger.info("Starting application")
        DisplaySocketHandler
        ErrorSocketHandler
    }

    @Synchronized
    fun isRunnable(): Boolean = runApplication

    fun stop() {
        logger.info("Stopping application")
        runApplication = false

        ErrorSocketHandler.closeSockets()
    }

    fun getUpdateInformation(): JSONObject = UpdateInformation.getAsJson(applicationName = ApplicationName.ErrorManager.name)
}