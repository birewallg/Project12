package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class Log implements ActionsInterface {
    private String description;

    @Override
    public void execute(String... args) {
        log.debug(getType().toString());
    }

    @Override
    public ActionType getType() {
        return ActionType.LOG;
    }
}
