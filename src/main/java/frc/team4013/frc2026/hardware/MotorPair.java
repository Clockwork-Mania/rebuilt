package frc.team4013.frc2026.hardware;


public class MotorPair extends Motor {
    public Motor other;

    public MotorPair(int id0, int id1) {
        super(id0);
        other = new Motor(id1);
    }

    public MotorPair(int id0, int id1, String bus) {
        super(id0, bus);
        other = new Motor(id1, bus);
    }

    public void setPower(double speed) {
        super.setPower(speed);
        other.setPower(speed);
    }

    public void setDir(Direction dir0, Direction dir1) {
        super.setDir(dir0);
        other.setDir(dir1);
    }

    public void stop() {
        set(0);
    }

    public void goTo(double position, double kp) {
        super.goTo(position, kp);
        other.goTo(position, kp);
    }

    public void setEnc(Motor.EncoderType encType, int id) {
        super.setEnc(encType, id);
    }

    public void setEnc(Motor.EncoderType encType, int id, String bus) {
        super.setEnc(encType, id, bus);
    }

    public double getPos() {
        return super.getPos();
    }
}
