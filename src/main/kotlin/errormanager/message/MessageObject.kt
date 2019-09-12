package errormanager.message

import enumstorage.message.Message
import enumstorage.message.MessageType
import org.json.JSONObject

class MessageObject(val code: Int, val message: String, val type: MessageType, val enabled: Boolean, val timeStamp: Long, val ssid: Int) {
    fun toJson(): JSONObject = JSONObject()
            .put(Message.Enabled.name, enabled)
            .put(Message.Timestamp.name, timeStamp)
            .put(Message.Code.name, code)
            .put(Message.Ssid.name, ssid)
            .put(Message.Type.name, type)
}