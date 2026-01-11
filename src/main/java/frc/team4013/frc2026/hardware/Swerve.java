package frc.team4013.frc2026.hardware;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team4013.frc2026.Utility;
// import frc.robot.hardware.Vision.BasePitchTarget;
// import frc.robot.hardware.Vision.BaseTarget;

public class Swerve extends SubsystemBase  {
    // order is fl, bl, fr, br
    public SwerveModule frontLeft, backLeft, frontRight, backRight;
    public SwerveModule wheels[] = {frontLeft, backLeft, frontRight, backRight};

    private final ADIS16448_IMU gyro = new ADIS16448_IMU();

    public SwerveDriveOdometry odo;
    // Vision vision;

    double width = 23.5;
    SwerveDriveKinematics kin = new SwerveDriveKinematics(
        new Translation2d( width/2,  width/2),
        new Translation2d( width/2, -width/2),
        new Translation2d(-width/2,  width/2),
        new Translation2d(-width/2, -width/2)
    );

    public Swerve() {
        // frontLeft  = new SwerveModule(3, 7, 11, .457);
        // backLeft   = new SwerveModule(4, 8, 12, .268);
        // frontRight = new SwerveModule(2, 6, 10, .030);
        // backRight  = new SwerveModule(5, 9, 13, .309);
        frontLeft  = new SwerveModule(3, 7, 11, .455);
        backLeft   = new SwerveModule(4, 8, 12, .536);
        frontRight = new SwerveModule(2, 6, 10, .770);
        backRight  = new SwerveModule(5, 9, 13, .309);
        odo = new SwerveDriveOdometry(kin, heading2d(), positions());
        targetMode = TargetMode.None;
    }

    // public void addVision(Vision vision) {
    // 	this.vision = vision;
    // }

    public void setSpeeds(ChassisSpeeds speeds) {
        SwerveModuleState[] moduleStates = kin.toSwerveModuleStates(speeds);
        SwerveModuleState fL = moduleStates[0];
        SwerveModuleState bL = moduleStates[1];
        SwerveModuleState fR = moduleStates[2];
        SwerveModuleState bR = moduleStates[3];

        frontLeft.pidSpin(fL.angle.getRadians(), 0.02, fL.speedMetersPerSecond);
        frontRight.pidSpin(fR.angle.getRadians(), 0.02, fR.speedMetersPerSecond);
        backLeft.pidSpin(bL.angle.getRadians(), 0.02, bL.speedMetersPerSecond);
        backRight.pidSpin(bR.angle.getRadians(), 0.02, bR.speedMetersPerSecond);
    }

    public void drive(double strafePower, double forwardPower, double turnPower, boolean relative) {

        if(relative) {
            double magnitude = Math.sqrt(strafePower * strafePower + forwardPower * forwardPower);
            double ang = Math.atan2(forwardPower, strafePower);
            ang += heading();
            strafePower = magnitude * Math.cos(ang);
            forwardPower = magnitude * Math.sin(ang);
        }
        frontLeft.drive(strafePower, forwardPower, turnPower,  Math.PI*.25);
        backLeft.drive(strafePower, forwardPower, turnPower,  Math.PI*.75);
        frontRight.drive(strafePower, forwardPower, turnPower, -Math.PI*.25);
        backRight.drive(strafePower, forwardPower, turnPower, -Math.PI*.75);
    }

    public Pose2d target;
    public double visionTarget;
    // public BaseTarget baseVisionTarget;
    // public BasePitchTarget basePitchVisionTarget;
    public double visionHeading;
    public enum TargetMode {
        None,
        Odometry,
        Vision,
        BaseVision,
        BasePitchVision
    };
    public TargetMode targetMode;

    public void setTarget(Pose2d target) {
        this.target = target;
        targetMode = TargetMode.Odometry;
    }

    public void setVisionTarget(double target) {
        setVisionTarget(target, heading());
    }

    public void setVisionTarget(double target, double heading) {
        this.visionTarget = target;
        targetMode = TargetMode.Vision;
        visionHeading = heading;
    }

