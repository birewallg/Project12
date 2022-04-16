package local.uniclog.services;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.sun.jna.Pointer.nativeValue;

@Slf4j
public class JnaKeyHookService {
    private static final AtomicBoolean hook = new AtomicBoolean(false);

    public boolean initialize(boolean hook, Consumer<Integer> actionCallBack, int keyCode, boolean stopByHook) {
        if (JnaKeyHookService.hook.get() == hook) {
            return hook;
        }
        JnaKeyHookService.hook.set(hook);
        if (!JnaKeyHookService.hook.get()) {
            return false;
        }
        var jnaKeyHookThread = new JnaKeyHookThread(actionCallBack, keyCode, stopByHook);
        var thread = new Thread(jnaKeyHookThread);
        thread.start();

        return true;
    }

    public static void stop() {
        JnaKeyHookService.hook.set(false);
    }

    private static class JnaKeyHookThread implements Runnable {
        private WinUser.HHOOK hHook;
        private final Consumer<Integer> actionCallBack;
        private final int keyCode;
        private final boolean stopByHook;

        private JnaKeyHookThread(Consumer<Integer> actionCallBack, int keyCode, boolean stopByHook) {
            this.actionCallBack = actionCallBack;
            this.keyCode = keyCode;
            this.stopByHook = stopByHook;
        }

        @SneakyThrows
        @Override
        public void run() {
            var hMod = Kernel32.INSTANCE.GetModuleHandle(null);
            LowLevelKeyboardProc lpfn = (nCode, wParam, lParam) -> {
                if (nCode >= 0) {
                    switch (wParam.intValue()) {
                        case WinUser.WM_KEYUP, WinUser.WM_SYSKEYUP -> handleKeyDown(lParam.vkCode);
                    }
                }
                var wlParam = new WinDef.LPARAM(nativeValue(lParam.getPointer()));
                return User32.INSTANCE.CallNextHookEx(hHook, nCode, wParam, wlParam);
            };
            hHook = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, lpfn, hMod, 0);
            if (hHook == null) {
                log.error("Hook is not set");
                return;
            }
            log.debug("Hooked");

            var msg = new WinUser.MSG();
            while (JnaKeyHookService.hook.get()) {
                User32.INSTANCE.PeekMessage(msg, null, 0, 0, 0);
                TimeUnit.MILLISECONDS.sleep(100);
            }

            if (User32.INSTANCE.UnhookWindowsHookEx(hHook)) {
                log.debug("Unhooked");
            }
        }

        private void handleKeyDown(int vkCode) {
            log.debug("Key = {}", vkCode);

            if (keyCode != -1 && vkCode == keyCode) {
                initCallBack(vkCode);
            }
            if (keyCode == -1) {
                initCallBack(vkCode);
            }
        }

        private void initCallBack(int vkCode) {
            actionCallBack.accept(vkCode);
            if (stopByHook) {
                JnaKeyHookService.hook.set(false);
            }
        }
    }
}
