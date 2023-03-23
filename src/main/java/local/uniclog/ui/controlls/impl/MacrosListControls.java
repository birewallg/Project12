package local.uniclog.ui.controlls.impl;

import local.uniclog.ui.controlls.ControlServiceAbstract;
import local.uniclog.ui.model.MacrosItem;

import static java.util.Objects.isNull;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_NOT_SET_CONTROLS;

/**
 * Macros List Controls
 * actions:
 * -add
 * -remove
 *
 * @version 1.0
 */
public class MacrosListControls extends ControlServiceAbstract {

    public MacrosListControls() {
        if (isNull(cp)) throw new IllegalStateException(TEMPLATE_NOT_SET_CONTROLS);
    }

    /**
     * Button: newMacros Button
     */
    public void newMacrosButtonAction() {
        macrosListAddItem(new MacrosItem());
    }

    /**
     * Button: delMacros Button
     */
    public void delMacrosButtonAction() {
        var index = cp.getMacrosList().getSelectionModel().getSelectedIndex();
        if (index == -1) return;
        macrosListRemoveItem(index);
    }
}
