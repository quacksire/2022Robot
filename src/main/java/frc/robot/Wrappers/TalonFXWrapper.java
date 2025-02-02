package frc.robot.wrappers;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DriverStation;


public class TalonFXWrapper extends Wrapper {
    TalonFX talon;

    public TalonFXWrapper(int port) {
        try{
            talon = new TalonFX(port);
            isInitialized = true;
        }
        catch (RuntimeException ex ) {
            DriverStation.reportError("Error Initiating TalonFX:  " + ex.getMessage(), true);
        }
    }
    public void set(double speed) {
        if (isInitialized) talon.set(ControlMode.PercentOutput, speed);
    }
}