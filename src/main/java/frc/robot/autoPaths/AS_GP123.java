package frc.robot.autoPaths;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.AutoCommands;

public class AS_GP123 {
    public Command load(AutoCommands autoCommands) {
        return Commands.sequence(
            autoCommands.initialize(1),
            autoCommands.score(),
            autoCommands.followTrajectory("AS GP123 P1"),
            //change pivot angles later
            autoCommands.SetPivotAngle(Constants.autoConstants.closePivotSetpoint), //same angles for everything
            autoCommands.score(),
            autoCommands.followTrajectory("AS GP123 P2"),
            autoCommands.SetPivotAngle(Constants.autoConstants.closePivotSetpoint),
            autoCommands.score(),
            autoCommands.followTrajectory("AS GP123 P3"),
            autoCommands.SetPivotAngle(Constants.autoConstants.closePivotSetpoint),
            autoCommands.score()
        );
    }
}

