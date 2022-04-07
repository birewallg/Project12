package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Builder
public class Sleep implements ActionsInterface {
    @Builder.Default
    private Long time = 0L;

    @SneakyThrows
    @Override
    public void execute(String... args) {
        TimeUnit.MILLISECONDS.sleep(time);
    }

    @Override
    public ActionType getType() {
        return ActionType.SLEEP;
    }
}
