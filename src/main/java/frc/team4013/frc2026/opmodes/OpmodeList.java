package frc.team4013.frc2026.opmodes;

// import frc.robot.opmodes.auto.*;
import frc.team4013.frc2026.opmodes.teleop.*;
import frc.team4013.frc2026.opmodes.test.*;


public class OpmodeList {
    public static class NamedOpmode {
        public String name;
        public Class<?> mode;
        NamedOpmode(String name, Class<?> mode) {
            this.name = name;
            this.mode = mode;
        }
    }

    public static NamedOpmode auto[] = {
        // new NamedOpmode("Laura", LauraAuto.class),
        // new NamedOpmode("Clipped", ClippedAuto.class),
        // new NamedOpmode("Vision", VisionAuto.class),
        // new NamedOpmode("Leave", TimedAuto.class),
        // new NamedOpmode("None", NoAuto.class),
        // new NamedOpmode("AprilTag", AprilTagAuto.class)
        // new NamedOpmode("Basic", BasicAuto.class),
        // new NamedOpmode("New", NewAuto.class),
        // new NamedOpmode("Homing", HomingAuto.class),
    };

    public static NamedOpmode teleop[] = {
        // new NamedOpmode("(THIS ONE) Positioning", PositioningTele.class),
        // new NamedOpmode("Full", FullTeleop.class),
        // new NamedOpmode("April Tag Align", AprilTagAlign.class),
        // new NamedOpmode("Sensor Test", Sensors.class),
        // new NamedOpmode("Led Test", LedTest.class),
        // new NamedOpmode("IMU Test", LedTest.class),
        // new NamedOpmode("Full Test", FullTest.class),
        new NamedOpmode("Pre-Full", PreFull.class)
    };

    // added in testing
    public static NamedOpmode test[] = {
        // new NamedOpmode("Manual", Manual.class),
        // new NamedOpmode("New Targeting", NewTargeting.class),
        // new NamedOpmode("Position Testing", PositionTesting.class),
        // new NamedOpmode("Targeting", Targeting.class)
        // new NamedOpmode("Position Test", PositionTesting.class),
        // new NamedOpmode("Elevator Test", ElevatorTest.class)
        new NamedOpmode("Shooter test", testShooter.class)
    };
}
