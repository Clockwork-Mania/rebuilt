package frc.robot.hardware;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.sim.CANcoderSimState;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkAbsoluteEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Utility;
import frc.robot.hardware.Motor.Direction;

public class SwerveModule {
    public Motor power, spin;
	public CANcoder enc;
	public CANcoderSimState encSim;
    public double off = 0;

	public SwerveModule(int powId, int spinId, int encId, double off) {
		power = new Motor(powId);
		spin = new Motor(spinId);
		enc = new CANcoder(encId);
		encSim = new CANcoderSimState(enc);
		power.setNeutralMode(NeutralModeValue.Brake);
		power.setDir(Direction.CCW);
		power.setEncDir(Direction.CCW);
		this.off = off;
	}

    public double powerPos() {return Utility.ticks2Meters(power.getPos());}
    public double powerVel() {return Utility.ticks2Meters(power.getVelocity().getValueAsDouble());} // DONT USE
    public double spinPos() {return Utility.ticks2Rad(enc.getPosition().getValueAsDouble()-off);}
    public double spinVel() {return Utility.ticks2Rad(enc.getVelocity().getValueAsDouble());}

	public SwerveModuleState getState() {
		return new SwerveModuleState(
			powerVel(), new Rotation2d(spinPos()));
	}

	public SwerveModulePosition getPosition() {
		return new SwerveModulePosition(
			powerPos(), new Rotation2d(spinPos()));
	}

	static final double spinDeadband = 0.01;
	static final double kpSpin = 0.2, kiSpin = 0, kdSpin = 0;
	double errSum = 0;
	double lastErr = 0;

	public double maxAccel = 0.01;
	public double lastPower = 0; 

	public void pidSpin(double target, double dt, double speed) {
		double err = target - spinPos();
		boolean reversed = false;

		// clip error to (-PI, PI), reversing if necessary
		while(err > Math.PI) {err -= Math.PI; reversed = !reversed;}
		while(err < -Math.PI) {err += Math.PI; reversed = !reversed;}
		
		// flip error if >PI/2 and reverse
		if (Math.abs(err) > Math.PI / 2) {
			err = (err < -Math.PI / 2 ? err + Math.PI: err - Math.PI);
			reversed = !reversed;
		}

		// calculate power using kp, ki, kd
		spin.setPower(-(Math.abs(err) > spinDeadband ?
			kpSpin * err + kiSpin * errSum + kdSpin * ((err-lastErr)/dt)
		:0));

		// update integral and derivative quanitites
		errSum += err;
		lastErr = err;

		// clip and set speed
		if(speed > 0) {
			// speed = Math.min(speed)
			if(speed - lastPower > maxAccel) speed = lastPower + maxAccel;
		}
		else {
			if(speed - lastPower < -maxAccel) speed = lastPower - maxAccel;
		}
		// if(speed - lastPower > maxAccel) speed = lastPower + Math.signum(lastPower) * maxAccel;
		speed = (speed > 1 ? 1 : (speed < -1 ? -1 : speed));
		power.setPower(reversed ? -speed : speed);
		lastPower = speed;

		// spin.setPID(kpSpin, kiSpin, kdSpin);
		// power.setPosition(Utility.ticks2Meters(2));
	}

	public void drive(double x, double y, double r, double theta) {
		double vx = x + r * Math.cos(theta);
		double vy = y + r * Math.sin(theta);
		if(Math.abs(vx)>.01||Math.abs(vy)>.01) {
			pidSpin(Math.atan2(vy, vx), .02, Math.sqrt(vx*vx+vy*vy));
		}
		else stop();
	}

	public void stop() {
		spin.setPower(0);
		power.setPower(0);
	}

	public void resetEncoders() {
        off = spinPos();
	}
}
