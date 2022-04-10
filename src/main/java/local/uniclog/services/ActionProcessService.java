package local.uniclog.services;

import local.uniclog.model.ActionContainer;
import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.actions.Log;
import local.uniclog.model.actions.MouseClick;
import local.uniclog.model.actions.Sleep;
import local.uniclog.utils.gson.GsonObjectWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Data
public class ActionProcessService {
    private final GsonObjectWrapper gson = new GsonObjectWrapper();
    private ActionContainer container = new ActionContainer();

    public ActionProcessService addAction(ActionsInterface instruction) {
        container.add(instruction);
        return this;
    }

    public void load() {
        container = gson.readContainer(ActionsInterface.class, ActionContainer.class);
    }

    public void save() {
        gson.writeContainer(container, ActionsInterface.class);
    }

    public ActionProcessService clear() {
        container.clear();
        return this;
    }

    public void executeActionContainer() {
        container.getData().forEach(ActionsInterface::execute);
    }

    public ActionProcessService getConfiguration(List<String> actionLines) {
        container.clear();
        actionLines.forEach(line -> {
                    String type = getActionType(line);
                    Map<String, String> map = getActionMap(line);
                    ActionsInterface action = getAction(type, map);
                    container.add(action);
                }
        );
        log.debug("{}", actionLines);
        log.debug("{}", container);
        return this;
    }

    private ActionsInterface getAction(String type, Map<String, String> map) {
        ActionsInterface action = switch (ActionType.getType(type)) {
            case MOUSE_CLICK -> new MouseClick();
            case LOG -> new Log();
            case SLEEP -> new Sleep();

            default -> new Log("default");
        };
        return action.fieldInjection(map);
    }

    private String getActionType(String line) {
        return line.substring(0, line.indexOf('['));
    }

    private Map<String, String> getActionMap(String line) {
        String map = line.substring(line.indexOf('[') + 1, line.indexOf(']'));
        return Arrays.stream(map.split(","))
                .map(param -> param.split("="))
                .collect(toMap(it -> it[0], it -> it[1]));
    }
}
