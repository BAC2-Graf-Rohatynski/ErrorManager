package errormanager.handler.action

import errormanager.handler.interfaces.ICommandHandlerAction
import apibuilder.error.GetAllMessagesItem
import enumstorage.message.MessageCommand
import errormanager.storage.messages.ErrorMessageStorage
import errormanager.storage.messages.InfoMessageStorage
import errormanager.storage.messages.WarningMessageStorage
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object GetAllMessages: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllMessages::class.java)
    private lateinit var item: GetAllMessagesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${MessageCommand.GetAllMessages.name}' will be executed ...")
        return JSONArray()
                .put(ErrorMessageStorage.getAllMessages())
                .put(WarningMessageStorage.getAllMessages())
                .put(InfoMessageStorage.getAllMessages())
    }

    @Synchronized
    override fun build(message: JSONArray): ICommandHandlerAction {
        item = GetAllMessagesItem().toObject(message = message)
        return this
    }
}