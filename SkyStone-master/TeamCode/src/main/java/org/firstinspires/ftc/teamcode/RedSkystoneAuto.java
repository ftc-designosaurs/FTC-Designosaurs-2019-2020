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

@Autonomous(name="Red Skystone", group="Mechanum")
public class RedSkystoneAuto extends LinearOpMode{

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
        /*initVuforia();

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
                double highestAvg = Double.NEGATIVE_INFINITY;
                double currentAvg = 0;
                for (Recognition recognition : updatedRecognitions) {
                    telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            recognition.getLeft(), recognition.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            recognition.getRight(), recognition.getBottom());

                    if (recognition.getLabel() == "Skystone") {
                        currentAvg = (recognition.getLeft() + recognition.getRight()) / 2;
                        if (currentAvg > highestAvg) {
                            highestAvg = currentAvg;
                        }
                    }


                }
                telemetry.addData("middle of sky", highestAvg);
                if (highestAvg < 284.5) {
                    telemetry.addData("Target pos", "Left");
                    pos = 2;
                } else if (highestAvg > 284.5 && highestAvg < 348.25) {
                    telemetry.addData("Target pos", "Middle");
                    pos = 1;
                } else if (highestAvg > 348.25) {
                    telemetry.addData("Target pos", "Right");
                    pos = 0;
                }
                telemetry.update();
            }


            if (tfod != null) {
                tfod.shutdown();
            }

        }*/
        waitForStart();
        pos = 1;
        Robot.init(hardwareMap, 0, 0, 0);

        Robot.moveRTP("backward", 1, 10.0, Robot, this, runtime);
        // first
        if (pos == 0) {
            Robot.moveRTP("right",1,1.5 ,Robot ,this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("left",1,4.5 ,Robot ,this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("left",1,12.5 ,Robot ,this, runtime);
        }
        Robot.moveRTP("backward", .4, 17.0 ,Robot, this, runtime);
        Robot.rightGripper.setPosition(0.02);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("forward", 1, 9.0 ,Robot, this, runtime);
        if (pos == 0) {
            Robot.moveRTP("left", .5, 43.0 ,Robot, this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("left", .5, 37.0 ,Robot, this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("left", .5, 32.5 ,Robot, this, runtime);
        }

        Robot.rightGripper.setPosition(1);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        //Robot.moveRTP("right", 1, 67 ,Robot,this,runtime);
        //second

        //Robot.moveRTP("right", .3, 25.0 ,Robot, this, runtime);

        if (pos == 0) {
            Robot.moveRTP("right",1,62 - 1 ,Robot ,this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("right",1,62- 7 ,Robot ,this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("right",1,62 - 12 ,Robot ,this, runtime);
        }

        Robot.moveRTP("backward", .2, 9 ,Robot, this, runtime);
        Robot.rightGripper.setPosition(0.02);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("forward", 1, 10.0 ,Robot, this, runtime);
        if (pos == 0) {
            Robot.moveRTP("left", .5, 60.0 ,Robot, this, runtime);
        } else if (pos == 1) {
            Robot.moveRTP("left", .5, 54.0 ,Robot, this, runtime);
        } else if (pos == 2) {
            Robot.moveRTP("left", .5, 48.0 ,Robot, this, runtime);
        }
        Robot.moveRTP("backward", 1, 1 ,Robot, this, runtime);
        Robot.rightGripper.setPosition(1);
        runtime.reset();
        while (runtime.time(TimeUnit.MILLISECONDS) < 500 && opModeIsActive()) {
            telemetry.addData("time elapsed", runtime.time(TimeUnit.MILLISECONDS));
            telemetry.update();
        }
        Robot.moveRTP("right", 1, 10 ,Robot, this, runtime);

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
