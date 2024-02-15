// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * The Constants class provides a csonvenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final Mode currentMode = Mode.SIM;

  //shooter, indexer, intake measured in motor output, pivot measured in radians
  public record Setpoint(double topShooterMotor, double bottomShooterMotor, double pivot, double indexer, double intake) {}

  public class StateConstants {
    public static final Setpoint stow = new Setpoint(0,0, 0.52, 0, 0);
    public static final Setpoint groundIntake = new Setpoint(0,0, 1.22, -0.5, -1);
    public static final Setpoint sourceIntake = new Setpoint(-0.6,0.6, 0.96, -0.5, 0);
    public static final Setpoint scoreAmp = new Setpoint(1,-1, 1.64, 0, 0);
    public static final Setpoint scoreSpeaker = new Setpoint(1,-1, 0, 0, 0);
  }

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }

  public static class OIConstants {
    public static final double kDriveDeadband = 0.1;

    public static final CommandXboxController driverController = new CommandXboxController(0);
    public static final CommandXboxController operatorController = new CommandXboxController(1);
  }

  public static class PathFindingConstants {
    public static final Pose2d blueAmp = new Pose2d(7.5,4.5, new Rotation2d());
    public static final Pose2d redAmp = new Pose2d(7.5, 4.5, new Rotation2d());
  }

  public static class AutoConstants {
    public static final double closePivotSetpoint = 0 //tune later
  }
}
