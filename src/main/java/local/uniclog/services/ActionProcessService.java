package local.uniclog.services;

import local.uniclog.model.ActionContainer;
import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.actions.Log;
import local.uniclog.model.actions.MouseClick;
import local.uniclog.model.actions.Sleep;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Data
public class ActionProcessService {
    private ActionContainer container = new ActionContainer();

    public ActionProcessService addAction(ActionsInterface instruction) {
        container.add(instruction);
        return this;
    }

    public void loadConfigurationFromFile() {
        setConfiguration(Arrays.stream(Objects.requireNonNull(FileServiceWrapper.read())
                .trim()
                .replaceAll("[ \\t\\x0B\\f\\r]", "")
                .split("\n")).toList());
    }

    public ActionProcessService clear() {
        container.clear();
        return this;
    }

    public void executeActionContainer() {
        container.getData().forEach(ActionsInterface::execute);
    }

    public ActionProcessService setConfiguration(List<String> actionLines) {
        container.clear();
        actionLines.forEach(line -> {
                    ActionType type = getActionType(line);
                    if (type == ActionType.DEFAULT) {
                        return;
                    }
                    Map<String, String> map = getActionMap(line);
                    ActionsInterface action = getAction(type, map);
                    container.add(action);
                }
        );
        log.debug("{}", actionLines);
        log.debug("{}", container);
        return this;
    }

    private ActionsInterface getAction(ActionType actionType, Map<String, String> map) {
        ActionsInterface action = switch (actionType) {
            case MOUSE_CLICK -> new MouseClick();
            case LOG -> new Log();
            case SLEEP -> new Sleep();

            default -> new Log("default");
        };
        return action.fieldInjection(map);
    }

    private ActionType getActionType(String line) {
        try {
            return ActionType.getType(line.substring(0, line.indexOf('[')));
        } catch (StringIndexOutOfBoundsException e) {
            log.error("ActionType is not parse");
            return ActionType.DEFAULT;
        }
    }

    private Map<String, String> getActionMap(String line) {
        String map = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
        return Arrays.stream(map.split(","))
                .map(param -> param.split("="))
                .collect(toMap(it -> it[0], it -> it[1]));
    }
}
