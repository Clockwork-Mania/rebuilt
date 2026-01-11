package frc.team4013.frc2026.opmodes.teleop;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4013.frc2026.hardware.*;
import frc.team4013.frc2026.opmodes.Opmode;
import org.littletonrobotics.junction.Logger;

import java.util.List;
import java.util.function.Predicate;

public class PreFull implements Opmode {
    Grinder bot;
    CWController con0, con1;
    List<CWController> cons;
    Field2d field;
    Timer prepTimer;
    String state;

    public void init(Grinder bot) {
        this.bot = bot;
        con0 = new CWController(0);
        con1 = new CWController(1);
        cons = List.of(con0, con1);
        prepTimer = new Timer();
        state = "";
    }

    boolean intake = true, coral = false, coralPrep = false;

    boolean either(Predicate<CWController> cond) {
        return cond.test(con0) || cond.test(con1);
    }

    public void periodic() {
        con0.update();
        con1.update();

        double
            f = -con0.getLeftY()*.4,
            s = con0.getLeftX()*.4,
            r = con0.getRightX()*.4;
        bot.base.drive(s, f, r, true);
        bot.base.periodic();

        Logger.recordOutput("Controller Forward Input", f);
        Logger.recordOutput("Controller Strafe Input", s);
        Logger.recordOutput("Controller Rotation Input", r);
        for (int i = 0; i < bot.base.wheels.length; i++) {
            Logger.recordOutput("Module " + i + " Spin Power", bot.base.wheels[i].spinMotorPower);
            Logger.recordOutput("Module " + i + " Power Power", bot.base.wheels[i].powerMotorPower);
        }

        SmartDashboard.putNumber("forward", f);
        SmartDashboard.putNumber("strafe", s);
        SmartDashboard.putNumber("rotation", r);
        SmartDashboard.putNumber("heading", bot.base.heading()*180/Math.PI);

        if(
            either(CWController::getLeftBumperButton) &&
            either(CWController::getRightBumperButton)
        ) {
            bot.base.resetGyro();
        }

        // if(either(CWController::getXButton)) {
        //     bot.arm.startSeq(Arm.CORAL_READY_SEQ);
        // }
        // if(either(CWController::getLeftButton)) {
        //     bot.arm.startSeq(Arm.CORAL_INTAKE_SEQ);
        // }
        // if(either(CWController::getBButton)) {
        //     bot.arm.startSeq(Arm.CORAL_L2_SEQ);
        // }
        // if(either(CWController::getDownButton)) {
        //     bot.arm.endSeq();
        // }

        // bot.arm.runSeq();

        /*// --------------- HOMING -------------- //
        // if(con.getLeftStickButton()) {
        //     if(con.getRightX() < -.3) {
        //         // home, tilting left
        //     }
        //     else if(con.getRightX() > .3) {
        //         // home, tilting right
        //     }
        //     else {
        //         // home on center
        //     }
        // }

        // ------------- MECHANISM ------------- //
        // if(con.getUpButtonPressed()) {
        //     // Algae from L3
        //     bot.arm.setTarget(Arm.ALGAE_L3);
        //     coral = false;
        //     intake = true;
        // }
        // else if(con.getRightButtonPressed()) {
        //     // Algae from L2
        //     bot.arm.setTarget(Arm.ALGAE_L2);
        //     coral = false;
        //     intake = true;
        // }
        // else if(con.getDownButtonPressed()) {
        //     // Algae from ground
        //     bot.arm.setTarget(Arm.ALGAE_GROUND);
        //     coral = false;
        //     intake = true;
        // }
        // else if(con.getLeftButtonPressed()) {
        //     // Coral from funnel
        //     bot.arm.setTarget(Arm.CORAL_PREP);
        //     coralPrep = true;
        //     prepTimer.reset();
        //     coral = true;
        //     intake = true;
        // }
        // else if(con.getYButtonPressed()) {
        //     if(coral) {
        //         // Coral to L4
        //         bot.arm.setTarget(Arm.CORAL_L4);
        //     }
        //     else {
        //         // Algae to barge
        //         bot.arm.setTarget(Arm.ALGAE_BARGE);
        //     }
        //     intake = false;
        // }
        // else if(con.getBButtonPressed()) {
        //     // Coral to L3
        //     bot.arm.setTarget(Arm.CORAL_L3);
        //     intake = false;
        // }
        // else if(con.getAButtonPressed()) {
        //     if(coral) {
        //         // Coral to L2
        //         bot.arm.setTarget(Arm.CORAL_L2);
        //     }
        //     else {
        //         // Algae to processor
        //         bot.arm.setTarget(Arm.ALGAE_PROC);
        //     }
        //     intake = false;
        // }
        // else if(con.getXButtonPressed()) {
        //     // Raise to ready position
        //     bot.arm.setTarget(Arm.READY);
        //     // // Coral to L1
        //     // bot.arm.setTarget(Arm.CORAL_L1);
        //     // intake = false;
        // }

        // if(coralPrep && prepTimer.hasElapsed(3)) {
        //     bot.arm.setTarget(Arm.CORAL_INTAKE);
        //     coralPrep = false;
        // }

        SmartDashboard.putBoolean("Coral?", coral);
        SmartDashboard.putBoolean("Intaking?", intake);*/

        // ---------------- CLAW --------------- //

        // if(either(CWController::getRightTriggerButton)) {
        //     if (coral) {
        //         if (intake) {
        //             bot.arm.claw.set(Claw.INTAKE_CORAL);
        //         }
        //         else {
        //             bot.arm.claw.set(Claw.OUTTAKE_CORAL);
        //         }
        //     }
        //     else {
        //         if (intake) {
        //             bot.arm.claw.set(Claw.INTAKE_ALGAE);
        //         }
        //         else{
        //             bot.arm.claw.set(Claw.OUTTAKE_ALGAE);
        //         }
        //     }
        // }

        // if(either(CWController::getLeftTriggerButton)) {
        //     if (coral) {
        //         if (intake) {
        //             bot.arm.claw.set(Claw.OUTTAKE_CORAL);
        //         }
        //         else {
        //             bot.arm.claw.set(Claw.INTAKE_CORAL);
        //         }
        //     }
        //     else {
        //         if (intake) {
        //             bot.arm.claw.set(Claw.OUTTAKE_ALGAE);
        //         }
        //         else{
        //             bot.arm.claw.set(Claw.INTAKE_ALGAE);
        //         }
        //     }
        // }

        // if(either(CWController::getLeftTriggerButtonReleased)) {
        //     bot.arm.claw.stop();
        // }
    }
}
