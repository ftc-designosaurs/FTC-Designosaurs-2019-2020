package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class StandardTeleOp extends OpMode {

    // variables
    boolean isLowGear = false;

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();

    @Override
    public void init() {
        Robot.init(hardwareMap, 0, 0, 0); //initialize motors and sensors

        telemetry.addData("status: ", "Ready!");
    }

    @Override
    public void loop() {
        double fr, fl, bl, br; // Motor variables
        double lh, lv, rh;     // Joystick variables

        // set joystick variables
        if (isLowGear) {
            lh = gamepad1.left_stick_x/3;
            lv = -gamepad1.left_stick_y/3;
            rh = gamepad1.right_stick_x/3;
        } else {
            lh = gamepad1.left_stick_x;
            lv = -gamepad1.left_stick_y;
            rh = gamepad1.right_stick_x;
        }


        // square for exponential drive
        lh = Robot.square(lh, Robot.power)/2;
        lv = Robot.square(lv, Robot.power)/2;
        rh = Robot.square(rh, Robot.power)/2;

        // calculate motor powers for mecanum drive
        fl = -lv + lh + rh;
        fr = -lv - lh - rh;
        bl = lv - lh + rh;
        br = lv + lh - rh;

        // set motor speeds
        Robot.frontRight.setPower(fr);
        Robot.frontLeft.setPower(fl);
        Robot.backRight.setPower(br);
        Robot.backLeft.setPower(bl);

        // set gripper location
        if (gamepad1.left_trigger > .5||gamepad2.left_bumper){
            Robot.mainGripper.setPosition(1);
        }

        if (gamepad1.right_trigger > .5||gamepad2.right_bumper){
            Robot.mainGripper.setPosition(.5);
        }

        Robot.pitchMotor.setPower(gamepad2.left_stick_y/1.5);
        Robot.liftMotor.setPower(gamepad2.right_stick_y);

        telemetry.addData("lift enc", Robot.liftMotor.getCurrentPosition());
        telemetry.addData("pitch enc", Robot.pitchMotor.getCurrentPosition());
        telemetry.update();

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

        if (gamepad2.x) {
            Robot.foundationGripper.setPosition(0.5);
        } else if (gamepad2.b) {
            Robot.foundationGripper.setPosition(1);
        }
        if (gamepad1.left_bumper) {
            isLowGear = true;
        } else if (gamepad1.right_bumper) {
            isLowGear = false;
        }

    }

    @Override
    public void stop() {
        telemetry.addData("status: ","Done!");
    }

}
