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

        double speed=0.5;
        Robot.frontRight.setPower(-speed);
        Robot.frontLeft.setPower(-speed);
        Robot.backRight.setPower(speed);
        Robot.backLeft.setPower(speed);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < .2)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        Robot.frontRight.setPower(-speed);
        Robot.frontLeft.setPower(speed);
        Robot.backRight.setPower(speed);
        Robot.backLeft.setPower(-speed);

        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < .6)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }
    }

}
