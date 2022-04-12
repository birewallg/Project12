package local.uniclog.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ActionContainer {
    private ArrayList<ActionsInterface> data = new ArrayList<>();
    private Integer whileLoopCount = 0;
    private Integer whileLoopIndex = 0;

    public void add(ActionsInterface action) {
        data.add(action);
    }

    public ActionsInterface getAction(Integer index) {
        return data.get(index);
    }

    public void clear() {
        data.clear();
    }
}
