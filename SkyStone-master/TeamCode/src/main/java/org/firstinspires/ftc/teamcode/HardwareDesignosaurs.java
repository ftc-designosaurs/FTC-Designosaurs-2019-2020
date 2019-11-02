package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.concurrent.TimeUnit;

public class HardwareDesignosaurs {

    // Define Motors
    public DcMotor frontRight = null;
    public DcMotor frontLeft  = null;
    public DcMotor backRight  = null;
    public DcMotor backLeft   = null;
    public DcMotor pitchMotor = null;
    public DcMotor liftMotor  = null;

    // Define Servos
    public Servo mainGripper        = null;
    public Servo foundationGripper  = null;
    public Servo leftGripper        = null;
    public Servo rightGripper       = null;

    // Define Sensors

    // Define Variables
    public double startEncoder;
    public double lastTime = 0;
    public double deltaTime = 0;
    public double speed = 0;
    public double accelPerSec = .2;
    public double sideBias = Math.sqrt(2);

    public int power = 3;

    public static final double wheel_diameter = 4;   // inches
    public static final double encoder_ticks_per_revolution = 537.6;
    public static final double encoder_ticks_per_inch =
                    (wheel_diameter * Math.PI)/
                            encoder_ticks_per_revolution; // used in encoder drive

    private static final String VUFORIA_KEY =
            "AdCuaEX/////AAABmXYJgRHZxkB9gj+81cIaX+JZm4W2w3Ee2HhKucJINnuXQ8l214BoCiyEk04zmQ/1VPvo+8PY7Um3eI5rI4WnSJmEXo7jyMz2WZDkkRnA88uBCtbml8CsMSIS7J3aUcgVd9P8ocLLgwqpavhEEaUixEx/16rgzIEtuHcq5ghQzzCkqR1xvAaxnx5lWM+ixf6hBCfZEnaiUM7WjD4gflO55IpoO/CdCWQrGUw2LuUKW2J+4K6ftKwJ+B1Qdy7pt2tDrGZvMyB4AcphPuoJRCSr5NgRoNWZ+WH5LqAdzYEO0Bv7C9LeSgmSPPT7GPPDpjv6+3DO5BE6l+2uMYQQbuF11BWKKq5Xp+D5Y6l2+W97zpgP";
    public static final float mmPerInch = 25.4f;

    HardwareMap hwMap = null;

    public HardwareDesignosaurs() {

    }
  
