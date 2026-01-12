package frc.team4013.frc2026.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4013.frc2026.Utility;


public class Shooter{
    public Motor shooter;
    private double speed;

    public Shooter(){
        this.shooter = new Motor(26);
    }

    public void turnOnShooter(){
        shooter.setPower(speed);
    }

    public void turnOffShooter(){
        shooter.setPower(0);
    }

    public void updateShooter(){
        SmartDashboard.putNumber("current speed: ", speed);

    }

    public void increaseSpeed(){
        speed += 0.05;
        speed = Utility.clamp(speed, -1, 1);
    }

    public void decreaseSpeed(){
        speed -= 0.05;
        speed = Utility.clamp(speed, -1, 1);
    }
}
