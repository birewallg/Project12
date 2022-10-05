package local.uniclog.model.actions;

import local.uniclog.model.actions.impl.ActionWhile;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WhileModel extends ActionWhile {
    private final Integer index;
    // @Getter
    // private Integer count

    public WhileModel(int index, ActionWhile actionWhile) {
        super(setStartCount(actionWhile), actionWhile.getEternity());
        this.index = index;
    }

    private static Integer setStartCount(ActionWhile aw) {
        return Boolean.TRUE.equals(aw.getEternity()) ? 1 : aw.getCount() - 1;
    }

    public Integer setIteration() {
        if (Boolean.FALSE.equals(getEternity()))
            setCount(getCount() - 1);
        return index;
    }
}
