package errormanager.display.interfaces

import org.json.JSONArray

interface IDisplaySocket {
    fun send(message: JSONArray)
    fun getSocketId(): Int
}