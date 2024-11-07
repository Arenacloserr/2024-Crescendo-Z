// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

// import frc.robot.subsystems.Shooter.*;

import frc.robot.subsystems.leds.LEDs;

import java.util.function.DoubleSupplier;
import edu.wpi.first.wpilibj2.command.Command;

public class RevvingCommand extends Command {
    LEDs leds = LEDs.getInstance();
    double RPM;
    double MAX;

    public RevvingCommand(DoubleSupplier RPM, DoubleSupplier MAX) {
        this.RPM = RPM.getAsDouble();
        this.MAX = MAX.getAsDouble();

        addRequirements(leds);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        leds.setTopColorHSV(0, 0, 0);
        leds.setOutput();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Hue is 0-180
        for (int i = 0; i < leds.getLength(); i++) {
            if (this.RPM >= (this.MAX*(((double) i*(100/((double) leds.getLength()/1)))/100))) { // edit length if needed
                leds.setHSVIndex(i, 0, 0, 0);
            }
            else {
                break;
            }
        }

        leds.setOutput();
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}