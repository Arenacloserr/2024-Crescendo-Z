package frc.robot.subsystems.MAXSwerve;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Rotation2d;

public interface GyroIO {
    @AutoLog
    public static class GyroIOInputs {
        public boolean connected = false;
        public Rotation2d yaw = new Rotation2d();
        public double yawVelocity = 0.0; //degrees per second

        public double[] timestamps = new double[] {};
        public Rotation2d[] odometryYaw = new Rotation2d[] {};
    }

    public default void updateInputs(GyroIOInputs inputs){}
    
    public default void reset( ){}

    public default void addOffset(Rotation2d change) {}
}
