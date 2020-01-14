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
        time.reset();
        telemetry.addData("selected", "Blue");
        telemetry.update();
        while (!isStopRequested() && time.milliseconds() < 5000) {
        if (gamepad1.b) {
            robot.flip = true;
            telemetry.addData("selected", "Red");
            telemetry.update();
        }
        if (gamepad1.x) {
            robot.flip = false;
            telemetry.addData("selected", "Blue");
            telemetry.update();
        }
        if (gamepad1.a) {

        }
    }
        telemetry.addData("init","Camera");
        telemetry.update();
        camera.init(hardwareMap, CameraSubClass.Border.LEFT);
        telemetry.addData("init","Imu");
        telemetry.update();
        imu.init(hardwareMap);
        telemetry.addData("init","Robot");
        telemetry.update();
        robot.init2(hardwareMap,0,0,0);
        telemetry.addData("init", "done");
        telemetry.addData("selected", robot.flip ? "Red" : "Blue");
        telemetry.update();



        waitForStart();
        imu.loop();
        imuTarget = imu.getHeading() + 90;

        robot.moveRTP("right", .4,13.5,robot,this,time);
        robot.moveRTP("forward", .4,2,robot,this,time);
        robot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        imu.turnSimp(90,robot,this);
        //lockOn(false,false, .4);

        robot.moveRTP(HardwareDesignosaurs.Direction.BACKWARD, .4, 6, robot, this, time);
        robot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lockOn(true,true,true,false,.1);


        robot.moveRTP(HardwareDesignosaurs.Direction.RIGHT,.6,30,robot,this,time);

        robot.leftGripper.setPosition(0);
        robot.wait(1,this,time);

        robot.moveRTP(HardwareDesignosaurs.Direction.LEFT, .6, 50,robot,this,time);
        robot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lockOn();
        robot.moveRTP(HardwareDesignosaurs.Direction.RIGHT,.6,50,robot,this,time);
        robot.leftGripper.setPosition(0);
        robot.wait(1,this,time);
        robot.moveRTP(HardwareDesignosaurs.Direction.LEFT,.6,10,robot,this,time);

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

    void lockOn(boolean auto, boolean useCamera, boolean useDistance, boolean grab, double accuracy, double maxSpeed){
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
                camError = (500 - (double) camera.getBorderX()) / 350;
            } else {
                camError = 0;
            }
            imuError = (imu.getHeading() - imuTarget) / 60;
            if (useDistance) {
                disError = (8 - robot.getDistance()) / 15;
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
            if ( camera.getImageCount() == firstFrame) {
                camError = 0;
            }

            camError = robot.limit(camError,maxSpeed);
            imuError = robot.limit(imuError,maxSpeed);
            disError = robot.limit(disError,maxSpeed);
//            camError = Math.min(Math.max(camError, -.2), 2);
//            imuError = Math.min(Math.max(imuError, -.2), 2);
//            disError = Math.min(Math.max(disError, -.2), 2);


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
                    robot.moveRTP("right",.2,3,robot,this,time);
                    robot.moveRTP("backward", .2, 7, robot, this, time);
                    robot.leftGripper.setPosition(1);
                    robot.wait(1, this, time);
                    robot.moveRTP("forward", .2, 7, robot, this, time);
                }
                break;
            }
        }
    }

    void lockOn(boolean auto, boolean useCamera, boolean useDistance, boolean grab, double accuracy){
        lockOn(auto,useCamera,useDistance,grab,accuracy,.2);
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

    void lockOn(boolean camera, boolean distance, double speed) {
        lockOn(true, camera, distance, true, .1, speed);
    }

    void lockOn(boolean camera, boolean distance) {
        lockOn(true, camera, distance, true, .1, .2);
    }
}
