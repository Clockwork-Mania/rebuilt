package frc.robot.states;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LinearState {
    public List<Runnable> init, loop, end;
    public Supplier<Boolean> done;

    public LinearState() {
        init = new ArrayList<Runnable>();
        loop = new ArrayList<Runnable>();
        // done = (()=>{return false;});
    }
}
