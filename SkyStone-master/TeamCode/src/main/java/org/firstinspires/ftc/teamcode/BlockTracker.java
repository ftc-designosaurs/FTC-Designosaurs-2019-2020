package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.robocol.RobocolConfig;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Block Tracker", group = "Sensor")
public class BlockTracker extends LinearOpMode {
    CameraSubClass camera = new CameraSubClass();
    ImuSubClass imu = new ImuSubClass();
    HardwareDesignosaurs robot = new HardwareDesignosaurs();

    double camError;
    double imuError;
    double disError;

    @Override
    public void runOpMode() {
        camera.init(hardwareMap);
        imu.init(hardwareMap);
        robot.init(hardwareMap,0,0,0);


        waitForStart();
        imu.loop();
        double imuTarget = imu.getHeading();

        while (opModeIsActive()) {
            camera.loop();
            imu.loop();

            camError =  310 - camera.getBorderX();
            imuError = imu.getHeading() - imuTarget;
            disError = 10 - robot.getDistance();

            telemetry.addData("cam: ", camError);
            telemetry.addData("imu: ", imuError);
            telemetry.addData("dis: ", disError);
            telemetry.update();


            if (imuError > 10) {
                disError = 0;
                camError = 0;
            } else if (disError > 2) {
                camError = 0;
            }

            robot.moveDirection(disError/20, camError/350, imuError/100, robot);
        }
    }
}
