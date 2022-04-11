package local.uniclog.model.actions;

import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
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
public class Log implements ActionsInterface {
    private String description;

    @Override
    public void execute(String... args) {
        log.debug("{}: {}", getType().name(), this);
    }

    @Override
    public ActionsInterface fieldInjection(Map<String, String> args) {
        if (Objects.nonNull(args))
            args.forEach(this::setFieldValue);
        return this;
    }

    private void setFieldValue(String key, String value) {
        switch (key) {
            case "description" -> setDescription(value);
            default -> log.debug("Field: {}, not set: {}", key, value);
        }
    }

    @Override
    public String toString() {
        return format("%s [description=%s]", getType().name(), description);
    }

    @Override
    public ActionType getType() {
        return ActionType.LOG;
    }
}
