package frc.robot.states;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class State {
    List<Runnable> starts, runs, ends;
    List<Supplier<String>> nexts;

    public State() {
        this.starts = new ArrayList<>();
        this.runs = new ArrayList<>();
        this.ends = new ArrayList<>();
        this.nexts = new ArrayList<>();
    }

    public void start() {starts.forEach(f->f.run());}
    public void run() {runs.forEach(f->f.run());}
    public void end() {ends.forEach(f->f.run());}
    public String next() {
        String s = null;
        for(var f : nexts) {
            s = f.get();
            if(f != null) break;
        }
        return s;
    }
}
