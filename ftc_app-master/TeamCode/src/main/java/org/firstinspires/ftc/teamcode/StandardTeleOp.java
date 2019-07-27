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
        double liftSpeed;

        // set joystick variables
        lh = gamepad1.left_stick_x;
        lv = -gamepad1.left_stick_y;
        rh = -gamepad1.right_stick_x;



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

        //
        liftSpeed = (gamepad1.right_trigger - gamepad1.left_trigger) +
                (gamepad2.right_trigger - gamepad2.left_trigger);

        Robot.lift1.setPower(liftSpeed);
        if (!(gamepad1.x||gamepad2.x)) {
            Robot.lift2.setPower(liftSpeed);
        }

        if (gamepad1.dpad_left||gamepad2.dpad_left){
            Robot.gripLeft.setPosition(1);
            Robot.gripRight.setPosition(0);
        }

        if (gamepad1.dpad_right||gamepad2.dpad_right){
            Robot.gripLeft.setPosition(0);
            Robot.gripRight.setPosition(1);
        }

    }

    @Override
    public void stop() {
        telemetry.addData("status: ","Done!");
    }

}
