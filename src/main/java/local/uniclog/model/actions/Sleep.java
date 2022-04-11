package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.utils.DataUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sleep implements ActionsInterface {
    @Builder.Default
    private Long time = 0L;

    @SneakyThrows
    @Override
    public void execute(String... args) {
        TimeUnit.MILLISECONDS.sleep(time);
        log.debug("Sleep: {}", this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "time" -> setTime(DataUtils.getLong(value, 0));
            default -> log.debug("Field: {}, not set: {}", key, value);
        }
    }

    @Override
    public String toString() {
        return format("%s [time=%s]", getType().name(), time);
    }

    @Override
    public ActionType getType() {
        return ActionType.SLEEP;
    }
}
