package errormanager.handler

import apibuilder.error.header.Header
import apibuilder.error.response.ResponseItem
import enumstorage.message.MessageCommand
import errormanager.display.DisplaySocketHandler
import errormanager.handler.interfaces.IMessageHandler
import errormanager.handler.action.*
import errormanager.storage.clients.Clients
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object MessageHandler: IMessageHandler {
    private val logger: Logger = LoggerFactory.getLogger(MessageHandler::class.java)

    @Synchronized
    override fun parseMessage(header: Header, message: JSONArray) {
        val response: ResponseItem = try {
            val value = when (header.command) {
                MessageCommand.SetLanguage.name -> SetLanguage.build(message = message).run()
                MessageCommand.SendMessage.name -> SendMessage.build(message = message).run()
                MessageCommand.DeleteMessage.name -> DeleteMessage.build(message = message).run()
                MessageCommand.DeleteAllMessages.name -> DeleteAllMessages.build(message = message).run()
                MessageCommand.GetAllMessages.name -> GetAllMessages.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' received!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error occurred while parsing message!\n${ex.message}")
            ResponseItem().create(message = message, socketId = header.socketId)
        }

        sendResponse(response = response)
    }

    private fun sendResponse(response: ResponseItem) {
        try {
            if (response.isResponse) {
                return logger.info("Response won't be forwarded: $response")
            }

            if (response.isGetMessage) {
                DisplaySocketHandler.send(response = response)
                Clients.sendToSingleClient(response = response)
            } else {
                DisplaySocketHandler.send(response = response)
                Clients.sendToAllClients(response = response)
            }
        } catch (ex: Exception) {
            logger.error("Error while sending response!\n${ex.message}")
        }
    }
}