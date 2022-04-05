package local.uniclog.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActionContainer {
    private List<ActionsInterface> data = new ArrayList<>();

    public void add(ActionsInterface action) {
        data.add(action);
    }

    public void clear() {
        data.clear();
    }
}
