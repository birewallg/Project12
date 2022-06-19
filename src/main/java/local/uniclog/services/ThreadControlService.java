package local.uniclog.services;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

@Slf4j
public class ThreadControlService {
    private static final AtomicBoolean initializeHookListener = new AtomicBoolean(false);
    private static final AtomicBoolean initializeRunExecute = new AtomicBoolean(false);

    private ThreadControlService() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    public static boolean getRunExecuteState() {
        return !ThreadControlService.initializeRunExecute.get();
    }

    public static void startRunExecuteThread(String actionsList, Consumer<Boolean> actionCallBack, Consumer<Integer> jnaKeyHookCallback) {
        ThreadControlService.initializeRunExecute.set(true);

        // hook ctrl to stop
        ThreadControlService.startHookListenerThread(jnaKeyHookCallback, 162, true);
        // start action execute
        ActionProcessExecuteService.initialize(true, actionsList, actionCallBack);
    }

    public static void stopRunExecuteThread() {
        ThreadControlService.initializeRunExecute.set(false);

        ActionProcessExecuteService.stop();
        ThreadControlService.stopHookListenerThread();
    }

    public static boolean getHookListenerState() {
        return !ThreadControlService.initializeHookListener.get();
    }

    public static void startHookListenerThread(Consumer<Integer> actionCallBack, int keyCode, boolean stopByHook) {
        ThreadControlService.initializeHookListener.set(true);
        JnaKeyHookService.initialize(true, actionCallBack, keyCode, stopByHook);
    }

    public static void stopHookListenerThread() {
        ThreadControlService.initializeHookListener.set(false);
        JnaKeyHookService.stop();
    }
}
