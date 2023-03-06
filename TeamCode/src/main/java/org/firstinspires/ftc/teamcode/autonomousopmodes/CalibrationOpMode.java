package org.firstinspires.ftc.teamcode.autonomousopmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumHelper;
import org.firstinspires.ftc.teamcode.hardware.MecanumMotorsLinearSlideGrabberHardwareMap;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Mecanum Calibration", group="tests")
public class CalibrationOpMode extends LinearOpMode {

    private MecanumMotorsLinearSlideGrabberHardwareMap teamHardwareMap;

    @Override
    public void runOpMode() {
        teamHardwareMap = new MecanumMotorsLinearSlideGrabberHardwareMap(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        teamHardwareMap.runTime.reset();

        MecanumHelper mecanumHelper = new MecanumHelper(teamHardwareMap.frontRightMotor, teamHardwareMap.backRightMotor, teamHardwareMap.backLeftMotor, teamHardwareMap.frontLeftMotor);

        while (opModeIsActive()) {
            // Show the elapsed game time and wheel power.
            mecanumHelper.move(0, 1);

            if (teamHardwareMap.runTime.milliseconds() > 10000) {
                stop();
            }

            telemetry.addData("Status", "Run Time: " + teamHardwareMap.runTime.toString());
            telemetry.addData("FLW", teamHardwareMap.frontLeftMotor.getCurrentPosition());
            telemetry.addData("FRW", teamHardwareMap.frontRightMotor.getCurrentPosition());
            telemetry.addData("BLW", teamHardwareMap.backLeftMotor.getCurrentPosition());
            telemetry.addData("BRW", teamHardwareMap.backRightMotor.getCurrentPosition());
            telemetry.update();

        }
    }
}