package frc.team4013.frc2026;

import com.ctre.phoenix6.SignalLogger;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.team4013.frc2026.hardware.*;
import frc.team4013.frc2026.opmodes.*;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    Grinder bot;
    XboxController con;
    Field2d field;
    ShuffleboardTab main;

    RobotContainer robotContainer;
    SendableChooser<Class<?>> autoPicker, telePicker, testPicker;

    XboxController controller = new XboxController(0);

    SendableChooser<Class<?>> opmodePicker(OpmodeList.NamedOpmode modes[]) {
        SendableChooser<Class<?>> picker = new SendableChooser<>();
        if(modes.length > 0) picker.setDefaultOption(modes[0].name, modes[0].mode);
        for(int i = 1; i < modes.length; ++i) picker.addOption(modes[i].name, modes[i].mode);
        return picker;
    }


    void opInit(SendableChooser<Class<?>> picker) {
        Class<?> mode = picker.getSelected();
        try{op = (Opmode)mode.getDeclaredConstructor().newInstance();}
        catch(Exception e){}
        op.init(bot);
    }

    public Robot() {
        Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
        switch (BuildConstants.DIRTY) {
            case 0:
                Logger.recordMetadata("GitDirty", "All changes committed");
                break;
            case 1:
                Logger.recordMetadata("GitDirty", "Uncomitted changes");
                break;
            default:
                Logger.recordMetadata("GitDirty", "Unknown");
                break;
        }

        if (RobotBase.isReal()) {
            Logger.addDataReceiver(new WPILOGWriter());
            if (!DriverStation.isFMSAttached()) {
                Logger.addDataReceiver(new NT4Publisher());
            }
        } else if (Constants.kIsReplay) {
            setUseTiming(false);
            String logPath = LogFileUtil.findReplayLog();
            Logger.setReplaySource(new WPILOGReader(logPath));
            Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
        } else if (RobotBase.isSimulation()) {
            Logger.addDataReceiver(new NT4Publisher());
            Logger.addDataReceiver(new WPILOGWriter());
        }

        Logger.start();
        if (!Logger.hasReplaySource()) {
            RobotController.setTimeSource(RobotController::getFPGATime);
        }

        try(PowerDistribution pdh = new PowerDistribution()) {
            pdh.setSwitchableChannel(true);
            SmartDashboard.putBoolean("pdh status", pdh.getSwitchableChannel());
        }

        robotContainer = new RobotContainer();
        if (RobotBase.isSimulation()) {
            //TODO add drive subsystem stuff to robotcontainer
//            robotContainer.getDriveSubsystem().resetOdometry(new Pose2d(3, 3, new Rotation2d()));
        }

        SmartDashboard.putData("Command Scheduler", CommandScheduler.getInstance());
        SignalLogger.enableAutoLogging(false);

        //TODO pathplanner warmup


        autoPicker = opmodePicker(OpmodeList.auto);
        telePicker = opmodePicker(OpmodeList.teleop);
        testPicker = opmodePicker(OpmodeList.test);

        // main = Shuffleboard.getTab("Opmode Selection");
        // main.add(autoPicker).withWidget(BuiltInWidgets.kComboBoxChooser);
        // main.add("Teleop", telePicker);//.withWidget(BuiltInWidgets.kComboBoxChooser);
        // main.add("Test", testPicker);//.withWidget(BuiltInWidgets.kComboBoxChooser);

        // SendableChooser<String> chooser = new SendableChooser<String>();

        // Shuffleboard.getTab("New tab")
        // 	.add("test!", "yay!");

        SmartDashboard.putData("auto", autoPicker);
        SmartDashboard.putData("teleop", telePicker);
        SmartDashboard.putData("test", testPicker);


        bot = new Grinder();
    }


    @Override
    public void robotPeriodic() {
        // bot.arm.elevator.periodic();
    }

    Opmode op;

    @Override
    public void autonomousInit() {
        opInit(autoPicker);
    }

    @Override
    public void autonomousPeriodic() {
        op.periodic();
    }

    @Override
    public void teleopInit() {
        opInit(telePicker);
    }

    @Override
    public void teleopPeriodic() {
        op.periodic();
    }

    @Override
    public void testInit() {
        // test = new ElevatorTest();
        // test.init();
        opInit(testPicker);
    }

    @Override
    public void testPeriodic() {
        op.periodic();
        // test.periodic();
    }
}
