package errormanager.handler.action

import apibuilder.error.SetLanguageItem
import errormanager.handler.interfaces.ICommandHandlerAction
import enumstorage.message.MessageCommand
import errormanager.language.LanguageHandler
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object SetLanguage: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(SetLanguage::class.java)
    private lateinit var item: SetLanguageItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${MessageCommand.SendMessage.name}' will be executed ...")
        return LanguageHandler.setLanguage(language = item.language)
    }

    @Synchronized
    override fun build(message: JSONArray): ICommandHandlerAction {
        item = SetLanguageItem().toObject(message = message)
        return this
    }
}