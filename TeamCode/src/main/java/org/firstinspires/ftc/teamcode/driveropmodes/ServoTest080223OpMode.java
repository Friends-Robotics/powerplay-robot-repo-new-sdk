package org.firstinspires.ftc.teamcode.driveropmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.LinearSlideWithGrabberHardwareMap;


@TeleOp(name="Servo Test 08-02-23", group="Linear Opmode")
public class ServoTest080223OpMode extends LinearOpMode {

    private LinearSlideWithGrabberHardwareMap teamHardwareMap;


    @Override
    public void runOpMode() {
        teamHardwareMap = new LinearSlideWithGrabberHardwareMap(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        teamHardwareMap.runTime.reset();


        while(opModeIsActive())
        {
            if (gamepad1.circle) {
                teamHardwareMap.servo0.setPosition(1);
                teamHardwareMap.servo1.setPosition(0);
            }
            if (gamepad1.cross) {
                teamHardwareMap.servo0.setPosition(0);
                teamHardwareMap.servo1.setPosition(1);
            }
        }
    }
}
