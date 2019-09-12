package errormanager.handler.action

import apibuilder.error.SendMessageItem
import errormanager.handler.interfaces.ICommandHandlerAction
import enumstorage.message.MessageCommand
import enumstorage.message.MessageType
import errormanager.language.LanguageHandler
import errormanager.message.MessageObject
import errormanager.storage.messages.ErrorMessageStorage
import errormanager.storage.messages.InfoMessageStorage
import errormanager.storage.messages.WarningMessageStorage
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import propertystorage.MessageProperties
import java.lang.Exception

object SendMessage: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(SendMessage::class.java)
    private lateinit var item: SendMessageItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${MessageCommand.SendMessage.name}' will be executed ...")
        val messageObject = getMessageObject()

        when (getMessageObject().type) {
            MessageType.Info -> InfoMessageStorage.addMessage(messageObject = messageObject)
            MessageType.Warning -> WarningMessageStorage.addMessage(messageObject = messageObject)
            MessageType.Error -> ErrorMessageStorage.addMessage(messageObject = messageObject)
            else -> throw Exception("Invalid type '${getType()}' of code '${item.code}' received!")
        }

        return getMessageObject()
    }

    @Synchronized
    override fun build(message: JSONArray): ICommandHandlerAction {
        item = SendMessageItem().toObject(message = message)
        return this
    }

    private fun getType(): MessageType {
        val messageLevel = MessageProperties.getLevel(code = item.code)

        return when (messageLevel) {
            MessageType.Info.name -> MessageType.Info
            MessageType.Warning.name -> MessageType.Warning
            MessageType.Error.name -> MessageType.Error
            else -> throw Exception("Invalid type '$messageLevel' of code '${item.code}' received!")
        }
    }

    private fun getMessageObject(): MessageObject {
        val messageText = MessageProperties.getMessage(code = item.code, language = LanguageHandler.getLanguage())

        return MessageObject(
                code = item.code,
                enabled = item.enabled,
                timeStamp = item.timestamp,
                type = getType(),
                message = messageText,
                ssid = item.ssid)
    }
}