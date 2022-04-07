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

import static com.sun.jna.Pointer.nativeValue;
import static com.sun.jna.platform.win32.WinUser.VK_CONTROL;

@Slf4j
public class JnaKeyHookService {
    private static final AtomicBoolean hook = new AtomicBoolean(false);

    public boolean initialize(boolean hook) {
        if (JnaKeyHookService.hook.get() == hook) {
            return hook;
        }
        JnaKeyHookService.hook.set(hook);
        if (!JnaKeyHookService.hook.get()) {
            return false;
        }
        var jnaKeyHookThread = new JnaKeyHookThread();
        var thread = new Thread(jnaKeyHookThread);
        thread.start();

        return true;
    }

    private static class JnaKeyHookThread implements Runnable {
        private WinUser.HHOOK hHook;

        @SneakyThrows
        @Override
        public void run() {
            var hMod = Kernel32.INSTANCE.GetModuleHandle(null);
            LowLevelKeyboardProc lpfn = (nCode, wParam, lParam) -> {
                if (nCode >= 0) {
                    switch (wParam.intValue()) {
                        case WinUser.WM_KEYDOWN, WinUser.WM_SYSKEYDOWN -> handleKeyDown(lParam.vkCode);
                    }
                }
                var wlParam = new WinDef.LPARAM(nativeValue(lParam.getPointer()));
                return User32.INSTANCE.CallNextHookEx(hHook, nCode, wParam, wlParam);
            };
            hHook = User32.INSTANCE.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, lpfn, hMod, 0);
            if (hHook == null) {
                log.info("Hook is not set");
                return;
            }
            log.info("Hooked");

            var msg = new WinUser.MSG();
            while (JnaKeyHookService.hook.get()) {
                User32.INSTANCE.PeekMessage(msg, null, 0, 0, 0);
                TimeUnit.MILLISECONDS.sleep(100);
            }

            if (User32.INSTANCE.UnhookWindowsHookEx(hHook)) {
                log.info("Unhooked");
            }
        }

        private void handleKeyDown(int vkCode) {
            log.info("Key = {}", vkCode);
            if (vkCode == VK_CONTROL || vkCode == 162) {
                log.info("VK_CONTROL pressed!");
            }
        }
    }
}
