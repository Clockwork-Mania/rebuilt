package frc.team4013.frc2026.opmodes.test;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4013.frc2026.Utility;
import frc.team4013.frc2026.hardware.*;
import frc.team4013.frc2026.opmodes.Opmode;
import frc.team4013.frc2026.opmodes.teleop.CWController;

public class IMUTest implements Opmode {
    Grinder bot;
    CWController con;

    public boolean holding = false;

    public void init(Grinder bot) {
        this.bot = bot;
        con = new CWController(1);
    }

    public void periodic() {
        con.update();

        double
            f = -Utility.sgnsqr(con.getLeftY())*.4,
            s = Utility.sgnsqr(con.getLeftX())*.4,
            r = Utility.sgnsqr(con.getRightX())*.4;
        bot.base.drive(s, f, r, false);
        bot.base.periodic();

        SmartDashboard.putNumber("IMU value", bot.base.heading());
        SmartDashboard.putNumber("IMU value deg", Math.toDegrees(bot.base.heading()));
    }
}
