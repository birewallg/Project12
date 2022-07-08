package local.uniclog.model.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class WhileModel {
    private final Integer index;
    @Getter
    private Integer count;

    public Integer setIteration() {
        --count;
        return index;
    }
}
