package frc.team4013.frc2026.opmodes;

import frc.team4013.frc2026.hardware.Grinder;

public interface Opmode {
    void init(Grinder bot);
    void periodic();
}
