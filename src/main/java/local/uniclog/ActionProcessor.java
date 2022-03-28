package local.uniclog;

import local.uniclog.actions.ActionsInterface;

import java.util.ArrayList;
import java.util.List;

public class ActionProcessor {
    private List<ActionsInterface> commands = new ArrayList<>();

    public void loadInstructions() {
        // todo parse command
    }

    public void clearList() {
        commands.clear();
    }

    public void addCommand(ActionsInterface instruction) {
        commands.add(instruction);
    }
}
