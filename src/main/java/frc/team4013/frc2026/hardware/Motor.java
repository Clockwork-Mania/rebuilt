package frc.team4013.frc2026.hardware;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class Motor extends TalonFX {
    public static enum Direction {CW, CCW};
    public Direction dir = Direction.CW;
    public Direction encDir = Direction.CW;

    public static enum EncoderType {
        NONE,
        CAN,
        DUTY_CYCLE,
        WRAPPING_DC
    };
    EncoderType encType = EncoderType.NONE;
    public CANcoder canEnc = null;
    public DutyCycleEncoder dcEnc = null;
    public WrappingDutyCycleEncoder wrapEnc = null;

    public double kp = 0;

    public Motor(int id) {
        super(id);
    }

    public Motor(int id, CANBus bus) {
        super(id, bus);
    }

    public Motor(int id, String bus) {
        super(id, bus);
    }

    public void setEnc(EncoderType encType, int id) {
        switch(encType) {
            case CAN:
                setCanEnc(id);
                break;
            case DUTY_CYCLE:
                setDcEnc(id);
                break;
            case WRAPPING_DC:
                setWrappingDcEnc(id);
                break;
            default: break;
        }
    }

    public void setEnc(EncoderType encType, int id, String bus) {
        setCanEnc(id, bus);
    }

    // @Override
    public void setPower(double speed) {
        super.set(dir == Direction.CW ? speed : -speed);
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void setEncDir(Direction dir) {
        this.encDir = dir;
    }

    public void stop() {
        setPower(0);
    }

    void setCanEnc(int id) {setCanEnc(id, "rio");}

    void setCanEnc(int id, String bus) {
        canEnc = new CANcoder(id, bus);
        encType = EncoderType.CAN;
    }

    void setDcEnc(int id) {
        dcEnc = new DutyCycleEncoder(id);
        encType = EncoderType.DUTY_CYCLE;
    }

    void setWrappingDcEnc(int id) {
        wrapEnc = new WrappingDutyCycleEncoder(id);
        encType = EncoderType.WRAPPING_DC;
    }

    public double getPos() {
        double rev = encDir == Direction.CW ? 1 : -1;
        switch(encType) {
            case CAN:
                return rev*canEnc.getPosition().getValueAsDouble();
            case DUTY_CYCLE:
                return encDir == Direction.CW ?
                    dcEnc.get() :
                    1-dcEnc.get();
            case WRAPPING_DC:
                return encDir == Direction.CW ?
                    wrapEnc.get() :
                    1-wrapEnc.get();
            default:
                return rev*getPosition().getValueAsDouble();
        }
    }

    double target;
    public void setTarget(double target) {
        this.target = target;
    }

    public void goToTarget() {
        goTo(target);
    }

    public void goTo(double target) {
        goTo(target, kp);
    }

    public void goTo(double target, double kp) {
        double error = target - getPos();
        setPower(kp * error);
    }

    public void periodic() {
        if(encType == EncoderType.WRAPPING_DC) {
            wrapEnc.periodic();
        }
    }
}
