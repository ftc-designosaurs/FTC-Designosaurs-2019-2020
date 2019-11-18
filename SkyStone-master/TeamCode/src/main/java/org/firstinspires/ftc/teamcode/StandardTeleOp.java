package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class StandardTeleOp extends OpMode {

    // variables
    boolean isLowGear = false;
    boolean isPitchLowGear = false;
    int pitchPos = 0;
    static int pitchPosOne = 0;
    static int pitchPosTwo = 324;

    int liftPos = 0;
    static int liftPosOne = 0;
    static int liftPosTwo = 100;
    static int liftPosThree = 200;
    static int liftPosFour = 300;


    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime runtime = new ElapsedTime();

    double lastTime = runtime.now(TimeUnit.MILLISECONDS);
    double deltaTime = 0;

    @Override
    public void init() {
        Robot.init(hardwareMap, 0, 0, 0); //initialize motors and sensors

        telemetry.addData("status: ", "Ready!");
        Robot.pitchMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Robot.pitchMotor.setTargetPosition(pitchPos);
        Robot.pitchMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Robot.liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        double fr, fl, bl, br; // Motor variables
        double lh, lv, rh;     // Joystick variables

        // calculate delta time
        deltaTime = runtime.now(TimeUnit.MILLISECONDS) - lastTime;
        lastTime = runtime.now(TimeUnit.MILLISECONDS);

        // set joystick variables
        lh = gamepad1.left_stick_x;
        lv = -gamepad1.left_stick_y;
        rh = gamepad1.right_stick_x;

        // square for exponential drive
        lh = Robot.square(lh, Robot.power);
        lv = Robot.square(lv, Robot.power);
        rh = Robot.square(rh, Robot.power);

        // calculate motor powers for mecanum drive
        fl = -lv + lh + rh;
        fr = -lv - lh - rh;
        bl = lv - lh + rh;
        br = lv + lh - rh;

        // set motor speeds
        if (isLowGear) {
            Robot.frontRight.setPower(fr/3);
            Robot.frontLeft.setPower(fl/3);
            Robot.backRight.setPower(br/3);
            Robot.backLeft.setPower(bl/3);
        } else {
            Robot.frontRight.setPower(fr);
            Robot.frontLeft.setPower(fl);
            Robot.backRight.setPower(br);
            Robot.backLeft.setPower(bl);
        }


        // set gripper location
        if (gamepad1.left_trigger > .5||gamepad2.left_bumper){
            Robot.mainGripper.setPosition(1);
        }

        if (gamepad1.right_trigger > .5||gamepad2.right_bumper){
            Robot.mainGripper.setPosition(.25);
        }

        Robot.pitchMotor.setPower(gamepad2.left_stick_y/2.5);
        Robot.liftMotor.setPower(gamepad2.right_stick_y);

        telemetry.addData("lift enc", Robot.liftMotor.getCurrentPosition());
        telemetry.addData("pitch enc", Robot.pitchMotor.getCurrentPosition());
        telemetry.update();

        //set position of auto grippers
        if (gamepad2.a) {
            Robot.rightGripper.setPosition(0);
        } else if (gamepad2.y) {
            Robot.rightGripper.setPosition(1);
        }

        if (gamepad2.dpad_up) {
            Robot.leftGripper.setPosition(0);
        } else if (gamepad2.dpad_down) {
            Robot.leftGripper.setPosition(1);
        }

        // set position of foundation manipulator
        if (gamepad2.x || gamepad1.left_trigger > .5) {
            Robot.foundationGripper.setPosition(0.7);
        } else if (gamepad2.b || gamepad1.right_trigger > .5) {
            Robot.foundationGripper.setPosition(0.9);
        }
        if (gamepad1.left_bumper) {
            isLowGear = true;
        } else if (gamepad1.right_bumper) {
            isLowGear = false;
        }

        // set lift and pitch motor variables to macro positions
        if (gamepad2.a) {
            liftPos = liftPosOne;
        } else if (gamepad2.b) {
            liftPos = liftPosTwo;
        } else if (gamepad2.y) {
            liftPos = liftPosThree;
        } else if (gamepad2.x) {
            liftPos = liftPosFour;
        }
        if (gamepad2.dpad_left) {
            pitchPos = pitchPosOne;
        } else if (gamepad2.dpad_right) {
            pitchPos = pitchPosTwo;
        }

        if (gamepad2.left_trigger > .5) {
            Robot.capstoneGripper.setPosition(0);
        } else if (gamepad2.right_trigger > .5) {
            Robot.capstoneGripper.setPosition(1);
        }
        // manually change lift and pitch vars
        //liftPos += gamepad2.right_stick_y * deltaTime;
        //pitchPos += gamepad2.left_stick_y * deltaTime;
        //Robot.pitchMotor.setPower(0.7);
        //Robot.liftMotor.setPower(0.7);
        // set target positions based on vars
        //Robot.pitchMotor.setTargetPosition(pitchPos);
        //Robot.liftMotor.setTargetPosition(liftPos);

    }

    @Override
    public void stop() {
        telemetry.addData("status: ","Done!");
    }

}
