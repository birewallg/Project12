package local.uniclog.instructions;

import local.uniclog.InstructionsType;

public class Log implements Instructions {

    public Log(String description) {

    }

    @Override
    public void execute(String... args) {
        System.out.println(getType().toString());
    }

    @Override
    public InstructionsType getType() {
        return Instructions.super.getType();
    }
}
