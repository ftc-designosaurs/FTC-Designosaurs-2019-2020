package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name="Blue Foundation", group="Mechanum")
public class BlueFoundationMove extends LinearOpMode{

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        waitForStart();
        runtime.reset();
        Robot.init(hardwareMap, 0, 0, 0);

        Robot.moveRTP("backward", .4, 24.0 ,Robot, this, runtime);
        Robot.moveRTP("right", .4, 12.0 ,Robot, this, runtime);
        Robot.moveRTP("backward", .4, 10.0 ,Robot, this, runtime);
        Robot.foundationGripper.setPosition(1);
        long startTime = runtime.time(TimeUnit.MILLISECONDS);
        while (Math.abs(runtime.time(TimeUnit.MILLISECONDS) - startTime) < 500) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("forward", .4, 34.0 ,Robot, this, runtime);
        Robot.foundationGripper.setPosition(0);
        startTime = runtime.time(TimeUnit.MILLISECONDS);
        while (Math.abs(runtime.time(TimeUnit.MILLISECONDS) - startTime) < 500) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        //Robot.moveRTP("forward", .4, 1.0 ,Robot, this, runtime);
        Robot.moveRTP("left", .4, 12 ,Robot, this, runtime);

        while (runtime.time(TimeUnit.SECONDS) < 26) {
            telemetry.addData("time left:", 30- runtime.time(TimeUnit.SECONDS));
        }
        Robot.moveRTP("left", .4, 24 ,Robot, this, runtime);
    }

}
