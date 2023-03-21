package local.uniclog.services

import local.uniclog.model.ActionContainer
import local.uniclog.model.actions.types.ActionType
import local.uniclog.model.actions.types.ActionType.DEFAULT
import local.uniclog.model.actions.types.ActionType.END
import org.slf4j.LoggerFactory

/**
 * Класс управляющий коллекцией событий
 */
class ActionService(actionLines: List<String>) {

    private val logger = LoggerFactory.getLogger(ActionService::class.java)
    val container = ActionContainer()

    init {
        fun buildActionsContainer(line: String) {
            val actionType = getActionType(line)
            if (actionType == DEFAULT) return
            val params = getParamsMap(line)
            val action = actionType.action.fieldInjection(params)
            container.add(action)
        }
        actionLines.forEach(::buildActionsContainer)
        container.add(END.action)
        logger.debug("{}", container)
    }

    private fun getActionType(line: String): ActionType =
        ActionType.values().firstOrNull { line.startsWith(it.name) } ?: DEFAULT

    private fun getParamsMap(line: String): Map<String, String> {
        val regex = """(\w+)(?>=)([^,|\]]*)""".toRegex()
        return regex.findAll(line).associate { it.groupValues[1] to it.groupValues[2] }
    }
}