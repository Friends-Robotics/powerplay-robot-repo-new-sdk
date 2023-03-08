package org.firstinspires.ftc.teamcode.autonomousopmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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

@Autonomous(name="Powerplay Autonomous", group="tests")
public class PowerplayAutonomousOpMode extends LinearOpMode {

    private MecanumMotorsLinearSlideGrabberHardwareMap teamHardwareMap;

    @Override
    public void runOpMode() {
        teamHardwareMap = new MecanumMotorsLinearSlideGrabberHardwareMap(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        teamHardwareMap.runTime.reset();

        MecanumHelper mecanumHelper = new MecanumHelper(teamHardwareMap.frontRightMotor, teamHardwareMap.backRightMotor, teamHardwareMap.backLeftMotor, teamHardwareMap.frontLeftMotor, true);

        while (opModeIsActive()) {
            // 288 ticks is one rotation
            // 96mm diameter
            // 302mm circumference
            // 1mm forward = 0.953642384 ticks
            // 23.5 inches (596.9mm) forward = 569.229139073 ticks (ONE TILE VERTICAL)

            /* BACKWARD 32.25 INCHES CODE:
            teamHardwareMap.frontLeftMotor.setTargetPosition((int)(1.4 * 569));
            teamHardwareMap.backRightMotor.setTargetPosition((int)(1.4 * 569));
            teamHardwareMap.backLeftMotor.setTargetPosition((int)(1.4 * -569));
            teamHardwareMap.frontRightMotor.setTargetPosition((int)(1.4 * -569));
            teamHardwareMap.frontLeftMotor.setPower(0.2);
            teamHardwareMap.backRightMotor.setPower(0.2);
            teamHardwareMap.backLeftMotor.setPower(0.2);
            teamHardwareMap.frontRightMotor.setPower(0.2);

            teamHardwareMap.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            teamHardwareMap.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            teamHardwareMap.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            teamHardwareMap.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            telemetry.addData("FLW", teamHardwareMap.frontLeftMotor.getCurrentPosition());
            telemetry.addData("FRW", teamHardwareMap.frontRightMotor.getCurrentPosition());
            telemetry.addData("BLW", teamHardwareMap.backLeftMotor.getCurrentPosition());
            telemetry.addData("BRW", teamHardwareMap.backRightMotor.getCurrentPosition());
            telemetry.update();
             */

            /* LEFT 23.5 INCHES CODE:
            teamHardwareMap.frontLeftMotor.setTargetPosition((int)(0.235 * 9.8 * 288));
            teamHardwareMap.backRightMotor.setTargetPosition((int)(0.235 * 9.8 * 288));
            teamHardwareMap.backLeftMotor.setTargetPosition((int)(0.235 * 9.8 * 288));
            teamHardwareMap.frontRightMotor.setTargetPosition((int)(0.235 * 9.8 * 288));
            teamHardwareMap.frontLeftMotor.setPower(0.2);
            teamHardwareMap.backRightMotor.setPower(0.2);
            teamHardwareMap.backLeftMotor.setPower(0.2);
            teamHardwareMap.frontRightMotor.setPower(0.2);

            teamHardwareMap.frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            teamHardwareMap.backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            teamHardwareMap.backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            teamHardwareMap.frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            telemetry.addData("FLW", teamHardwareMap.frontLeftMotor.getCurrentPosition());
            telemetry.addData("FRW", teamHardwareMap.frontRightMotor.getCurrentPosition());
            telemetry.addData("BLW", teamHardwareMap.backLeftMotor.getCurrentPosition());
            telemetry.addData("BRW", teamHardwareMap.backRightMotor.getCurrentPosition());
            telemetry.update();
             */
        }
    }
}