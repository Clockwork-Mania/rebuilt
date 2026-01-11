package frc.robot.states;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinearMachine {
    List<LinearState> states;
    int index = 0;

    public LinearMachine(List<LinearState> states) {
        this.states = states;
    }

    public void init() {
        states.get(index).init.forEach(Runnable::run);
    }

    public void periodic() {
        if(index >= states.size()) return;
        states.get(index).loop.forEach(Runnable::run);
        if(states.get(index).done.get()) {
            states.get(index).end.forEach(Runnable::run);
            ++index;
            if(index < states.size()) {
                states.get(index).init.forEach(Runnable::run);
            }
        }
    }
}
