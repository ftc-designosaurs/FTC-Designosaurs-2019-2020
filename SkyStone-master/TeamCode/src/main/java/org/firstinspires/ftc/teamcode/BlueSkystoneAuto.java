package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Parking mission", group="Mechanum")
public class BlueSkystoneAuto extends LinearOpMode{

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        waitForStart();
        Robot.init(hardwareMap, 0, 0, 0);

        Robot.moveRTP("backward", .4, 10.0 ,Robot, this, runtime);
        Robot.moveRTP("left", .4, 30.0 ,Robot, this, runtime);
        Robot.moveRTP("backward", .4, 15.0 ,Robot, this, runtime);
        Robot.leftGripper.setPosition(1);
        Robot.moveRTP("forward", .4, 10.0 ,Robot, this, runtime);
        Robot.moveRTP("right", .4, 70.0 ,Robot, this, runtime);
        Robot.leftGripper.setPosition(0);

    }

}
