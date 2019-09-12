package errormanager.display.interfaces

import apibuilder.error.response.ResponseItem

interface IDisplaySocketHandler {
    fun send(response: ResponseItem)
}