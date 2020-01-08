package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Blue Skystone Tracker", group = "!Auto")
public class BlueSkystoneTracker extends LinearOpMode {
    CameraSubClass camera = new CameraSubClass();
    ImuSubClass imu = new ImuSubClass();
    HardwareDesignosaurs robot = new HardwareDesignosaurs();
    ElapsedTime time = new ElapsedTime();

    double camError;
    double imuError;
    double disError;

    double camInt;
    double imuInt;
    double disInt;

    double camOut;
    double imuOut;
    double disOut;

    double lastTime;
    double deltTime;
    double currTime;

    double fireAtWill = 0;
    double imuTarget;

    @Override
    public void runOpMode() {
        camera.init(hardwareMap, CameraSubClass.Border.LEFT);
        imu.init(hardwareMap);
        robot.init(hardwareMap,0,0,0);


        waitForStart();
        imu.loop();
        imuTarget = imu.getHeading();

        robot.moveRTP("backward", .4, 20, robot, this, time);
        robot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lastTime = time.now(TimeUnit.MILLISECONDS);
        lockOn(true,false,true,false,.05);
        robot.moveRTP("right",.3,7,robot,this,time);
        lockOn(true,true,false,true,.07);

        robot.moveRTP("right",.6,30,robot,this,time);

        robot.leftGripper.setPosition(0);
        robot.wait(1,this,time);

        robot.moveRTP("left", .6, 50,robot,this,time);
        robot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lockOn();
        robot.moveRTP("right",.6,50,robot,this,time);
        robot.leftGripper.setPosition(0);
        robot.wait(1,this,time);
        robot.moveRTP("left",.6,10,robot,this,time);

    }

    double loopIntegral(double input, double cuttoff, double last, double deltaT, double intGain) {
        double output;
        if (input > cuttoff) {
            output = 0;
        } else {
            output = last + (input * deltaT * intGain);
        }
        return output;
    }

    void lockOn(boolean auto, boolean useCamera, boolean useDistance, boolean grab, double accuracy){
        robot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double lastBad = 0;
        if (auto) {
            fireAtWill = 1;
        } else {
            fireAtWill = 0;
        }
        int firstFrame = camera.getImageCount();
        lastTime = time.now(TimeUnit.MILLISECONDS);
        while (opModeIsActive()) {
            // find delta time
            currTime = time.now(TimeUnit.MILLISECONDS);
            deltTime = (currTime - lastTime) / 1000;
            lastTime = currTime;

            // loop sensors
            camera.loop();
            imu.loop();

            // find errors
            if (useCamera) {
                camError = (300 - (double) camera.getBorderX()) / 350;
            } else {
                camError = 0;
            }
            imuError = (imu.getHeading() - imuTarget) / 60;
            if (useDistance) {
                disError = (10 - robot.getDistance()) / 20;
            } else {
                disError = 0;
            }

            if (Math.abs(camError) + Math.abs(disError) + Math.abs(imuError) < accuracy) {
                telemetry.addData("good", " enough");
                if (fireAtWill == 1) {
                    fireAtWill = 2;
                }
            } else {
                lastBad = time.now(TimeUnit.MILLISECONDS);
            }
            telemetry.addData("totErr: ", Math.abs(camError) + Math.abs(imuError) + Math.abs(disError));

            if (Math.abs(disError) > .5) {
                disError = 0;
            }
            if (camera.getBorderX() == -1 || camera.getImageCount() == firstFrame) {
                camError = 0;
            }

            camError = Math.min(Math.max(camError, -.2), 2);
            imuError = Math.min(Math.max(imuError, -.2), 2);
            disError = Math.min(Math.max(disError, -.2), 2);


            //            if (Math.abs(camError) > .5) {
            //                camError = 0;
            //            }

            // calculate integrals
            camInt = loopIntegral(camError, .2, camInt, deltTime, 0.1);
            imuInt = loopIntegral(imuError, .2, imuInt, deltTime, 0.1);
            disInt = loopIntegral(disError, .2, disInt, deltTime, 0.1);

            // log telemetry
            telemetry.addData("camErr: ", camError);
            telemetry.addData("camVal: ", camera.getBorderX());
            telemetry.addData("imuErr: ", imuError);
            telemetry.addData("disErr: ", disError);
            telemetry.addData("camInt: ", camInt);
            telemetry.addData("imuInt: ", imuInt);
            telemetry.addData("disInt: ", disInt);
            telemetry.update();

            // calculate output
            camOut = camError + camInt;
            imuOut = imuError + imuInt;
            disOut = disError + disInt;


            if (gamepad1.a && fireAtWill == 0) {
                fireAtWill = 1;
            }

            // zero values from sensors with high probability of failing
            if (imuError > 0.15) {
                disOut = 0;
                camOut = 0;
            } else if (disError > 0.075) {
                camOut = 0;
            }

            // output to drivetrain
            robot.moveDirection(disOut, camOut, imuOut, robot);

            if (gamepad1.b || fireAtWill == 2 && time.now(TimeUnit.MILLISECONDS) - lastBad > 250 && camera.getImageCount() != firstFrame) {
                robot.setPowers(robot, 0);
                if (grab) {
                    robot.moveRTP("backward", .2, 10, robot, this, time);
                    robot.leftGripper.setPosition(1);
                    robot.wait(1, this, time);
                    robot.moveRTP("forward", .2, 10, robot, this, time);
                }
                break;
            }
        }
    }

    void lockOn(boolean auto, double accuracy) {
        lockOn(auto, true, true, true, accuracy);
    }

    void lockOn() {
        lockOn(true,.1);
    }

    void lockOn(double accuracy) {
        lockOn(true, accuracy);
    }
}
