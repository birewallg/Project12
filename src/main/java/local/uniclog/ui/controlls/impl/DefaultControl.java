package local.uniclog.ui.controlls.impl;

import local.uniclog.ui.controlls.ControlServiceAbstract;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_NOT_SET_CONTROLS;

/**
 * App default controls
 *
 * @version 1.0
 */
public class DefaultControl extends ControlServiceAbstract {

    public DefaultControl() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

}
