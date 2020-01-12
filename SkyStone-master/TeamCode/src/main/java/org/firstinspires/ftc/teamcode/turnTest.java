package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class turnTest extends LinearOpMode {
    HardwareDesignosaurs robot = new HardwareDesignosaurs();
    ImuSubClass imu = new ImuSubClass();
    ElapsedTime time = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        imu.init(hardwareMap);

        waitForStart();

        imu.turn(90,robot,time,this);
    }
}
