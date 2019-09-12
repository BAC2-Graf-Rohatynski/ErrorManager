package errormanager.storage.clients.interfaces

import apibuilder.error.response.ResponseItem
import errormanager.socket.ErrorSocket
import org.json.JSONArray

interface IClients {
    fun sendToAllClients(response: ResponseItem)
    fun sendToSingleClient(response: ResponseItem)
    fun addClient(client: ErrorSocket)
    fun removeClient(client: ErrorSocket)
    fun getAllClients(): List<ErrorSocket>
}