package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HardwareDesignosaurs {
    
    // Define Motors
    public DcMotor frontRight = null;
    public DcMotor frontLeft  = null;
    public DcMotor backRight  = null;
    public DcMotor backLeft   = null;

    public DcMotor lift1      = null;
    public DcMotor lift2      = null;

    // Define Servos
    public Servo gripLeft     = null;
    public Servo gripRight    = null;

    // Define Sensors
    
    // Define Variables
    
    HardwareMap hwMap = null;
    
    public HardwareDesignosaurs() {
        
    }
    
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        // Initialize Motors
        frontRight = hwMap.get(DcMotor.class,"front_right");
        frontLeft = hwMap.get(DcMotor.class,"front_left");
        backRight = hwMap.get(DcMotor.class,"back_right");
        backLeft = hwMap.get(DcMotor.class,"back_left");

        lift1 = hwMap.get(DcMotor.class,"lift_1");
        lift2 = hwMap.get(DcMotor.class,"lift_2");


        // Initialize Servos
        gripLeft = hwMap.get(Servo.class, "left_gripper");
        gripRight = hwMap.get(Servo.class,"right_gripper");

        // Initialize Sensors

        // Set Motor Directions
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);

        lift1.setDirection(DcMotor.Direction.FORWARD);
        lift2.setDirection(DcMotor.Direction.FORWARD);

        // Stop Motors
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        lift1.setPower(0);
        lift2.setPower(0);

        // Enable All Encoders
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lift1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lift2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set Servo Positions

        gripRight.setPosition(0);
        gripLeft.setPosition(0);

    }

    // Functions
}
