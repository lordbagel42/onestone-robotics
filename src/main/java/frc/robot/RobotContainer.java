// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.LauncherConstants.kLaunchFeederSpeed;
import static frc.robot.Constants.LauncherConstants.kLauncherSpeed;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.LauncherConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.LaunchNote;
import frc.robot.commands.PrepareLaunch;
//import frc.robot.subsystems.PWMDrivetrain;
//import frc.robot.subsystems.PWMLauncher;

import frc.robot.subsystems.CANDrivetrain;
import frc.robot.subsystems.CANLauncher;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems are defined here.
  //private final PWMDrivetrain m_drivetrain = new PWMDrivetrain();
   final CANDrivetrain m_drivetrain = new CANDrivetrain();
  //private final PWMLauncher m_launcher = new PWMLauncher();
  private final CANLauncher m_launcher = new CANLauncher();

  /*The gamepad provided in the KOP shows up like an XBox controller if the mode switch is set to X mode using the
   * switch on the top.*/
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final CommandXboxController m_operatorController =
      new CommandXboxController(OperatorConstants.kOperatorControllerPort);
  private final XboxController xDriver = new XboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be accessed via the
   * named factory methods in the Command* classes in edu.wpi.first.wpilibj2.command.button (shown
   * below) or via the Trigger constructor for arbitary conditions
   */
  private void configureBindings() {
    //launcher
    /*new JoystickButton(m_operatorController, 3)
      .whileTrue(
        new PrepareLaunch(m_launcher, 0)
          .andThen(new LaunchNote(m_launcher, 0))

      );

    new JoystickButton(m_operatorController, 2)
     .whileTrue(
        new PrepareLaunch(m_launcher, 1)
          .andThen(new LaunchNote(m_launcher,1))
     );
    
    new JoystickButton(m_operatorController, 1)//.onTrue(new LaunchNote(m_launcher, 1)).onFalse(new LaunchNote(m_launcher, 0));
      .whileTrue(
        new PrepareLaunch(m_launcher, -1)
          .andThen(new LaunchNote(m_launcher, -1))
      );
    */
      /*Create an inline sequence to run when the operator presses and holds the A (green) button. Run the PrepareLaunch
     * command for 1 seconds and then run the LaunchNote command */
    
      m_driverController.a().whileTrue(
            new PrepareLaunch(m_launcher, -1)
                .withTimeout(2)
                .andThen(new LaunchNote(m_launcher, -1))
                .handleInterrupt(() -> m_launcher.stop()));
      // new JoystickButton(xDriver, 1).onTrue(new PrepareLaunch(m_launcher, -1)
      //         .withTimeout(2)
      //         .andThen(new LaunchNote(m_launcher, -1))
      //         .handleInterrupt(() -> m_launcher.stop())).onFalse(m_launcher.stop());

     



    // Set up a binding to run the intake command while the operator is pressing and holding the
    // left Bumper
    // new JoystickButton(m_operatorController, 5)
    // .whileTrue(m_launcher.getIntakeCommand()); 

    m_driverController.leftBumper().whileTrue(m_launcher.getIntakeCommand());
    // new JoystickButton(xDriver, 5).onTrue(m_launcher.getIntakeCommand());

    // Set the default command for the drivetrain to drive using the joysticks
   /*  m_drivetrain.setDefaultCommand(
        new RunCommand(
            () ->
                m_drivetrain.tankDrive(
                    -m_driverController.getLeftY(), -m_driverController.getRightY()),
            m_drivetrain));
*/
  m_drivetrain.setDefaultCommand(
    new RunCommand(
      () ->
         m_drivetrain.arcadeDrive(
              -m_driverController.getLeftY(), -m_driverController.getRightX()),
        m_drivetrain));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_drivetrain);
  }
}
