package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "IMU Test OpMode", group = "IMU")
public class imuTest extends LinearOpMode {

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime runtime = new ElapsedTime();

    double speed = .2;
    double skewGain = 0.01;

    @Override
    public void runOpMode() {
        telemetry.addData("init: ", "robot");
        telemetry.update();
        Robot.init(hardwareMap, 0, 0, 0);
        telemetry.addData("init: ", "gyro");
        telemetry.update();
        Robot.initIMU();
        telemetry.addData("init: ", "done");
        telemetry.update();
        waitForStart();

        runtime.reset();
        double target = Robot.heading;
        while (runtime.time(TimeUnit.SECONDS) < 3) {
            double skewComp = (target - Robot.heading) * skewGain;
            Robot.frontLeft.setPower(-(speed) - skewComp);
            Robot.frontRight.setPower(-(speed) + skewComp);
            Robot.backLeft.setPower(speed + skewComp);
            Robot.backRight.setPower(speed - skewComp);

            telemetry.addData("Heading: ", Robot.angles.firstAngle);
            telemetry.addData("Roll: ", Robot.angles.firstAngle);
            telemetry.addData("Pitch: ", Robot.angles.firstAngle);
            telemetry.update();
        }
        Robot.setPowers(Robot,0);

    }

}


