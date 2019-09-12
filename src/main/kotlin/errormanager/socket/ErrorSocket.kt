package errormanager.socket

import apibuilder.error.header.HeaderBuilder
import errormanager.ErrorManagerRunner
import errormanager.handler.MessageHandler
import errormanager.socket.interfaces.IErrorSocket
import errormanager.storage.clients.Clients
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket

class ErrorSocket(private val clientSocket: Socket): IErrorSocket, Thread() {
    private lateinit var bufferedReader: BufferedReader
    private lateinit var printWriter: PrintWriter
    private val logger: Logger = LoggerFactory.getLogger(ErrorSocket::class.java)
    private val socketId = (10000..19999).shuffled().first()

    override fun run() {
        try {
            openSockets()
            receive()
        } catch (ex: Exception) {
            logger.error("Error occurred while running socket!\n${ex.message}")
        } finally {
            closeSockets()
        }
    }

    @Synchronized
    override fun getSocketId(): Int = socketId

    private fun openSockets() {
        try {
            logger.info("Opening sockets ...")
            bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            printWriter = PrintWriter(clientSocket.getOutputStream(), true)
            logger.info("Sockets opened")
        } catch (ex: Exception) {
            logger.error("Error occurred while opening sockets!\n${ex.message}")
        }
    }

    private fun receive() {
        logger.info("Hearing for messages ...")

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

    @Synchronized
    override fun send(message: JSONArray) {
        try {
            if (::printWriter.isInitialized) {
                printWriter.println(message)
                logger.info("Message '$message' sent to system")
            } else {
                throw Exception("Print writer instance not started yet!")
            }
        } catch (ex: Exception) {
            logger.error("Error occurred while sending message!\n${ex.message}")
        }
    }

    private fun closeSockets() {
        try {
            logger.info("Closing sockets ...")

            if (::bufferedReader.isInitialized) {
                bufferedReader.close()
            }

            if (::printWriter.isInitialized) {
                printWriter.close()
            }

            Clients.removeClient(client = this)
            clientSocket.close()
            logger.info("Sockets closed")
        } catch (ex: Exception) {
            logger.error("Error occurred while closing sockets!\n${ex.message}")
        }
    }
}