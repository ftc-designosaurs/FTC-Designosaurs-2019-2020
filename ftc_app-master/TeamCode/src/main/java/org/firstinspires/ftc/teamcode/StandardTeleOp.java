package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Mecanum Drive", group="TeleOp")
public class StandardTeleOp extends OpMode {

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();

    @Override
    public void init() {
        Robot.init(hardwareMap); //initialize motors and sensors

        telemetry.addData("status: ", "Ready!");
    }

    @Override
    public void loop() {
        double fr, fl, bl, br; // Motor variables
        double lh, lv, rh;     // Joystick variables

        // set joystick variables
        lh = gamepad1.left_stick_x;
        lv = -gamepad1.left_stick_y;
        rh = -gamepad1.right_stick_x;

        // Square for more control at lower speeds
        lh = lh * lh;
        lv = lv * lv;
        rh = rh * rh;

        // calculate motor powers for mecanum drive
        fl = lv + lh + rh;
        fr = lv - lh - rh;
        bl = lv - lh + rh;
        br = lv + lh - rh;

        // set motor speeds
        Robot.frontRight.setPower(fr);
        Robot.frontLeft.setPower(fl);
        Robot.backRight.setPower(br);
        Robot.backLeft.setPower(bl);
    }

    @Override
    public void stop() {
        telemetry.addData("status: ","Done!");
    }

}
