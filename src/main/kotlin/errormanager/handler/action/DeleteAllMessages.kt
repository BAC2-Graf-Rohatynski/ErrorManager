package errormanager.handler.action

import apibuilder.error.DeleteAllMessagesItem
import errormanager.handler.interfaces.ICommandHandlerAction
import enumstorage.message.MessageCommand
import errormanager.storage.messages.ErrorMessageStorage
import errormanager.storage.messages.InfoMessageStorage
import errormanager.storage.messages.WarningMessageStorage
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object DeleteAllMessages: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllMessages::class.java)
    private lateinit var item: DeleteAllMessagesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${MessageCommand.DeleteAllMessages.name}' will be executed ...")
        ErrorMessageStorage.deleteAllMessages()
        WarningMessageStorage.deleteAllMessages()
        return InfoMessageStorage.deleteAllMessages()
    }

    @Synchronized
    override fun build(message: JSONArray): ICommandHandlerAction {
        item = DeleteAllMessagesItem().toObject(message = message)
        return this
    }
}