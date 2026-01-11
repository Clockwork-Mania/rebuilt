package frc.team4013.frc2026;

public final class Constants {
    public static final int encCPR = 2048;
    public static final double gearRatio = 6.12;
    public static final double wheelDiam = 4*.0254;
    public static final double distPerTick = (wheelDiam * Math.PI) / encCPR;

    public static final boolean kIsReplay = true;
}