package local.uniclog.model.actions.impl;

import local.uniclog.model.actions.ActionsInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class Default implements ActionsInterface {
    @Override
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        return null;
    }
}
