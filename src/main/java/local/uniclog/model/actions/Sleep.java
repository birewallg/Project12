package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
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

    @Override
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
        TimeUnit.MILLISECONDS.sleep(time);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        if (Objects.nonNull(args))
            args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        if ("time".equals(key)) {
            setTime(DataUtils.getLong(value, 0));
        } else {
            log.debug("Field: {}, not set: {}", key, value);
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
