package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class StandardTeleOp extends OpMode {

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
        Robot.frontRight.setPower(fr);
        Robot.frontLeft.setPower(fl);
        Robot.backRight.setPower(br);
        Robot.backLeft.setPower(bl);

        // set gripper location
        if (gamepad1.left_trigger > .5||gamepad2.dpad_left){
            Robot.gripper.setPosition(1);
        }

        if (gamepad1.right_trigger > .5||gamepad2.dpad_right){
            Robot.gripper.setPosition(.5);
        }

        telemetry.addData("X pos", Robot.xPos);
        telemetry.addData("Y pos", Robot.yPos);
        telemetry.update();
    }

    @Override
    public void stop() {
        telemetry.addData("status: ","Done!");
    }

}
