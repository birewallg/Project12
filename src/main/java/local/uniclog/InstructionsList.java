package local.uniclog;

import local.uniclog.instructions.Instructions;

import java.util.ArrayList;
import java.util.List;

public class InstructionsList {
    private List<Instructions> commands = new ArrayList<>();

    public void loadInstructions() {
        // todo parse command
    }

    public void clearList() {
        commands.clear();
    }

    public void addCommand(Instructions instruction) {
        commands.add(instruction);
    }
}
