package errormanager.handler.action

import apibuilder.error.DeleteMessageItem
import errormanager.handler.interfaces.ICommandHandlerAction
import enumstorage.message.MessageCommand
import errormanager.storage.messages.ErrorMessageStorage
import errormanager.storage.messages.InfoMessageStorage
import errormanager.storage.messages.WarningMessageStorage
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object DeleteMessage: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteMessage::class.java)
    private lateinit var item: DeleteMessageItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${MessageCommand.DeleteMessage.name}' will be executed ...")
        ErrorMessageStorage.removeMessage(code = item.code)
        WarningMessageStorage.removeMessage(code = item.code)
        return InfoMessageStorage.removeMessage(code = item.code)
    }

    @Synchronized
    override fun build(message: JSONArray): ICommandHandlerAction {
        item = DeleteMessageItem().toObject(message = message)
        return this
    }
}