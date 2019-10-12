package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Parking mission", group="Mechanum")
public class ParkingAuto extends LinearOpMode{

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        waitForStart();
        Robot.init(hardwareMap, 0, 0, 0);

        Robot.move("forward", .4, 6.0 ,Robot, this);
        Robot.move("backward", .4, 6.0 ,Robot, this);
        Robot.move("left", .4, 6.0 ,Robot, this);
        Robot.move("right", .4, 6.0 ,Robot, this);
    }

}
