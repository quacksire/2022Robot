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
        frontLeft = new Wheel(RobotConstants.frontLeftAngleID, RobotConstants.frontLeftSpeedID, RobotConstants.frontLeftAbsoluteEncoderID, "Front Left (1)");
        frontRight = new Wheel(RobotConstants.frontRightAngleID, RobotConstants.frontRightSpeedID, RobotConstants.frontRightAbsoluteEncoderID, "Front Right (2)");
        backLeft = new Wheel(RobotConstants.backLeftAngleID, RobotConstants.backLeftSpeedID, RobotConstants.backLeftAbsoluteEncoderID, "Back Left (3)");
        backRight = new Wheel(RobotConstants.backRightAngleID, RobotConstants.backRightSpeedID, RobotConstants.backRightAbsoluteEncoderID, "Back Right (4)");

        // Locations for the swerve drive modules relative to the robot center.
        Translation2d frontLeftLocation = new Translation2d(RobotConstants.frontLeftXMeters, RobotConstants.frontLeftYMeters);
        Translation2d frontRightLocation = new Translation2d(RobotConstants.frontRightXMeters, RobotConstants.frontRightYMeters);
        Translation2d backLeftLocation = new Translation2d(RobotConstants.backLeftXMeters, RobotConstants.backLeftYMeters);
        Translation2d backRightLocation = new Translation2d(RobotConstants.backRightXMeters, RobotConstants.backRightYMeters);

        // Creating my kinematics object using the module locations
        kinematics = new SwerveDriveKinematics(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
        odometry = new SwerveDriveOdometry(kinematics, new Rotation2d(), new Pose2d(5.0, 13.5, new Rotation2d()));

        speeds = new ChassisSpeeds();

        ahrs = new AHRS(SPI.Port.kMXP);
    }

    public void drive(double x, double y, double r) {
        speeds.vxMetersPerSecond = x;
        speeds.vyMetersPerSecond = y;
        speeds.omegaRadiansPerSecond = r;

        speeds = ChassisSpeeds.fromFieldRelativeSpeeds(x, y, r, Rotation2d.fromDegrees(-ahrs.getYaw()));
        SmartDashboard.putNumber("angle from navx", ahrs.getYaw());

        SmartDashboard.putNumber("X", x);
        SmartDashboard.putNumber("Y", y);
        SmartDashboard.putNumber("R", r);
        SmartDashboard.putNumber("Robot Angle", ahrs.getYaw());

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);

        SmartDashboard.putNumber("SpeedMotorFront", states[1].speedMetersPerSecond);
        SmartDashboard.putNumber("AngleMotorFront", states[1].angle.getDegrees());

        SmartDashboard.putNumber("SpeedMotorBack", states[1].speedMetersPerSecond);
        SmartDashboard.putNumber("AngleMotorBack", states[1].angle.getDegrees());

        //Set to angle that we get from the NavX
        //double angle = 0;

        //Rotation2d gyroAngle = Rotation2d.fromDegrees(angle);

        // Update the pose
        //pose = odometry.update(gyroAngle, moduleStates[0], moduleStates[1], moduleStates[2], moduleStates[3]);

        frontLeft.drive(states[2].speedMetersPerSecond, states[2].angle.getDegrees());
        frontRight.drive(states[0].speedMetersPerSecond, states[0].angle.getDegrees());
        backLeft.drive(states[3].speedMetersPerSecond, states[3].angle.getDegrees());
        backRight.drive(states[1].speedMetersPerSecond, states[1].angle.getDegrees());
    }

    public double turnToAngle(double goalAngle) {
        double error = 1;
        double currentAngle = ahrs.getYaw();

        double diff = (currentAngle - goalAngle) % 360;

        if (Math.abs(diff) > 180) {
          diff = diff - 360*Math.signum(diff); // add or subtract 360 so the difference is always smaller than 180
        }

        double realGoalAngle = (currentAngle - diff);

        if (Math.abs(currentAngle - realGoalAngle) > error) {
            if (currentAngle > realGoalAngle) {
                return -.5;
            }
            else {
                return .5;
            }
        }
        return 0;

    }

    public void setEncoders2() {
        frontLeft.setEncoders(SmartDashboard.getNumber("front left offset", 0));
        frontRight.setEncoders(SmartDashboard.getNumber("front right offset", 0));
        backLeft.setEncoders(SmartDashboard.getNumber("back left offset", 0));
        backRight.setEncoders(SmartDashboard.getNumber("back right offset", 0));
    }

    public void setEncoders() {
        frontLeft.setEncoders(RobotConstants.frontLeftAngleOffset);
        frontRight.setEncoders(RobotConstants.frontRightAngleOffset);
        backLeft.setEncoders(RobotConstants.backLeftAngleOffset);
        backRight.setEncoders(RobotConstants.backRightAngleOffset);
    }

    public void readAbsoluteEncoder() {
        SmartDashboard.putNumber("front left absolute", frontLeft.getAbsoluteEncoderValue());
        SmartDashboard.putNumber("front right absolute", frontRight.getAbsoluteEncoderValue());
        SmartDashboard.putNumber("back left absolute", backLeft.getAbsoluteEncoderValue());
        SmartDashboard.putNumber("back right absolute", frontRight.getAbsoluteEncoderValue());

    }
    public void resetNavX () {
        ahrs.reset();
    }
}