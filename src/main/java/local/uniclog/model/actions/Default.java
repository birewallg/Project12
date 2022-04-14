package local.uniclog.model.actions;

import local.uniclog.model.ActionsInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class Default implements ActionsInterface {
    @Override
    public void execute(String... args) {
        log.debug("{}", this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        return null;
    }
}
