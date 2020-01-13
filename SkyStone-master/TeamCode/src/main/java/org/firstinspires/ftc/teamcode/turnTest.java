package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="90* Turn test", group="Z")
public class turnTest extends LinearOpMode {
    HardwareDesignosaurs robot = new HardwareDesignosaurs();
    ImuSubClass imu = new ImuSubClass();
    ElapsedTime time = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init2(hardwareMap);
        imu.init(hardwareMap);

        waitForStart();

        imu.turn(90,robot,time,this);
    }
}
