package local.uniclog.utils;

import local.uniclog.model.ActionsInterface;
import local.uniclog.utils.gson.GsonObjectWrapper;
import lombok.Data;

@Data
public class ActionProcessor {
    private final GsonObjectWrapper gson = new GsonObjectWrapper();
    private ActionContainer container = new ActionContainer();

    public void addAction(ActionsInterface instruction) {
        container.add(instruction);
    }

    public void load() {
        container = gson.readContainer(ActionsInterface.class, ActionContainer.class);
    }

    public void save() {
        gson.writeContainer(container, ActionsInterface.class);
    }

    public void clear() {
        container.clear();
    }

}
