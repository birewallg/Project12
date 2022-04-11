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
    public void execute(String... args) {
        log.debug("{}: {}", getType().name(), this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "count" -> setCount(DataUtils.getInteger(value, 0));
            default -> log.debug("Field: {}, not set: {}", key, value);
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
