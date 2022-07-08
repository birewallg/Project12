package local.uniclog.model.actions.impl;

import local.uniclog.model.actions.ActionType;
import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.utils.DataUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActionWhile implements ActionsInterface {
    @Builder.Default
    private Integer count = 0;

    @Override
    public void execute(String... args) throws InterruptedException {
        log.debug("{}", this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        if (Objects.nonNull(args))
            args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        if ("count".equals(key)) {
            setCount(DataUtils.getInteger(value, 0));
        } else {
            log.debug("Field: {}, not set: {}", key, value);
        }
    }

    @Override
    public String toString() {
        return format("%s [count=%d]", getType().name(), count);
    }

    @Override
    public ActionType getType() {
        return ActionType.WHILE;
    }
}
