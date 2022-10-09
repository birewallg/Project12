package local.uniclog.ui.controlls.service.impl;

import local.uniclog.ui.controlls.service.ControlService;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_NOT_SET_CONTROLS;

public class DefaultControl extends ControlService {
    public DefaultControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }
}
