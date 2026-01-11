package frc.team4013.frc2026.opmodes.test;
import edu.wpi.first.wpilibj.XboxController;
import frc.team4013.frc2026.opmodes.teleop.CWController;
import frc.team4013.frc2026.hardware.Grinder;
import frc.team4013.frc2026.opmodes.Opmode;

public class testShooter implements Opmode{
    private Grinder bot;
    CWController controller;


    public void init(Grinder bot){
        this.bot = bot;
        controller = new CWController(0);
    }

    public void periodic(){
        bot.shooter.updateShooter();
        
        if (controller.getAButtonPressed()){
            bot.shooter.turnOnShooter();
        }
        else if (controller.getBButtonPressed()){
            bot.shooter.turnOffShooter();
        }
        else if (controller.getLeftButtonPressed()){
            bot.shooter.increaseSpeed();
        }
        else if (controller.getRightButtonPressed()){
            bot.shooter.decreaseSpeed();
        }
        
    }
}