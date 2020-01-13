package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Disabled
@Deprecated
@TeleOp(name = "Sensor: REV2mDistance p test", group = "Sensor")

public class distanceTest extends LinearOpMode {

    public DistanceSensor distance;
    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    public double speed = 0;
    public double nominalSpeed = 0.2;
    public double speedGain = 0.1;

    @Override
    public void runOpMode() {
        distance = hardwareMap.get(DistanceSensor.class,"sensor_range");
        Robot.init(hardwareMap,0,0,0);

        waitForStart();
        while (opModeIsActive()) {
            speed = (20 - distance.getDistance(DistanceUnit.INCH)) * speedGain;
            Robot.frontLeft.setPower(-(speed));
            Robot.frontRight.setPower(-(speed));
            Robot.backLeft.setPower(speed);
            Robot.backRight.setPower(speed);
        }

        Robot.setPowers(Robot,0);
    }
}
