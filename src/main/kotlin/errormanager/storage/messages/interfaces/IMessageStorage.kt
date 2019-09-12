package errormanager.storage.messages.interfaces

import errormanager.message.MessageObject
import org.json.JSONArray

interface IMessageStorage {
    fun addMessage(messageObject: MessageObject): Boolean
    fun removeMessage(code: Int)
    fun deleteAllMessages()
    fun getAllMessages(): JSONArray
}