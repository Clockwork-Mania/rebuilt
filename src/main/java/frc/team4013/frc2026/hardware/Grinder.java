package frc.team4013.frc2026.hardware;

public class Grinder {
    public Swerve base;
//    public Shooter shooter;
    public Vision vision;
    public Shooter shooter;

    public Grinder() {
        base = new Swerve();
//        shooter = new Shooter();
        vision = new Vision();
        shooter = new Shooter();
    }
}
