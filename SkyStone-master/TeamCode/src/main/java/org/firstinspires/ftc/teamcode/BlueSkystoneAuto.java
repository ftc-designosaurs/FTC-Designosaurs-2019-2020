package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Blue Skystone", group="Mechanum")
public class BlueSkystoneAuto extends LinearOpMode{

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime     runtime = new ElapsedTime();

    public int pos = 0;

    @Override
    public void runOpMode() {
        waitForStart();
        Robot.init(hardwareMap, 0, 0, 0);

        Robot.moveRTP("backward", .4, 10.0, Robot, this, runtime);
        Robot.moveRTP("left", .4, 30.0 ,Robot, this, runtime);

        if (pos == 0) {
            Robot.moveRTP("right",.4,1 ,Robot ,this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("right",.4,9 ,Robot ,this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("right",.4,17 ,Robot ,this, runtime);
        }

        Robot.moveRTP("backward", .4, 22.0 ,Robot, this, runtime);
        Robot.leftGripper.setPosition(1);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("forward", .4, 10.0 ,Robot, this, runtime);
        Robot.moveRTP("right", .4, 60.0 ,Robot, this, runtime);
        Robot.leftGripper.setPosition(0);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }

    }

}
