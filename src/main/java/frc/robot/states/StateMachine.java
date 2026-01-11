package frc.robot.states;

import java.util.HashMap;
import java.util.Map;

public class StateMachine {
    public Map<String, State> map;
    public State state;
    boolean finished = false;

    public StateMachine() {
        this.map = new HashMap<String, State>();
        this.state = new State();
    }

    public StateMachine(Map<String, State> map, State state) {
        this.map = map;
        this.state = state;
    }

    public void start() {
        state.start();
    }

    public void run() {
        state.run();
        String s = state.next();
        if(s == null) return;
        if(s != "") state = map.get(s);
        else finished = true;
    }

    public boolean done() {return finished;}

    public void end() {
        state.end();
    }
}
