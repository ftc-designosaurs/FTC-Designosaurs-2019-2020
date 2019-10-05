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
            lastFR, lastFL, lastBR, lastBL, // last positions
            disFR, disFL, disBR, disBL, // displacements by wheel
            averageDisplacement, // what it says on the tin
            devFR, devFL, devBR, devBL, // difference from average
            deltXr, deltYr, deltXf, deltYf; // deltas in robot and field frame of reference.

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

        //Compute delta displacement for each wheel
        disFR = (Robot.frontRight.getCurrentPosition() - lastFR) * encoder_ticks_per_inch;
        disFL = (Robot.frontRight.getCurrentPosition() - lastFL) * encoder_ticks_per_inch;
        disBR = (Robot.frontRight.getCurrentPosition() - lastBR) * encoder_ticks_per_inch;
        disBL = (Robot.frontRight.getCurrentPosition() - lastBL) * encoder_ticks_per_inch;

        //Compute the average displacement in order to untangle rotation from displacement
        averageDisplacement = (disFR + disFL + disBR + disBL) / 4.0;

        //Compute the component of the wheel displacements that yield robot displacement
        devFR = disFR - averageDisplacement;
        devFL = disFL - averageDisplacement;
        devBR = disBR - averageDisplacement;
        devBL = disFL - averageDisplacement;

        //Compute the displacement of the holonomic drive, in robot reference frame
        deltXr = (dev + dev_m1 - dev_m2 - dev_m3) / Math.sqrt(2);
        deltYr = (dev_m0 - dev_m1 - dev_m2 + dev_m3) / Math.sqrt(2);

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
