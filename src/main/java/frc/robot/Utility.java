package frc.robot;

public class Utility {
    public final static double TAU = 2*Math.PI;

    public static double ticks2Meters(double ticks) {
        return ticks / Constants.gearRatio * Constants.wheelDiam * Math.PI;
    }

    public static double ticks2Rad(double ticks) {
        return fixang(ticks*2*Math.PI);
    }

    public static double fixang(double a) {
        return ((a%TAU+Math.PI)%TAU)-Math.PI;
    }

    public static double clamp(double v, double min, double max) {
        return Math.min(max, Math.max(v, min));
    }

    public static double clamp(double v, double max) {
        return Math.min(max, Math.max(v, -max));
    }

    public static double sgnsqr(double a) {
        return Math.signum(a)*a*a;
    }

    public static double toRad(double a) {
        return a*Math.PI/180;
    }
}
