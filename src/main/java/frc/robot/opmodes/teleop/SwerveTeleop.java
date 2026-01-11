package frc.robot.opmodes.teleop;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.opmodes.Opmode;
import frc.robot.Utility;
import frc.robot.hardware.Grinder;
import frc.robot.hardware.Swerve;

public class SwerveTeleop implements Opmode {
    Swerve drive;
    XboxController controller;

    public void init(Grinder bot) {
        drive = new Swerve();
        controller = new XboxController(0);
    }

    public void periodic() {
        double
            f = -Utility.sgnsqr(controller.getLeftY())*.4,
            s = Utility.sgnsqr(controller.getLeftX())*.4,
            r = Utility.sgnsqr(controller.getRightX())*.4;
        drive.drive(-s, -f, -r, false);
        drive.periodic();

        SmartDashboard.putNumber("forward", f);
        SmartDashboard.putNumber("strafe", s);
        SmartDashboard.putNumber("rotation", r);
        SmartDashboard.putNumber("heading", drive.heading());
        // SmartDashboard.putNumber("enc", driv)

        if(controller.getLeftBumperButton() && controller.getRightBumperButton()) {
            drive.resetGyro();
        }
    }
}
