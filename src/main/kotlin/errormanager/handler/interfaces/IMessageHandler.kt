package errormanager.handler.interfaces

import apibuilder.error.header.Header
import org.json.JSONArray

interface IMessageHandler {
    fun parseMessage(header: Header, message: JSONArray)
}