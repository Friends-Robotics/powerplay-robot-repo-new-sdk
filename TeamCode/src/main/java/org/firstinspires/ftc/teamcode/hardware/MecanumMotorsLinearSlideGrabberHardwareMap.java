package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MecanumMotorsLinearSlideGrabberHardwareMap extends TeamHardwareMap {
    public MecanumMotorsLinearSlideGrabberHardwareMap(HardwareMap hardwareMap) {
        super(hardwareMap);
    }

    public DcMotor frontRightMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor frontLeftMotor;

    public CRServo linearSlide;
    public Servo grabberRight;
    public Servo grabberLeft;

    @Override
    protected void initialise() {
        frontRightMotor = hardwareMap.get(DcMotor.class, "FRW");
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backRightMotor = hardwareMap.get(DcMotor.class, "BRW");
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        backLeftMotor = hardwareMap.get(DcMotor.class, "BLW");
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeftMotor = hardwareMap.get(DcMotor.class, "FLW");
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        linearSlide = hardwareMap.get(CRServo.class, "LSD");
        grabberRight = hardwareMap.get(Servo.class, "RG");
        grabberLeft = hardwareMap.get(Servo.class, "LG");
    }
}
