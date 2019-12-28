package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Block Tracker", group = "Sensor")
public class BlockTracker extends LinearOpMode {
    CameraSubClass camera = new CameraSubClass();
    HardwareDesignosaurs robot = new HardwareDesignosaurs();

    @Override
    public void runOpMode() {
        camera.init(hardwareMap);
        robot.init(hardwareMap,0,0,0);

        waitForStart();
        while (opModeIsActive()) {
            camera.loop();

        }

    }
}
