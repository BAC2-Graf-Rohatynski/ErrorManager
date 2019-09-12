package errormanager.handler.interfaces

import org.json.JSONArray

interface ICommandHandlerAction {
    fun run(): Any
    fun build(message: JSONArray): ICommandHandlerAction
}