    // public void setBaseVisionTarget(BaseTarget target, double heading) {
    // 	this.baseVisionTarget = target;
    // 	targetMode = TargetMode.BaseVision;
    // 	visionHeading = heading;
    // }

    // public void setBasePitchVisionTarget(BasePitchTarget target, double heading) {
    // 	this.basePitchVisionTarget = target;
    // 	targetMode = TargetMode.BasePitchVision;
    // 	visionHeading = heading;
    // 	yawSum = 0; areaSum = 0;
    // }

    public void setTarget(double x, double y, double h) {
        setTarget(new Pose2d(x, y, new Rotation2d(h)));
    }

    public void driveto() {
        switch(targetMode) {
            case Odometry:
                drivetoOdo();
                break;
            case Vision:
                // drivetoVision();
                break;
            case BaseVision:
                // drivetoBaseVision();
                break;
            case BasePitchVision:
                // drivetoBasePitchVision();
                break;
            default:
                stop();
        }
    }

    double MAG_KP = .4, DH_KP = .2;
    double MAG_CAP = .4, DH_CAP = .2;
    public void drivetoOdo() {
        Translation2d tr = target.minus(pose()).getTranslation();
        double mag = tr.getNorm(), ang = tr.getAngle().getRadians();
        double dh = Utility.fixang(target.getRotation().getRadians()-heading());
        double vy, vx;
        mag = Math.min(mag*MAG_KP, MAG_CAP);
        dh = Utility.clamp(dh*DH_KP, -DH_CAP, DH_CAP);
        vx = mag * Math.cos(ang);
        vy = mag * Math.sin(ang);
        drive(vx, vy, dh, true);
    }

    double VIS_KP = 0.01;
    // public void drivetoVision() {
    // 	if(vision.foundTag()) {
    // 		double err = visionTarget-vision.getX();
    // 		double dh = Utility.fixang(visionHeading-heading());   //also btw why does this use target thats not even involved with drivetovision()
    // 		dh = Utility.clamp(dh*DH_KP, -DH_CAP, DH_CAP);
    // 		drive(VIS_KP*err, 0, dh, false);
    // 	}
    // 	else stop();
    // }

    double BVIS_YAW_KP = 0.006, BVIS_YAW_KI = 0.00003, BVIS_YAW_ICAP = 0.1/BVIS_YAW_KI;
    double BVIS_AREA_KP = .09/*0.2*/, BVIS_AREA_KI = .00017/*0.0005*/, BVIS_AREA_ICAP = 0.2/BVIS_AREA_KI;
    double BVIS_AREA_INT_THRESH = 0.5, BVYIT = 4;
    double BVYC = 0.4, BVAC = 0.4;
    double yawSum, areaSum;

    // public void drivetoBaseVision() {
    // 	SmartDashboard.putNumber("left target yaw", baseVisionTarget.left.yaw);
    // 	SmartDashboard.putNumber("left target area", baseVisionTarget.left.area);
    // 	SmartDashboard.putNumber("right target yaw", baseVisionTarget.left.yaw);
    // 	SmartDashboard.putNumber("right target area", baseVisionTarget.left.area);
    // 	SmartDashboard.putNumber("yaw sum", yawSum);
    // 	SmartDashboard.putNumber("area sum", areaSum);
    // 	boolean leftSaw = false, rightSaw = false;
    // 	// TODO check id and such
    // 	double yawErr = 0, areaErr = 0;
    // 	if(vision.leftView.hasTargets() && vision.leftView.getBestTarget() != null) {
    // 		leftSaw = true;
    // 		SmartDashboard.putNumber("left yaw", vision.leftView.getBestTarget().getYaw());
    // 		SmartDashboard.putNumber("left area", vision.leftView.getBestTarget().getArea());
    // 		yawErr += vision.leftView.getBestTarget().getYaw()-baseVisionTarget.left.yaw;

