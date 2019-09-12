package errormanager.storage.messages

import errormanager.message.MessageObject
import errormanager.storage.messages.interfaces.IMessageStorage
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class MessageStorage: IMessageStorage {
    private val messages: MutableList<MessageObject> = mutableListOf()
    private val logger: Logger = LoggerFactory.getLogger(MessageStorage::class.java)

    @Synchronized
    override fun getAllMessages(): JSONArray {
        val jsonArray = JSONArray()

        messages.forEach { message ->
            jsonArray.put(message.toJson())
        }

        return jsonArray
    }

    private fun checkForMessageObject(code: Int): Boolean {
        messages.forEach { message ->
            if (message.code == code) {
                return true
            }
        }

        return false
    }

    private fun removeFromMessageObjectStorage(code: Int) {
        messages.forEach { message ->
            if (message.code == code) {
                messages.remove(message)
                logger.info("Message with code '$code' removed from storage")
            }
        }
    }

    @Synchronized
    override fun addMessage(messageObject: MessageObject): Boolean {
        return if (!checkForMessageObject(code = messageObject.code)) {
            messages.add(messageObject)
            logger.info("Message with code '${messageObject.code}' added to storage")
            true
        } else {
            false
        }
    }

    @Synchronized
    override fun deleteAllMessages() {
        messages.clear()
        logger.info("All messages cleared")
    }

    @Synchronized
    override fun removeMessage(code: Int) {
        if (checkForMessageObject(code = code)) {
            removeFromMessageObjectStorage(code = code)
        }
    }
}