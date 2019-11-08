package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Parking mission", group="test")
public class ParkingAuto extends LinearOpMode{

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime     runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        waitForStart();
        Robot.init(hardwareMap, 0, 0, 0);

        //Robot.moveRTP("forward", .4,Robot.encoder_ticks_per_revolution* Robot.encoder_ticks_per_inch,Robot,this,runtime);
        Robot.moveRTP("left", .4, 6.0 ,Robot, this, runtime);
        /*Robot.moveRTP("backward", .4, 6.0 ,Robot, this, runtime);
        Robot.moveRTP("left", .4, 6.0 ,Robot, this, runtime);
        Robot.moveRTP("right", .4, 6.0 ,Robot, this, runtime);  //*/
    }

}
