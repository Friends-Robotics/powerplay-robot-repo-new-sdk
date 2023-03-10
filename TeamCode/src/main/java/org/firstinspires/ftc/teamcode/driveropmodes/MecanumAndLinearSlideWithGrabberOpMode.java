package org.firstinspires.ftc.teamcode.driveropmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

@TeleOp(name="Mecanum And Linear Slide With Grabber", group="Linear Opmode")
public class MecanumAndLinearSlideWithGrabberOpMode extends LinearOpMode {

    private MecanumMotorsLinearSlideGrabberHardwareMap teamHardwareMap;

    @Override
    public void runOpMode() {
        teamHardwareMap = new MecanumMotorsLinearSlideGrabberHardwareMap(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        teamHardwareMap.runTime.reset();

        MecanumHelper mecanumHelper = new MecanumHelper(teamHardwareMap.frontRightMotor, teamHardwareMap.backRightMotor, teamHardwareMap.backLeftMotor, teamHardwareMap.frontLeftMotor, false);

        boolean bumperRightPressed = false;
        boolean bumperLeftPressed = false;

        while (opModeIsActive()) {
            double leftStickYInput = -gamepad1.left_stick_y;
            double leftStickXInput = gamepad1.left_stick_x;
            double leftStickYInput2 = -gamepad2.left_stick_y;
            double rightStickXInput = gamepad1.right_stick_x;

            mecanumHelper.setSpeedToPoint4 = gamepad1.right_trigger > 0.5;

            if (rightStickXInput == 0) {
                mecanumHelper.move(leftStickXInput, leftStickYInput);
            }
            else {
                mecanumHelper.rotate(rightStickXInput);
            }

            teamHardwareMap.linearSlide.setPower(leftStickYInput2 / 1.5);
            //if (rightStickXInput == 0) teamHardwareMap.linearSlide.setPower(0.1);

            if (gamepad2.cross) { // in
                teamHardwareMap.grabberLeft.setPosition(1);
                teamHardwareMap.grabberRight.setPosition(0);
            }
            if (gamepad2.circle) { // out

                //teamHardwareMap.grabberLeft.setPosition(0.54);
                //teamHardwareMap.grabberRight.setPosition(0.48);
                teamHardwareMap.grabberLeft.setPosition(0.60);
                teamHardwareMap.grabberRight.setPosition(0.42);

            }

            if (gamepad1.right_bumper && !bumperRightPressed) {
                mecanumHelper.speed += 0.1;
                if (mecanumHelper.speed > 1) mecanumHelper.speed = 1;
                bumperRightPressed = true;
            }
            if (!gamepad1.right_bumper) bumperRightPressed = false;
            if (gamepad1.left_bumper && !bumperLeftPressed) {
                mecanumHelper.speed -= 0.1;
                if (mecanumHelper.speed < 0) mecanumHelper.speed = 0;
                bumperLeftPressed = true;
            }
            if (!gamepad1.left_bumper) bumperLeftPressed = false;

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + teamHardwareMap.runTime.toString());
            telemetry.addData("Left Stick X", leftStickXInput);
            telemetry.addData("Left Stick Y", leftStickYInput);
            telemetry.addData("FLW", teamHardwareMap.frontLeftMotor.getPower());
            telemetry.addData("FRW", teamHardwareMap.frontRightMotor.getPower());
            telemetry.addData("BLW", teamHardwareMap.backLeftMotor.getPower());
            telemetry.addData("BRW", teamHardwareMap.backRightMotor.getPower());
            telemetry.addData("Speed", String.format("%.2f", mecanumHelper.speed));
            telemetry.update();

        }
    }
}