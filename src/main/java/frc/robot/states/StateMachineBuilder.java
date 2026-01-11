package frc.robot.states;

import java.time.chrono.ThaiBuddhistChronology;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StateMachineBuilder {
    Map<String, State> map;
    State starting, state;

    public StateMachineBuilder() {
        this.map = new HashMap<String, State>();
        starting = state = null;
    }
    
    public StateMachineBuilder state(String name) {
        if(starting == null) {
            starting = state = new State();
            map.put(name, state);
        }
        return this;
    }

    public StateMachineBuilder start(Runnable f) {
        state.starts.add(f);
        return this;
    }
    
    public StateMachineBuilder run(Runnable f) {
        state.runs.add(f);
        return this;
    }

    public StateMachineBuilder end(Runnable f) {
        state.ends.add(f);
        return this;
    }
    
    public StateMachineBuilder next(Supplier<Boolean> p, String name) {
        state.nexts.add(()->p.get()?name:null);
        return this;
    }

    public StateMachineBuilder next(Supplier<Boolean> p, Supplier<String> name) {
        state.nexts.add(()->p.get()?name.get():null);
        return this;
    }

    public StateMachine build() {
        return new StateMachine(map, starting);
    }

    // TODO: add auto moving step
    public StateMachineBuilder move(double f, double s, double r) {
        return this
            .start(()->{})
            .run(()->{})
            .end(()->{})
            .next(()->true, "");
    } 
}
