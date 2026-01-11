package frc.robot.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LinearMachineBuilder {
    List<LinearState> states;
    LinearState curr;

    public LinearMachineBuilder() {
        states = new ArrayList<LinearState>();
    }

    public LinearMachineBuilder init(Runnable init) {
        curr.init.add(init);
        return this;
    }

    public LinearMachineBuilder loop(Runnable loop) {
        curr.loop.add(loop);
        return this;
    }

    public LinearMachineBuilder end(Runnable end) {
        curr.end.add(end);
        return this;
    }

    public LinearMachineBuilder until(Supplier<Boolean> done) {
        curr.done = done;
        return this;
    }

    public LinearMachineBuilder then() {
        states.add(curr);
        curr = new LinearState();
        return this;
    }

    public LinearMachine build() {
        return new LinearMachine(states);
    }
}
