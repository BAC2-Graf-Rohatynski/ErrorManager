package errormanager.display

import apibuilder.error.header.HeaderBuilder
import apibuilder.json.Json
import errormanager.ErrorManagerRunner
import errormanager.display.interfaces.IDisplaySocket
import errormanager.handler.MessageHandler
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import propertystorage.PortProperties
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.lang.Exception
import kotlin.concurrent.thread

class DisplaySocket: IDisplaySocket {
    private lateinit var clientSocket: Socket
    private lateinit var printWriter: PrintWriter
    private lateinit var bufferedReader: BufferedReader
    private val logger: Logger = LoggerFactory.getLogger(DisplaySocket::class.java)
    private val socketId = (10000..19999).shuffled().first()

    init {
        try {
            openSockets()
            receive()
        } catch (ex: Exception) {
            logger.error("Error socket failure while running socket!\n${ex.message}")
            closeSockets()
        }
    }

    @Synchronized
    override fun getSocketId(): Int = socketId

    private fun openSockets() {
        logger.info("Opening sockets ...")
        clientSocket = Socket("127.0.0.1", PortProperties.getDisplayPort())
        printWriter = PrintWriter(clientSocket.getOutputStream(), true)
        bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        logger.info("Sockets opened")
    }

    @Synchronized
    override fun send(message: JSONArray) {
        try {
            if (::printWriter.isInitialized) {
                printWriter.println(message)
                logger.info("Message '$message' sent")
            } else {
                throw Exception("Print writer isn't initialized yet!")
            }
        } catch (ex: Exception) {
            logger.error("Error occurred while sending message!\n${ex.message}")
        }
    }

    private fun receive() {
        logger.info("Hearing for messages ...")

        thread {
            bufferedReader.use {
                while (ErrorManagerRunner.isRunnable()) {
                    try {
                        val inputLine = bufferedReader.readLine()

                        if (inputLine != null) {
                            val message = JSONArray(inputLine)
                            logger.info("Message '$message' received")
                            val header = HeaderBuilder().build(message = message, socketId = socketId)
                            MessageHandler.parseMessage(message = message, header = header)
                        }
                    } catch (ex: Exception) {
                        logger.error("Error occurred while parsing message!\n${ex.message}")
                    }
                }
            }
        }
    }

    private fun closeSockets() {
        try {
            logger.info("Closing sockets ...")

            if (::printWriter.isInitialized) {
                printWriter.close()
            }

            if (::clientSocket.isInitialized) {
                clientSocket.close()
            }

            logger.info("Sockets closed")
        } catch (ex: Exception) {
            logger.error("Error occurred while closing sockets!\n${ex.message}")
        }
    }
}