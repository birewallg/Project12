package local.uniclog.ui.controlls.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static local.uniclog.utils.ConfigConstants.EMPTY;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_NEW_MACROS;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MacrosItem {
    private String name = TEMPLATE_NEW_MACROS;

    private String text = EMPTY;

    private String path = EMPTY;


    @Override
    public String toString() {
        return name;
    }
}
