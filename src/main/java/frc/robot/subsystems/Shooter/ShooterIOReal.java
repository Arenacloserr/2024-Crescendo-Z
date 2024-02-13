package frc.robot.subsystems.Shooter;

import com.ctre.phoenix6.hardware.TalonFX;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;

public class ShooterIOReal implements ShooterIO {
    
    //private CANSparkMax rightShooterMotor = new CANSparkMax(ShooterConstants.rightCanID,MotorType.kBrushless);
    private TalonFX topShooterMotor = new TalonFX(ShooterConstants.rightCanID);
    private TalonFX bottomShooterMotor = new TalonFX(ShooterConstants.leftCanID);

    private Orchestra m_orchestra = new Orchestra();

    private TalonFXConfiguration talonFXConfig = new TalonFXConfiguration();
    final VelocityVoltage m_request = new VelocityVoltage(0).withSlot(0);
    private boolean isEnabled;
    private boolean hasPlayed;

    public ShooterIOReal(){
        talonFXConfig.Slot0.kP = 0.05;
        talonFXConfig.Slot0.kI = 0;
        talonFXConfig.Slot0.kD = 0;
        talonFXConfig.Slot0.kS = 0;
        talonFXConfig.Slot0.kV = 0;

        talonFXConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        talonFXConfig.CurrentLimits.StatorCurrentLimit = 30;
        talonFXConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        talonFXConfig.CurrentLimits.SupplyCurrentLimit = 30;    

        talonFXConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // talonFXConfig.MotionMagic.MotionMagicAcceleration = 100;
        // talonFXConfig.MotionMagic.MotionMagicCruiseVelocity = 10;

        talonFXConfig.Audio.BeepOnBoot = true;

        bottomShooterMotor.clearStickyFaults();
        topShooterMotor.clearStickyFaults();

        StatusCode response = bottomShooterMotor.getConfigurator().apply(talonFXConfig);
        if (!response.isOK()) {
            System.out.println(
                    "Talon ID "
                            + bottomShooterMotor.getDeviceID()
                            + " failed config with error "
                            + response.toString());
        }

        response = topShooterMotor.getConfigurator().apply(talonFXConfig);
        if (!response.isOK()) {
            System.out.println(
                    "Talon ID "
                            + topShooterMotor.getDeviceID()
                            + " failed config with error "
                            + response.toString());
        }

        hasPlayed = false;

        m_orchestra.addInstrument(bottomShooterMotor);
        m_orchestra.addInstrument(topShooterMotor);

        m_orchestra.loadMusic("chirpWindowsXPFX.chrp");
    }

    @Override
    public void runShooterMotors(double speed) {
        // set velocity to certain rps, add 0.5 V to overcome gravity
        //leftShooterMotor.setControl(m_request.withVelocity(speed).withFeedForward(0.2));

        bottomShooterMotor.set(MathUtil.clamp(speed, -1, 1));
        topShooterMotor.set(MathUtil.clamp(speed, -1, 1));
    }

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.shooterMotorAppliedVolts = bottomShooterMotor.getMotorVoltage().getValueAsDouble();
        inputs.shooterMotorVelocityRPM = bottomShooterMotor.getVelocity().getValueAsDouble() * 60.0; // RPS to RPM
        inputs.shooterMotorAppliedVolts = topShooterMotor.getMotorVoltage().getValueAsDouble();
        inputs.shooterMotorVelocityRPM = topShooterMotor.getVelocity().getValueAsDouble() * 60.0; // RPS to RPM

        isEnabled = DriverStation.isEnabled();

        if (isEnabled && !hasPlayed) {
            m_orchestra.play();
            hasPlayed = true;
        }
        else {
            m_orchestra.stop();
        }
    }
}