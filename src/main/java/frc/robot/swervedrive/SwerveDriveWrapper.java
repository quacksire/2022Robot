package frc.robot.swervedrive;

import edu.wpi.first.wpilibj.DriverStation;

import frc.robot.*;

public class SwerveDriveWrapper extends Wrapper{
   
    public SwerveDrive swervedrive;

    public SwerveDriveWrapper () {
        try{
            swervedrive = new SwerveDrive() ;
            
        }
        catch (RuntimeException ex ){
            DriverStation.reportError("Error instantiating swervedrive:  " + ex.getMessage(), true);
        }

    }

    public void drive(double x, double y, double r) {
        swervedrive.drive(x, y, r);
    }

    public void setEncoders() {
        swervedrive.setEncoders();
    }

}
