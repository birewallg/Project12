package local.uniclog.services;

import local.uniclog.model.ActionContainer;
import local.uniclog.model.ActionType;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.actions.ActionWhile;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.util.Arrays.stream;

@Slf4j
public class ActionProcessExecuteService {
    private static final AtomicBoolean hook = new AtomicBoolean(false);

    public boolean initialize(boolean hook, String actionsList, Consumer<Boolean> actionCallBack) {
        if (ActionProcessExecuteService.hook.get() == hook) {
            return hook;
        }
        ActionProcessExecuteService.hook.set(hook);
        if (!ActionProcessExecuteService.hook.get()) {
            return false;
        }

        var processKeyHookThread = new ActionProcessExecuteService.ActionProcessExecuteThread(actionsList, actionCallBack);
        var thread = new Thread(processKeyHookThread);
        thread.start();

        return true;
    }

    public static void stop() {
        ActionProcessExecuteService.hook.set(false);
    }

    private static class ActionProcessExecuteThread implements Runnable {
        private final ActionProcessService service = new ActionProcessService();
        private final Consumer<Boolean> actionCallBack;

        public ActionProcessExecuteThread(String actionsListAsString, Consumer<Boolean> actionCallBack) {
            this.actionCallBack = actionCallBack;
            service.setConfiguration(
                    stream(actionsListAsString.trim()
                            .replaceAll("[ \\t\\x0B\\f\\r]", "")
                            .split("\n")).toList());
        }

        @Override
        public void run() {
            ActionContainer container = service.getContainer();
            int size = container.getData().size();
            int index = 0;
            while (index < size) {
                if (!ActionProcessExecuteService.hook.get()) {
                    break;
                }
                ActionsInterface action = container.getAction(index);

                if (action.getType().equals(ActionType.WHILE)) {
                    if (((ActionWhile) action).getCount() > 0) {
                        container.setWhileLoopCount(((ActionWhile) action).getCount() - 1);
                        container.setWhileLoopIndex(index);
                    }
                } else if (action.getType().equals(ActionType.END) && container.getWhileLoopCount() > 0) {
                    container.setWhileLoopCount((container.getWhileLoopCount() - 1));
                    index = container.getWhileLoopIndex();
                }

                action.execute();

                index++;
            }

            actionCallBack.accept(true);
            hook.set(false);
        }
    }
}
