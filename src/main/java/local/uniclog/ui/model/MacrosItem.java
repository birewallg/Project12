package local.uniclog.ui.model;

import lombok.*;

import static local.uniclog.utils.ConfigConstants.EMPTY;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_NEW_MACROS;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class MacrosItem {
    private String name = TEMPLATE_NEW_MACROS;

    private String text = EMPTY;

    private String path = EMPTY;

    private Integer activation = null;

    public MacrosItem(String name, String text) {
        this(name, text, EMPTY, null);
    }

    public MacrosItem(String name, String text, String path) {
        this(name, text, path, null);
    }

    @Override
    public String toString() {
        return name;
    }
}
