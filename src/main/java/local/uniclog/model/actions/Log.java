package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log implements ActionsInterface {

    public Log(String description) {

    }

    @Override
    public void execute(String... args) {
        log.info(getType().toString());
    }

    @Override
    public ActionType getType() {
        return ActionType.LOG;
    }
}
