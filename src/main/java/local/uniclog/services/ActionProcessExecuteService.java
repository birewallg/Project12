package local.uniclog.services;

import local.uniclog.model.ActionContainer;
import local.uniclog.model.ActionsInterface;
import local.uniclog.model.WhileModel;
import local.uniclog.model.actions.ActionEnd;
import local.uniclog.model.actions.ActionWhile;
import local.uniclog.model.actions.ActionWhileBrakeByColor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
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
            service.setConfiguration(stream(actionsListAsString.trim()
                    .replaceAll("[ \\t\\x0B\\f\\r]", "")
                    .split("\n"))
                    .toList());
            log.debug("ActionProcessExecuteThread init");
        }

        @Override
        public void run() {
            log.debug("ActionProcessExecuteThread start");
            ActionContainer container = service.getContainer();
            int size = container.getData().size();
            int index = 0;
            while (index < size) {
                if (!ActionProcessExecuteService.hook.get()) {
                    break;
                }
                ActionsInterface action = container.getAction(index);
                action.execute();

                index = correctIndexByAction(index, container, action);
                index++;
            }

            actionCallBack.accept(true);
            hook.set(false);
            log.debug("ActionProcessExecuteThread stop");
        }

        private Integer correctIndexByAction(int index, ActionContainer container, ActionsInterface action) {
            if (action instanceof ActionWhile it && it.getCount() > 0) {
                container.whileModelStackPush(new WhileModel(index, it.getCount() - 1));

            } else if (action instanceof ActionWhileBrakeByColor it) {
                if (it.checkColorChange()) {
                    int whileCounts = 0;
                    for (int i = index; i < container.getData().size(); i++) {
                        if (container.getData().get(i) instanceof ActionWhile) {
                            whileCounts++;
                        }
                        if (container.getData().get(i) instanceof ActionEnd) {
                            whileCounts--;
                            if (whileCounts < 0) {
                                index = i + 1;
                                container.whileModelStackPollFirst();
                                break;
                            }
                        }
                    }
                }

            } else if (action instanceof ActionEnd) {
                WhileModel whileModel = container.whileModelStackPeekFirst();
                if (Objects.nonNull(whileModel) && whileModel.getCount() > 0) {
                    index = whileModel.setIteration();
                } else {
                    container.whileModelStackPollFirst();
                    whileModel = container.whileModelStackPeekFirst();
                    if (Objects.nonNull(whileModel) && whileModel.getCount() > 0) {
                        index = whileModel.setIteration();
                    }
                }
            }
            return index;
        }

    }
}
