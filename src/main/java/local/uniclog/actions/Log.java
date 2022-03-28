package local.uniclog.actions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Log extends ActionAbstract {

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
