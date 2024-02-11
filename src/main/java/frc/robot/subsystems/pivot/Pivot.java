package frc.robot.subsystems.pivot;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.Constants.OIConstants;

public class Pivot extends SubsystemBase {
    private PivotIO io;
    private PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();

    private ProfiledPIDController controller = new ProfiledPIDController(
        PivotConstants.kP, PivotConstants.KI, PivotConstants.kD, 
        PivotConstants.constraints);
    private final ArmFeedforward pivotFeedForward = new ArmFeedforward(
        PivotConstants.kS, PivotConstants.kG, PivotConstants.kV, PivotConstants.kA);
    
    private double goal = 0;

    PivotVisualizer visualizer = new PivotVisualizer(Color.kDarkOrange);

    private static Pivot instance;

    public static Pivot getInstance() {
        return instance;
    }

    public static Pivot initialize(PivotIO io) {
        if (instance == null) {
            instance = new Pivot(io);
        }
        return instance;
    }

    public Pivot(PivotIO pivotIO) {
        io = pivotIO;
        io.updateInputs(inputs);

        setGoal(inputs.pivotPositionRads);
        visualizer.update(360 * inputs.pivotPositionRads);
        controller.setTolerance(Units.degreesToRadians(0.5));

        setDefaultCommand(run(() -> {
            double up = MathUtil.applyDeadband(
                    OIConstants.operatorController.getRightTriggerAxis(), OIConstants.kAxisDeadband);
            double down = MathUtil.applyDeadband(
                    OIConstants.operatorController.getLeftTriggerAxis(), OIConstants.kAxisDeadband);
           
            // double output = 0.15 * (Math.pow(up, 3) - Math.pow(down, 3));
            // io.rotatePivot(output);
            double change = PivotConstants.manualSpeed * MathUtil.applyDeadband(OIConstants.operatorController.getLeftY(), OIConstants.kAxisDeadband);
            // double change = PivotConstants.manualSpeed * (Math.pow(up, 3) - Math.pow(down, 3));
            setGoal(goal + change);
        }));
    }

    public void setGoal(double goal) {
        this.goal = MathUtil.clamp(goal, PivotConstants.lowLimit, PivotConstants.highLimit);
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Pivot", inputs);
        visualizer.update(Units.radiansToDegrees(inputs.pivotPositionRads));
        double springVolts = pivotFeedForward(inputs.pivotPositionRads, inputs.pivotVelocityRadPerSec);
        double PIDVolts = controller.calculate(inputs.pivotPositionRads, goal);
        double FFVolts = 0.0;

        if(Robot.isSimulation()){
            FFVolts = pivotFeedForward.calculate(controller.getSetpoint().position, controller.getSetpoint().velocity);
        } else {
            FFVolts = pivotFeedForward.calculate(controller.getSetpoint().position + PivotConstants.angleOffset, controller.getSetpoint().velocity);
        }
        
        io.setVoltage(PIDVolts + FFVolts);

        Logger.recordOutput("Pivot/Spring Volts", springVolts);
        Logger.recordOutput("Pivot/PID Volts", PIDVolts);
        Logger.recordOutput("Pivot/FF Volts", FFVolts);
        Logger.recordOutput("Pivot/Setpoint Position", controller.getSetpoint().position);
        Logger.recordOutput("Pivot/Setpoint Velocity", controller.getSetpoint().velocity);
        Logger.recordOutput("Pivot/Goal", goal);
    }

    private double torqueFromAngle(double angleRad) {
        double springAngle = Math.atan2(
            PivotConstants.d * Math.sin(angleRad) + PivotConstants.y, 
            -PivotConstants.d * Math.cos(angleRad) + PivotConstants.x);
        double Tg = -PivotConstants.M * PivotConstants.R - PivotConstants.g * Math.cos(angleRad);
        double Ts = PivotConstants.d * PivotConstants.F * Math.sin(springAngle - (Math.PI - angleRad));
        return (-Tg - Ts) / PivotConstants.gearing;
    }

    private double pivotFeedForward(double angle, double velocity) {
        double torque = torqueFromAngle(angle) ;        
        return DCMotor.getNeoVortex(2).getVoltage(torque, velocity);
    }
}
