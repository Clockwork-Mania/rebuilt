package frc.robot.opmodes;

import frc.robot.hardware.Grinder;

public interface Opmode {
    void init(Grinder bot);
    void periodic();
}
