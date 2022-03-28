package local.uniclog.instructions;

import local.uniclog.InstructionsType;

import static local.uniclog.InstructionsType.*;

public interface Instructions {
    void execute(String... args);

    default InstructionsType getType() {
        return DEFAULT;
    }
}
