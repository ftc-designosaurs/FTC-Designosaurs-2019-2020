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

    // Define Servos
    public Servo gripper    = null;

    // Define Sensors
    
    // Define Variables
    public int power = 3;

    public double xPos, yPos, thetaPos,
            deltaFR, deltaFL, deltaBR, deltaBL,j;

    public static final double encoder_ticks_per_revolution = 537.6;
    public static final double turn_diameter = 16; // if the wheelbase was a circle, this is the diameter
    public static final double wheel_diameter = 4; // inches
    private double IMU_ThetaRAD = 0; //TODO: Configure imu

    public static final double ticks_per_degree =
            (turn_diameter/wheel_diameter)*
                    (encoder_ticks_per_revolution/360); //used in encoder turn
    public static final double encoder_ticks_per_inch =
            encoder_ticks_per_revolution/
                    (wheel_diameter * Math.PI); // used in encoder drive

    HardwareMap hwMap = null;
    
    public HardwareDesignosaurs() {
        
    }
    
    public void init(HardwareMap ahwMap, int startX, int startY, int startTheta) {
        hwMap = ahwMap;

        // Initialize Motors
        frontRight = hwMap.get(DcMotor.class,"front_right");
        frontLeft = hwMap.get(DcMotor.class,"front_left");
        backRight = hwMap.get(DcMotor.class,"back_right");
        backLeft = hwMap.get(DcMotor.class,"back_left");


        // Initialize Servos
        gripper = hwMap.get(Servo.class, "left_gripper");

        // Initialize Sensors

        // Set Motor Directions
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);

        // Stop Motors
        frontRight.setPower(0);
        frontLeft.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);

        // Enable All Encoders
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set Servo Positions

        gripper.setPosition(0);

    }

    // Functions
    public double square(double base, int power) { //function that does fractional squaring
        if (base == 0) {
            return 0;
        } else {
            double basen, based;
            basen = base; // numerator
            based = base; // denominator

            // Multiplies numerator
            for (int i = 0; i < (power+16); i++) {
                basen *= base;
            }

            // Multiplies denominator
            for (int i = 0; i < 16; i++) {
                based *= base;
            }
            return basen/based;

        }
    }

    public void deadReckoningLoop(HardwareDesignosaurs Robot){

        disFR = (Robot.frontRight - lastFR) * ;
        //Compute change in encoder positions
        delt_m0 = wheel0Pos - lastM0;
        delt_m1 = wheel1Pos - lastM1;
        delt_m2 = wheel2Pos - lastM2;
        delt_m3 = wheel3Pos - lastM3;
//Compute displacements for each wheel
        displ_m0 = delt_m0 * wheelDisplacePerEncoderCount;
        displ_m1 = delt_m1 * wheelDisplacePerEncoderCount;
        displ_m2 = delt_m2 * wheelDisplacePerEncoderCount;
        displ_m3 = delt_m3 * wheelDisplacePerEncoderCount;
//Compute the average displacement in order to untangle rotation from displacement
        displ_average = (displ_m0 + displ_m1 + displ_m2 + displ_m3) / 4.0;
//Compute the component of the wheel displacements that yield robot displacement
        dev_m0 = displ_m0 - displ_average;
        dev_m1 = displ_m1 - displ_average;
        dev_m2 = displ_m2 - displ_average;
        dev_m3 = displ_m3 - displ_average;
//Compute the displacement of the holonomic drive, in robot reference frame
        delt_Xr = (dev_m0 + dev_m1 - dev_m2 - dev_m3) / twoSqrtTwo;
        delt_Yr = (dev_m0 - dev_m1 - dev_m2 + dev_m3) / twoSqrtTwo;
//Move this holonomic displacement from robot to field frame of reference
        robotTheta = IMU_ThetaRAD;
        sinTheta = Math.sin(robotTheta);
        cosTheta = Math.cos(robotTheta);
        delt_Xf = delt_Xr * cosTheta - delt_Yr * sinTheta;
        delt_Yf = delt_Yr * cosTheta + delt_Xr * sinTheta;
//Update the position
        xPos = lastX + delt_Xf;
        yPos = lastY + delt_Yf;
        Theta = robotTheta;
        lastM0 = wheel0Pos;
        lastM1 = wheel1Pos;
        lastM2 = wheel2Pos;
        lastM3 = wheel3Pos;
    }
}