    public void init(HardwareMap ahwMap, int xPos, int yPos, int thetaPos) {
        hwMap = ahwMap;

        // Initialize Motors
        frontRight = hwMap.get(DcMotor.class,"front_right");
        frontLeft = hwMap.get(DcMotor.class,"front_left");
        backRight = hwMap.get(DcMotor.class,"back_right");
        backLeft = hwMap.get(DcMotor.class,"back_left");
        pitchMotor = hwMap.get(DcMotor.class, "pitch_motor");
        liftMotor = hwMap.get(DcMotor.class, "lift_motor");

        // Initialize Servos
        mainGripper = hwMap.get(Servo.class, "main_manipulator");
        foundationGripper = hwMap.get(Servo.class, "foundation_manipulator");
        leftGripper = hwMap.get(Servo.class, "left_auto_manipulator");
        rightGripper = hwMap.get(Servo.class, "right_auto_manipulator");


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

        mainGripper.setPosition(0);
        foundationGripper.setPosition(0);
        leftGripper.setPosition(0);
        rightGripper.setPosition(1);

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

    public void setSpeeds(double FR, double FL, double BR, double BL, HardwareDesignosaurs Robot){
        Robot.frontRight.setPower(FR);
        Robot.frontLeft.setPower(FL);
        Robot.backRight.setPower(BR);
        Robot.backLeft.setPower(BL);
    }

    public void movePID(String direction, double distance,HardwareDesignosaurs Robot, LinearOpMode opMode, ElapsedTime time){
        startEncoder = Robot.frontRight.getCurrentPosition();
        double error = Math.abs(startEncoder - Robot.frontRight.getCurrentPosition()) / encoder_ticks_per_inch;
        while (error > 1){
            error = Math.abs(startEncoder - Robot.frontRight.getCurrentPosition()) / encoder_ticks_per_inch;
            double speed = error - distance;
            if (direction == "forward") {         setSpeeds(-speed, -speed, speed, speed, Robot);
            } else if (direction == "backward") { setSpeeds(speed, speed, -speed, -speed, Robot);
            } else if (direction == "left") {     setSpeeds(-speed, speed, speed, -speed, Robot);
            } else if (direction == "right") {    setSpeeds(speed, -speed, -speed, speed, Robot);
            } else {  opMode.telemetry.addData("invalid","input");
            }
        }
        deltaTime = time.nanoseconds()-lastTime;
        lastTime = time.nanoseconds();

    }

    public void move(String direction, double maxSpeed, double distance, HardwareDesignosaurs Robot, LinearOpMode opMode) {
        if (direction == "forward") {
            Robot.frontRight.setPower(-speed);
            Robot.frontLeft.setPower(-speed);
            Robot.backRight.setPower(speed);
            Robot.backLeft.setPower(speed);
        } else if (direction == "backward") {
            Robot.frontRight.setPower(speed);
            Robot.frontLeft.setPower(speed);
            Robot.backRight.setPower(-speed);
            Robot.backLeft.setPower(-speed);
        } else if (direction == "left") {
            Robot.frontRight.setPower(-speed);
            Robot.frontLeft.setPower(speed);
            Robot.backRight.setPower(speed);
            Robot.backLeft.setPower(-speed);
        } else if (direction == "right") {
            Robot.frontRight.setPower(speed);
            Robot.frontLeft.setPower(-speed);
            Robot.backRight.setPower(-speed);
            Robot.backLeft.setPower(speed);
        } else {
            opMode.telemetry.addData("failure","invalid input");
            opMode.telemetry.update();
        }
        while (opMode.opModeIsActive() & Math.abs((Robot.frontRight.getCurrentPosition() - startEncoder) * encoder_ticks_per_inch) < distance) {
            opMode.telemetry.addData("inches moved",(Robot.frontRight.getCurrentPosition() - startEncoder) * encoder_ticks_per_inch);
            opMode.telemetry.addData("goal",distance);
            opMode.telemetry.update();
        }
        Robot.frontRight.setPower(0);
        Robot.frontLeft.setPower(0);
        Robot.backRight.setPower(0);
        Robot.backLeft.setPower(0);
    }

    public void setTargetPos (DcMotor motor, double position) {
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setTargetPosition( (int) Math.round(position));
    }

    public void setPowers (HardwareDesignosaurs Robot, double speed) {
        Robot.frontRight.setPower(speed);
        Robot.frontLeft.setPower(speed);
        Robot.backRight.setPower(speed);
        Robot.backLeft.setPower(speed);
    }

    public void moveRTP(String direction, double maxSpeed, double distance, HardwareDesignosaurs Robot, LinearOpMode opMode, ElapsedTime time) {
        double encDist = distance / encoder_ticks_per_inch;
        //double encDist = Math.abs(startEncoder - Robot.frontRight.getCurrentPosition()) * encoder_ticks_per_inch;

        Robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setTargetPos(Robot.frontRight, encDist);
        if (direction == "forward") {
            setTargetPos(Robot.frontLeft, -encDist);
            setTargetPos(Robot.frontRight, -encDist);
            setTargetPos(Robot.backLeft, encDist);
            setTargetPos(Robot.backRight, encDist);

        } else if (direction == "backward") {
            setTargetPos(Robot.frontLeft, encDist);
            setTargetPos(Robot.frontRight, encDist);
            setTargetPos(Robot.backLeft, -encDist);
            setTargetPos(Robot.backRight, -encDist);
        } else if (direction == "left") {
            setTargetPos(Robot.frontLeft, -encDist * sideBias);
            setTargetPos(Robot.frontRight, encDist * sideBias);
            setTargetPos(Robot.backLeft, encDist * sideBias);
            setTargetPos(Robot.backRight, -  encDist * sideBias);

        } else if (direction == "right") {
            setTargetPos(Robot.frontLeft, encDist * sideBias);
            setTargetPos(Robot.frontRight, -encDist * sideBias);
            setTargetPos(Robot.backLeft, -encDist * sideBias);
            setTargetPos(Robot.backRight, encDist * sideBias);
        } else {
            opMode.telemetry.addData("failure","invalid input");
            opMode.telemetry.update();
        }

        time.reset();
        double prevTime = time.time();
        double nowTime;
        double deltaTime;
        double currentMaxSpeed = 0;
        while (Robot.frontRight.isBusy() || Robot.frontLeft.isBusy() || Robot.backRight.isBusy() || Robot.backLeft.isBusy()) {
            nowTime = time.time();
            deltaTime = prevTime - nowTime;
            prevTime = nowTime;
            if (currentMaxSpeed < maxSpeed) {
                currentMaxSpeed += deltaTime * accelPerSec;
            } else {
                currentMaxSpeed = maxSpeed;
            }
            setPowers(Robot, currentMaxSpeed);
            opMode.telemetry.addData("fr",Robot.frontRight.getCurrentPosition());
            opMode.telemetry.addData("fl",Robot.frontLeft.getCurrentPosition());
            opMode.telemetry.addData("br",Robot.backRight.getCurrentPosition());
            opMode.telemetry.addData("bl",Robot.backLeft.getCurrentPosition());
            opMode.telemetry.addData("loop ps", 1/deltaTime);
            opMode.telemetry.addData("current speed", currentMaxSpeed);
            opMode.telemetry.update();
        }
        time.reset();
        while (time.time(TimeUnit.MILLISECONDS) < 500) {
            opMode.telemetry.addData("time until quit", 500 - time.time(TimeUnit.MILLISECONDS));
            opMode.telemetry.update();
        }
        setPowers(Robot, 0);

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
    }
}

