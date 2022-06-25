package local.uniclog.services;

import local.uniclog.model.actions.ActionContainer;
import local.uniclog.model.actions.ActionsInterface;
import local.uniclog.model.actions.WhileModel;
import local.uniclog.model.actions.impl.ActionEnd;
import local.uniclog.model.actions.impl.ActionWhile;
import local.uniclog.model.actions.impl.ActionWhileBrakeByColor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static java.util.Arrays.stream;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

@Slf4j
public class ActionProcessExecuteService {
    private static final AtomicBoolean hook = new AtomicBoolean(false);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private ActionProcessExecuteService() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    /**
     * ActionProcessExecute thread initialize
     *
     * @param hook           hook state
     * @param actionsList    action's list: text commands
     * @param actionCallBack callback
     * @return hook state
     */
    public static boolean initialize(boolean hook, String actionsList, Consumer<Boolean> actionCallBack) {
        if (ActionProcessExecuteService.hook.get() == hook) {
            return hook;
        }

        ActionProcessExecuteService.stop();
        ActionProcessExecuteService.hook.set(hook);
        if (!ActionProcessExecuteService.hook.get()) {
            return false;
        }

        var processKeyHookThread = new ActionProcessExecuteService.ActionProcessExecuteThread(actionsList, actionCallBack);
        synchronized (ActionProcessExecuteService.hook) {
            executorService = Executors.newSingleThreadExecutor();
            executorService.submit(processKeyHookThread);
            executorService.shutdown();
        }
        return true;
    }

    public static void stop() {
        ActionProcessExecuteService.hook.set(false);
        synchronized (hook) {
            executorService.shutdownNow();
        }
    }

    @Slf4j
    private static class ActionProcessExecuteThread implements Runnable {
        private final ActionService service = new ActionService();
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
            var container = service.getContainer();
            var size = container.getData().size();
            var index = 0;
            try {
                while (index < size) {
                    if (!ActionProcessExecuteService.hook.get()) {
                        break;
                    }
                    var action = container.getAction(index);
                    action.execute();

                    index = correctIndexByAction(index, container, action);
                    index++;
                }
                stop();
            } catch (InterruptedException e) {
                stop();
                Thread.currentThread().interrupt();
            }
        }

        private void stop() {
            actionCallBack.accept(true);
            ActionProcessExecuteService.hook.set(false);
            log.debug("ActionProcessExecuteThread stop");
        }

        private Integer correctIndexByAction(int index, ActionContainer container, ActionsInterface action) {
            if (action instanceof ActionWhile it && it.getCount() > 0) {
                container.whileModelStackPush(new WhileModel(index, it.getCount() - 1));

            } else if (action instanceof ActionWhileBrakeByColor it) {
                index = correctByActionWhileBrakeByColor(index, container, it);

            } else if (action instanceof ActionEnd) {
                index = correctByActionEnd(index, container);
            }

            return index;
        }

        private Integer correctByActionWhileBrakeByColor(int index, ActionContainer container, ActionWhileBrakeByColor action) {
            if (action.checkColorChange()) {
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
            return index;
        }

        private Integer correctByActionEnd(int index, ActionContainer container) {
            var whileModel = container.whileModelStackPeekFirst();
            if (Objects.nonNull(whileModel) && whileModel.getCount() > 0) {
                index = whileModel.setIteration();
            } else {
                container.whileModelStackPollFirst();
                whileModel = container.whileModelStackPeekFirst();
                if (Objects.nonNull(whileModel) && whileModel.getCount() > 0) {
                    index = whileModel.setIteration();
                }
            }
            return index;
        }
    }
}
