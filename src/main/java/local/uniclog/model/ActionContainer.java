package local.uniclog.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

@Data
public class ActionContainer {
    private ArrayList<ActionsInterface> data = new ArrayList<>();
    private Deque<WhileModel> whileModelStack = new LinkedList<>();

    public void whileModelStackPush(WhileModel model) {
        whileModelStack.push(model);
    }

    public WhileModel whileModelStackPeekFirst() {
        return whileModelStack.peekFirst();
    }

    public void whileModelStackPollFirst() {
        whileModelStack.pollFirst();
    }

    public void add(ActionsInterface action) {
        data.add(action);
    }

    public ActionsInterface getAction(Integer index) {
        return data.get(index);
    }

    public void clear() {
        data.clear();
        whileModelStack.clear();
    }
}
