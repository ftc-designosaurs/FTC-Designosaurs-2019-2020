package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


@TeleOp(name = "GoToPoleTest", group = "Sensor")
public class GoToPoleTest extends LinearOpMode {

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    @Override
    public void runOpMode() {
        Robot.init(hardwareMap,0,0,0);

        waitForStart();

        Robot.runToPost(HardwareDesignosaurs.Direction.RIGHT,.2,20,this);

    }
}
