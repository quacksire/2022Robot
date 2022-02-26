package frc.robot.swervedrive;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import frc.robot.RobotConstants;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI;
import com.kauailabs.navx.frc.AHRS;

public class SwerveDrive {
    ChassisSpeeds speeds;
    Wheel frontLeft;
    Wheel frontRight;
    Wheel backLeft;
    Wheel backRight;
    SwerveDriveKinematics kinematics;
    SwerveDriveOdometry odometry;
    Pose2d pose;
    AHRS ahrs;

    public SwerveDrive() {
        //frontLeft = new Wheel(RobotConstants.frontLeftAngleID, RobotConstants.frontLeftSpeedID, RobotConstants.frontLeftAbsoluteEncoderID, "Front Left (1)");
        //frontRight = new Wheel(RobotConstants.frontRightAngleID, RobotConstants.frontRightSpeedID, RobotConstants.frontRightAbsoluteEncoderID, "Front Right (2)");
        //backLeft = new Wheel(RobotConstants.backLeftAngleID, RobotConstants.backLeftSpeedID, RobotConstants.backLeftAbsoluteEncoderID, "Back Left (3)");
        backRight = new Wheel(RobotConstants.backRightAngleID, RobotConstants.backRightSpeedID, RobotConstants.backRightAbsoluteEncoderID, "Back Right (4)");

        // Locations for the swerve drive modules relative to the robot center.
        Translation2d frontLeftLocation = new Translation2d(RobotConstants.frontLeftXMeters, RobotConstants.frontLeftYMeters);
        Translation2d frontRightLocation = new Translation2d(RobotConstants.frontRightXMeters, RobotConstants.frontLeftYMeters);
        Translation2d backLeftLocation = new Translation2d(RobotConstants.backLeftXMeters, RobotConstants.backLeftYMeters);
        Translation2d backRightLocation = new Translation2d(RobotConstants.backRightXMeters, RobotConstants.backLeftYMeters);

        // Creating my kinematics object using the module locations
        kinematics = new SwerveDriveKinematics(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
        odometry = new SwerveDriveOdometry(kinematics, new Rotation2d(), new Pose2d(5.0, 13.5, new Rotation2d()));

        speeds = new ChassisSpeeds();

        ahrs = new AHRS(SPI.Port.kMXP);
    }

    public void drive(double x, double y, double r) {
        // speeds.vxMetersPerSecond = x;
        // speeds.vyMetersPerSecond = y;
        // speeds.omegaRadiansPerSecond = r;

        speeds = ChassisSpeeds.fromFieldRelativeSpeeds(x, y, r, Rotation2d.fromDegrees(ahrs.getYaw()));

        SmartDashboard.putNumber("X", x);
        SmartDashboard.putNumber("Y", y);
        SmartDashboard.putNumber("R", r);
        SmartDashboard.putNumber("Robot Angle", ahrs.getYaw());

        SwerveModuleState[] moduleStates = kinematics.toSwerveModuleStates(speeds);

        SmartDashboard.putNumber("SpeedMotor", moduleStates[1].speedMetersPerSecond);
        SmartDashboard.putNumber("AngleMotor", moduleStates[1].angle.getDegrees());

        //Set to angle that we get from the NavX
        //double angle = 0;

        //Rotation2d gyroAngle = Rotation2d.fromDegrees(angle);

        // Update the pose
        //pose = odometry.update(gyroAngle, moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3]);

        //frontLeft.drive(moduleStates[0]);
        //frontRight.drive(moduleStates[1]);
        //backLeft.drive(moduleStates[2]);
        backRight.drive(moduleStates[1]);
    }

    public void setEncoders() {
        //frontLeft.zeroEncoders();
        //frontRight.zeroEncoders();
        //backLeft.zeroEncoders();
        backRight.setEncoders();
    }
}