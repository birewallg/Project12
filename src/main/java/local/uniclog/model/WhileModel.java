package local.uniclog.model;

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
