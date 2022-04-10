package local.uniclog.services;

import local.uniclog.model.ActionsInterface;
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
            for (ActionsInterface action : service.getContainer().getData()) {
                if (!ActionProcessExecuteService.hook.get()) {
                    break;
                }
                action.execute();
            }
            actionCallBack.accept(true);
            hook.set(false);
        }
    }
}