    // 		yawErr /= Math.sqrt(vision.leftView.getBestTarget().getArea());
    // 		// areaErr += Math.sqrt(baseVisionTarget.left.area)-Math.sqrt(vision.leftView.getBestTarget().getArea());
    // 		areaErr += baseVisionTarget.left.area-vision.leftView.getBestTarget().getArea();
    // 	}
    // 	if(vision.rightView.hasTargets() && vision.rightView.getBestTarget() != null) {
    // 		rightSaw = true;
    // 		SmartDashboard.putNumber("right yaw", vision.rightView.getBestTarget().getYaw());
    // 		SmartDashboard.putNumber("right area", vision.rightView.getBestTarget().getArea());
    // 		yawErr += vision.rightView.getBestTarget().getYaw()-baseVisionTarget.right.yaw;
    // 		// areaErr += Math.sqrt(baseVisionTarget.right.area)-Math.sqrt(vision.rightView.getBestTarget().getArea());
    // 	}
    // 	double mod = leftSaw && rightSaw ? 0.5 : 1;
    // 	if(leftSaw || rightSaw) {
    // 		if(Math.signum(yawErr) != Math.signum(yawSum)) {
    // 			yawSum = 0;
    // 		}
    // 		if(Math.abs(yawErr) < BVYIT) {
    // 			yawSum += yawErr;
    // 		}
    // 		yawSum = Utility.clamp(yawSum, BVIS_YAW_ICAP);
    // 		if(Math.abs(areaErr) < BVIS_AREA_INT_THRESH) {
    // 			areaSum += areaErr;
    // 		}
    // 		areaSum = Utility.clamp(areaSum, BVIS_AREA_ICAP);
    // 		double dh = Utility.fixang(visionHeading-heading());
    // 		drive(
    // 			Utility.clamp(mod*(BVIS_YAW_KP*yawErr+BVIS_YAW_KI*yawSum), BVYC),
    // 			Utility.clamp(BVIS_AREA_KP*areaErr+BVIS_AREA_KI*areaSum, BVAC),
    // 		dh, false);
    // 		// 0, false);
    // 	}
    // 	else stop();
    // }

    // double BVIS_PITCH_KP = 0.1, BVIS_PITCH_KI = 0.00012, BVIS_PITCH_ICAP = 0.1/BVIS_PITCH_KI;
    // double pitchSum;

    // public double pitchFix(double p) {
    // 	return Math.tan(Math.toRadians(p+Math.PI/18));
    // }

    // public void drivetoBasePitchVision() {
    // 	SmartDashboard.putNumber("left target yaw", basePitchVisionTarget.left.yaw);
    // 	SmartDashboard.putNumber("left target pitch", basePitchVisionTarget.left.pitch);
    // 	SmartDashboard.putNumber("right target yaw", basePitchVisionTarget.right.yaw);
    // 	SmartDashboard.putNumber("right target pitch", basePitchVisionTarget.right.pitch);
    // 	boolean leftSaw = false, rightSaw = false;
    // 	// TODO check id and such
    // 	double yawErr = 0, pitchErr = 0;
    // 	if(vision.leftView.hasTargets() && vision.leftView.getBestTarget() != null) {
    // 		leftSaw = true;
    // 		SmartDashboard.putNumber("left yaw", vision.leftView.getBestTarget().getYaw());
    // 		SmartDashboard.putNumber("left pitch", vision.leftView.getBestTarget().getPitch());
    // 		yawErr += vision.leftView.getBestTarget().getYaw()-basePitchVisionTarget.left.yaw;
    // 		pitchErr +=
    // 			// Math.tan(Math.toRadians(basePitchVisionTarget.left.pitch)+Math.PI/18)
    // 			// -Math.tan(Math.toRadians(vision.leftView.getBestTarget().getPitch())+Math.PI/18);
    // 			pitchFix(basePitchVisionTarget.left.pitch)
    // 			-pitchFix(vision.leftView.getBestTarget().getPitch());
    // 	}
    // 	if(vision.rightView.hasTargets() && vision.rightView.getBestTarget() != null) {
    // 		rightSaw = true;
    // 		SmartDashboard.putNumber("right yaw", vision.rightView.getBestTarget().getYaw());
    // 		SmartDashboard.putNumber("right pitch", vision.rightView.getBestTarget().getPitch());
    // 		yawErr += vision.rightView.getBestTarget().getYaw()-basePitchVisionTarget.right.yaw;
    // 		pitchErr +=
    // 			// Math.tan(Math.toRadians(basePitchVisionTarget.right.pitch)+Math.PI/18)
    // 			// -Math.tan(Math.toRadians(vision.rightView.getBestTarget().getPitch())+Math.PI/18);
    // 			pitchFix(basePitchVisionTarget.right.pitch)
    // 			-pitchFix(vision.rightView.getBestTarget().getPitch());
    // 	}
    // 	double mod = leftSaw && rightSaw ? 0.5 : 1;
    // 	if(leftSaw || rightSaw) {
    // 		yawSum += yawErr;
    // 		yawSum = Utility.clamp(yawSum, BVIS_YAW_ICAP);
    // 		pitchSum += pitchErr;
    // 		pitchSum = Utility.clamp(pitchSum, BVIS_PITCH_ICAP);
    // 		double dh = Utility.fixang(visionHeading-heading());
    // 		drive(
    // 			mod*(BVIS_YAW_KP*yawErr+BVIS_YAW_KI*yawSum),
    // 			mod*(BVIS_PITCH_KP*pitchErr+BVIS_PITCH_KI*pitchSum),
    // 		dh, false);
    // 	}

