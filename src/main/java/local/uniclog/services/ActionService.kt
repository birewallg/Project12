package local.uniclog.services

import local.uniclog.model.ActionContainer
import local.uniclog.model.actions.types.ActionType
import local.uniclog.model.actions.types.ActionType.DEFAULT
import local.uniclog.model.actions.types.ActionType.END
import local.uniclog.utils.ConfigConstants.*
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
        if (!line.contains("\\[(.*)]".toRegex())) {
            logger.warn("ParamsMap is empty - {}", line)
            return emptyMap()
        }
        val args = line.substring(line.indexOf(PARAM_START) + 1, line.indexOf(PARAM_END))
        return args.split(COMMA).dropLastWhile { it.isEmpty() }
            .map { it.split("=") }
            .filter {
                if (it.size != 2) logger.warn("ParamsMap ist parse - {}", line)
                it.size == 2
            }
            .associate { it[0] to it[1] }
    }
}