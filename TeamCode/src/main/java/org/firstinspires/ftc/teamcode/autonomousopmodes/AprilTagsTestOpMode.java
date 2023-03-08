/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.autonomousopmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.MecanumHelper;
import org.firstinspires.ftc.teamcode.hardware.MecanumMotorsLinearSlideGrabberHardwareMap;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;

@TeleOp(name="April Tags Test", group="tests")
public class AprilTagsTestOpMode extends LinearOpMode
{
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // tag IDs 1, 2, 3 from 36h11 family
    final int LEFT = 1;
    final int MIDDLE = 2;
    final int RIGHT = 3;

    AprilTagDetection tagOfInterest = null;

    private MecanumMotorsLinearSlideGrabberHardwareMap teamHardwareMap;
    boolean firstDone = false;

    @Override
    public void runOpMode()
    {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(1920,1080, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        teamHardwareMap = new MecanumMotorsLinearSlideGrabberHardwareMap(hardwareMap);
        MecanumHelper mecanumHelper = new MecanumHelper(teamHardwareMap.frontRightMotor, teamHardwareMap.backRightMotor, teamHardwareMap.backLeftMotor, teamHardwareMap.frontLeftMotor, true);
        teamHardwareMap.runTime.reset();
        firstDone = false;

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */
        while (!isStarted() && !isStopRequested())
        {
            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if(currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)
                {
                    if(tag.id == LEFT || tag.id == MIDDLE  || tag.id == RIGHT)
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }
                }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest :(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen)");
                }
                else
                {
                    telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        /* Actually do something useful */
        if(tagOfInterest == null)
        {
            /*
             * Insert your autonomous code here, presumably running some default configuration
             * since the tag was never sighted during INIT
             */
        }
        else
        {
            /*
             * Insert your autonomous code here, probably using the tag pose to decide your configuration.
             */

            switch (tagOfInterest.id) {
                case LEFT:
                    while (opModeIsActive()) {
                        telemetry.addData("DONE", firstDone);

                        while (!firstDone) {
                            teamHardwareMap.frontLeftMotor.setTargetPosition((int) (0.235 * 9.8 * 288));
                            teamHardwareMap.backRightMotor.setTargetPosition((int) (0.235 * 9.8 * 288));
                            teamHardwareMap.backLeftMotor.setTargetPosition((int) (0.235 * 9.8 * 288));
                            teamHardwareMap.frontRightMotor.setTargetPosition((int) (0.235 * 9.8 * 288));
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

                            if (Math.abs(teamHardwareMap.frontLeftMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 20 && Math.abs(teamHardwareMap.backRightMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 20 && Math.abs(teamHardwareMap.backLeftMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 20 && Math.abs(teamHardwareMap.frontRightMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 20) {
                                firstDone = true;
                            }


                        if (!firstDone && Math.abs(teamHardwareMap.frontLeftMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 7 && Math.abs(teamHardwareMap.backRightMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 7 && Math.abs(teamHardwareMap.backLeftMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 7 && Math.abs(teamHardwareMap.frontRightMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 7) {
                            firstDone = true;
                            teamHardwareMap.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            teamHardwareMap.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            teamHardwareMap.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            teamHardwareMap.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        }

                        if (firstDone) {
                            teamHardwareMap.frontLeftMotor.setTargetPosition((int) (-1.4 * 569));
                            teamHardwareMap.backRightMotor.setTargetPosition((int) (-1.4 * 569));
                            teamHardwareMap.backLeftMotor.setTargetPosition((int) (-1.4 * -569));
                            teamHardwareMap.frontRightMotor.setTargetPosition((int) (-1.4 * -569));
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
                        }
                    }
                    break;

                case MIDDLE:
                    teamHardwareMap.frontLeftMotor.setTargetPosition((int)(-1.4 * 569));
                    teamHardwareMap.backRightMotor.setTargetPosition((int)(-1.4 * 569));
                    teamHardwareMap.backLeftMotor.setTargetPosition((int)(-1.4 * -569));
                    teamHardwareMap.frontRightMotor.setTargetPosition((int)(-1.4 * -569));
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
                    break;

                case RIGHT:
                    if (!firstDone) {
                        teamHardwareMap.frontLeftMotor.setTargetPosition((int) (-0.235 * 9.8 * 288));
                        teamHardwareMap.backRightMotor.setTargetPosition((int) (-0.235 * 9.8 * 288));
                        teamHardwareMap.backLeftMotor.setTargetPosition((int) (-0.235 * 9.8 * 288));
                        teamHardwareMap.frontRightMotor.setTargetPosition((int) (-0.235 * 9.8 * 288));
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
                    }

                    if (!firstDone && Math.abs(teamHardwareMap.frontLeftMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 3 && Math.abs(teamHardwareMap.backRightMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 3 && Math.abs(teamHardwareMap.backLeftMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 3 && Math.abs(teamHardwareMap.frontRightMotor.getCurrentPosition() - (int) (0.235 * 9.8 * 288)) < 3) {
                        firstDone = true;
                        teamHardwareMap.frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        teamHardwareMap.backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        teamHardwareMap.backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        teamHardwareMap.frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    }

                    if (firstDone) {
                        teamHardwareMap.frontLeftMotor.setTargetPosition((int) (-1.4 * 569));
                        teamHardwareMap.backRightMotor.setTargetPosition((int) (-1.4 * 569));
                        teamHardwareMap.backLeftMotor.setTargetPosition((int) (-1.4 * -569));
                        teamHardwareMap.frontRightMotor.setTargetPosition((int) (-1.4 * -569));
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
                        telemetry.addData("DONE", teamHardwareMap.backRightMotor.getCurrentPosition());
                        telemetry.update();
                    }
                    break;
            }
        }


        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {sleep(20);}
    }

    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
