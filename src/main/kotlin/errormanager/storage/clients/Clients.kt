package errormanager.storage.clients

import apibuilder.error.response.ResponseItem
import errormanager.socket.ErrorSocket
import errormanager.storage.clients.interfaces.IClients
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Clients: IClients {
    private val clients: MutableList<ErrorSocket> = mutableListOf()
    private val logger: Logger = LoggerFactory.getLogger(Clients::class.java)

    @Synchronized
    override fun sendToAllClients(response: ResponseItem) {
        clients.forEach { client ->
            try {
                client.send(message = response.toJson())
            } catch (ex: Exception) {
                logger.error("Error occurred while sending response to all connected clients\n${ex.message}")
                removeClient(client = client)
            }
        }
    }

    @Synchronized
    override fun sendToSingleClient(response: ResponseItem) {
        clients.forEach { client ->
            if (client.getSocketId() == response.socketId) {
                try {
                    client.send(message = response.toJson())
                } catch (ex: Exception) {
                    logger.error("Error occurred while sending response to a single client\n${ex.message}")
                    removeClient(client = client)
                }
            }
        }
    }

    @Synchronized
    override fun addClient(client: ErrorSocket) {
        clients.add(client)
        logger.info("Client saved")
    }

    @Synchronized
    override fun removeClient(client: ErrorSocket) {
        if (clients.contains(client)) {
            clients.remove(client)
            logger.info("Client removed")
        }
    }

    @Synchronized
    override fun getAllClients(): List<ErrorSocket> = clients
}