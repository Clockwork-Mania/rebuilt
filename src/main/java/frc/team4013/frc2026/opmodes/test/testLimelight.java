package frc.team4013.frc2026.opmodes.test;

import frc.team4013.frc2026.hardware.Grinder;
import frc.team4013.frc2026.opmodes.Opmode;

public class testLimelight implements Opmode{
    Grinder bot;

    public void init(Grinder bot) {
        this.bot = bot;
    }

    public void periodic() {
        bot.vision.updateTX();
    }
}
