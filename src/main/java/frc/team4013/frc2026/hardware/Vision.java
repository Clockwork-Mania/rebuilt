// package frc.robot.hardware;
package frc.team4013.frc2026.hardware;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team4013.frc2026.LimelightHelpers;
// import edu.wpi.first.net.PortForwarder;
// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
// import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class Vision{
    double tx = 0;

    public void updateTX(){
        SmartDashboard.putNumber("tx: ", tx);
        tx = LimelightHelpers.getTX("b");
    }
}
// public class Vision {
//     public class CamTarget {
//         public double yaw, area;
//         public int id;
//         public CamTarget(int id, double yaw, double area) {
//             this.yaw = yaw;
//             this.area = area;
//         }
//     }

//     public class CamPitchTarget {
//         public double yaw, pitch;
//         public int id;
//         public CamPitchTarget(int id, double yaw, double pitch) {
//             this.yaw = yaw;
//             this.pitch = pitch;
//         }
//     }

//     public class BaseTarget {
//         public CamTarget left, right;
//         public BaseTarget(CamTarget left, CamTarget right) {
//             this.left = left;
//             this.right = right;
//         }
//     }

//     public class BasePitchTarget {
//         public CamPitchTarget left, right;
//         public BasePitchTarget(CamPitchTarget left, CamPitchTarget right) {
//             this.left = left;
//             this.right = right;
//         }
//     }

//     public BaseTarget REEF_LEFT = new BaseTarget(
//         new CamTarget(10, 0, 0),
//         new CamTarget(10, 0, 0)
//     );

//     // public BaseTarget REEF_RIGHT = new BaseTarget(
//     //     new CamTarget(10, 8.23, 6.17),
//     //     new CamTarget(10, 0, 0)
//     // );
//     public BaseTarget REEF_RIGHT = new BaseTarget(
//         new CamTarget(10, 8.30, 2.77),
//         new CamTarget(10, -19.92, 1.96)
//     );

//     public BasePitchTarget P_REEF_RIGHT = new BasePitchTarget(
//         new CamPitchTarget(10, 3.86, -5.11),
//         new CamPitchTarget(10, -20.167, -5.85)
//     );

//     // public PhotonCamera leftCam = new PhotonCamera("arduzz2"),
//     //                     rightCam = new PhotonCamera("arduzz1");

//     // public PhotonPipelineResult rightView, leftView;

//     NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
//     NetworkTableEntry pipeline = table.getEntry("pipeline");
//     NetworkTableEntry tx = table.getEntry("tx");
//     NetworkTableEntry ty = table.getEntry("ty");
//     NetworkTableEntry ta = table.getEntry("ta");

//     // pipeline constants
//     public static final int DETECT_10 = 0;

//     public Vision() {
//         PortForwarder.add(5800, "photonvision.local", 5800);
//     }

//     public void setPipeline(int n) {
//         pipeline.setNumber(n);
//     }

//     public void update() {
//         rightView = rightCam.getLatestResult();
//         leftView = leftCam.getLatestResult();

//         SmartDashboard.putNumber("LimelightX", getX());
//         SmartDashboard.putNumber("LimelightY", getY());
//         SmartDashboard.putNumber("LimelightArea", getA());
//     }

//     public double getX() {return tx.getDouble(0);}
//     public double getY() {return ty.getDouble(0);}
//     public double getA() {return ta.getDouble(0);}
//     public boolean foundTag() {return ta.getDouble(-1) != -1;}
// }