    // 	SmartDashboard.putNumber("pitch err", pitchErr);
    // 	SmartDashboard.putNumber("pitch sum", pitchSum);
    // 	SmartDashboard.putNumber("pitch pow", mod*(BVIS_PITCH_KP*pitchErr+BVIS_PITCH_KI*pitchSum));
    // 	// PhotonUtils.calculateDistanceToTargetMeters(0.09, 0.13, Math)
    // 	// else stop();
    // }

    double TR_THRESH = 0.05, ROT_THRESH = Math.PI/8;
    double VIS_THRESH = 0.2;
    double BVIS_YAW_THRESH = 1, BVIS_AREA_THRESH = 0.1, BVIS_PITCH_THRESH = 2;
    public boolean ready() {
        switch(targetMode) {
            case Odometry:
                Transform2d err = target.minus(pose());
                return
                    err.getTranslation().getNorm() < TR_THRESH &&
                    err.getRotation().getRadians() < ROT_THRESH;
            case Vision:
                // return Math.abs(visionTarget-vision.getX())<VIS_THRESH;
                return true;
            case BaseVision:
                return true;
                    // vision.leftView.getBestTarget() == null ? false :
                    // Math.abs(baseVisionTarget.left.yaw-vision.leftView.getBestTarget().getYaw())<BVIS_YAW_THRESH &&
                    // Math.abs(baseVisionTarget.left.area-vision.leftView.getBestTarget().getArea())<BVIS_AREA_THRESH;
            case BasePitchVision:
                return true;
                    // vision.leftView.getBestTarget() == null ? false :
                    // Math.abs(basePitchVisionTarget.left.yaw-vision.leftView.getBestTarget().getYaw())<BVIS_YAW_THRESH &&
                    // Math.abs(basePitchVisionTarget.left.pitch-vision.leftView.getBestTarget().getPitch())<BVIS_PITCH_THRESH;
            default: return true;
        }
    }

    @Override
    public void periodic() {odo.update(heading2d(), positions());}
    public Pose2d pose() {return odo.getPoseMeters();}
    public void resetOdo() {resetOdo(new Pose2d());}
    public void resetOdo(Pose2d pose) {odo.resetPosition(heading2d(), positions(), pose);}

    public void stop() {drive(0, 0, 0, false);}
    public void resetEncoders() {for(SwerveModule wheel : wheels) wheel.resetEncoders();}
    public void resetGyro() {gyro.reset();}
    public double heading() {return gyro.getGyroAngleZ() * Math.PI/180;}
    public Rotation2d heading2d() {return new Rotation2d(heading());}
    public double turnRate() {return gyro.getRate();}
    public SwerveModulePosition[] positions() {
        return new SwerveModulePosition[] {
            frontLeft.getPosition(),
            backLeft.getPosition(),
            frontRight.getPosition(),
            backRight.getPosition()
        };
    }

}
