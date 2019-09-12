package errormanager.language

import enumstorage.language.Language
import errormanager.language.interfaces.ILanguageHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object LanguageHandler: ILanguageHandler {
    private var enabledLanguage: String = Language.En.name
    private val logger: Logger = LoggerFactory.getLogger(LanguageHandler::class.java)

    @Synchronized
    override fun setLanguage(language: String) {
        if (enabledLanguage == language) {
            logger.warn("Language '$language' is already selected!")
        } else {
            enabledLanguage = language
            logger.info("Language '$language' selected")
        }
    }

    @Synchronized
    override fun getLanguage(): String = enabledLanguage
}