package local.uniclog.actions;

import static local.uniclog.actions.ActionType.*;

public interface ActionsInterface {
    void execute(String... args);

    default ActionType getType() {
        return DEFAULT;
    }
}
