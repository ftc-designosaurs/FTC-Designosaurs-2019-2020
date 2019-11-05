package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Autonomous(name="Blue Skystone", group="Mechanum")
public class BlueSkystoneAuto extends LinearOpMode{

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "AdCuaEX/////AAABmXYJgRHZxkB9gj+81cIaX+JZm4W2w3Ee2HhKucJINnuXQ8l214BoCiyEk04zmQ/1VPvo+8PY7Um3eI5rI4WnSJmEXo7jyMz2WZDkkRnA88uBCtbml8CsMSIS7J3aUcgVd9P8ocLLgwqpavhEEaUixEx/16rgzIEtuHcq5ghQzzCkqR1xvAaxnx5lWM+ixf6hBCfZEnaiUM7WjD4gflO55IpoO/CdCWQrGUw2LuUKW2J+4K6ftKwJ+B1Qdy7pt2tDrGZvMyB4AcphPuoJRCSr5NgRoNWZ+WH5LqAdzYEO0Bv7C9LeSgmSPPT7GPPDpjv6+3DO5BE6l+2uMYQQbuF11BWKKq5Xp+D5Y6l2+W97zpgP";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    HardwareDesignosaurs Robot = new HardwareDesignosaurs();
    private ElapsedTime     runtime = new ElapsedTime();

    public int pos = 0;

    @Override
    public void runOpMode() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Cmon man!", "ZTEs Suck m8");
        }

        if (tfod != null) {
            tfod.activate();
        }

        telemetry.addData(">", "TFOD Scanning:");
        telemetry.update();


        waitForStart();

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                int i = 0;
                double lowestAvg = Double.POSITIVE_INFINITY;
                double currentAvg = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());

                    if (recognition.getLabel() == "Skystone") {
                        currentAvg = (recognition.getLeft() + recognition.getRight()) / 2;
                        if (currentAvg < lowestAvg) {
                            lowestAvg = currentAvg;
                        }
                    }


                }
                telemetry.addData("middle of sky", lowestAvg);
                if (lowestAvg > 400) {
                    telemetry.addData("Target pos", "Left");
                    pos = 0;
                } else if (lowestAvg > 352 && lowestAvg < 400) {
                    telemetry.addData("Target pos", "Middle");
                    pos = 1;
                } else if (lowestAvg < 352) {
                    telemetry.addData("Target pos", "Right");
                    pos = 2;
                }
                telemetry.update();
            } /*else {
                telemetry.addData("Target pos", "Right");
            }*/


            if (tfod != null) {
                tfod.shutdown();
            }

        }

        while (runtime.time(TimeUnit.MILLISECONDS) < 10000 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
        }
        Robot.init(hardwareMap, 0, 0, 0);

        Robot.moveRTP("backward", .4, 10.0, Robot, this, runtime);
        // first
        if (pos == 0) {
            Robot.moveRTP("left",.4,3.5 ,Robot ,this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("right",.4,2.5 ,Robot ,this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("right",.4,9.5 ,Robot ,this, runtime);
        }
        Robot.moveRTP("backward", .4, 20.0 ,Robot, this, runtime);
        Robot.leftGripper.setPosition(1);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("forward", .4, 12.0 ,Robot, this, runtime);
        if (pos == 0) {
            Robot.moveRTP("right", .4, 43.0 ,Robot, this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("right", .4, 35.0 ,Robot, this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("right", .4, 27.0 ,Robot, this, runtime);
        }

        Robot.leftGripper.setPosition(0);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("left", .4, 40.5,Robot,this,runtime);
        //second
        Robot.moveRTP("left", .4, 30.0 ,Robot, this, runtime);

        if (pos == 0) {
            Robot.moveRTP("right",.4,1 ,Robot ,this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("right",.4,9 ,Robot ,this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("right",.4,17 ,Robot ,this, runtime);
        }

        Robot.moveRTP("backward", .4, 18.0 ,Robot, this, runtime);
        Robot.leftGripper.setPosition(1);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("forward", .4, 12.0 ,Robot, this, runtime);
        if (pos == 0) {
            Robot.moveRTP("right", .4, 60.0 ,Robot, this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("right", .4, 52.0 ,Robot, this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("right", .4, 44.0 ,Robot, this, runtime);
        }

        Robot.leftGripper.setPosition(0);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }

    }
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.8;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

}
