package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.vuforia.Frame;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;

@Autonomous(name = "Bitmap tracker", group = "Sensor")
public class BitmapTracker extends LinearOpMode {
    final boolean diaplayMonitor = true;
    final boolean useWebcam = true;
    boolean awaitingCapture = false;


    VuforiaLocalizer vuforia;

    WebcamName webcamName;

    @Override public void runOpMode() {
        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = null;
        if (diaplayMonitor) {
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        } else {
            parameters = new VuforiaLocalizer.Parameters();
        }

        parameters.vuforiaLicenseKey = "AdCuaEX/////AAABmXYJgRHZxkB9gj+81cIaX+JZm4W2w3Ee2HhKucJINnuXQ8l214BoCiyEk04zmQ/1VPvo+8PY7Um3eI5rI4WnSJmEXo7jyMz2WZDkkRnA88uBCtbml8CsMSIS7J3aUcgVd9P8ocLLgwqpavhEEaUixEx/16rgzIEtuHcq5ghQzzCkqR1xvAaxnx5lWM+ixf6hBCfZEnaiUM7WjD4gflO55IpoO/CdCWQrGUw2LuUKW2J+4K6ftKwJ+B1Qdy7pt2tDrGZvMyB4AcphPuoJRCSr5NgRoNWZ+WH5LqAdzYEO0Bv7C9LeSgmSPPT7GPPDpjv6+3DO5BE6l+2uMYQQbuF11BWKKq5Xp+D5Y6l2+W97zpgP";

        if (useWebcam) {
            parameters.cameraName = webcamName;
        } else {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        }


        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.enableConvertFrameToBitmap();

        boolean buttonPressed = false;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a && !buttonPressed) {
                findBorder();
            }
            buttonPressed = gamepad1.a;

            if (gamepad1.b && !awaitingCapture) {
                findBorder();
            }
        }



    }
    void findBorder () {
        vuforia.getFrameOnce(Continuation.create(ThreadPool.getDefault(), new Consumer<Frame>()
        {
            @Override public void accept(Frame frame)
            {
                Bitmap bitmap = vuforia.convertFrameToBitmap(frame);
                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int color = bitmap.getPixel(width/2,height/2);
                    telemetry.addData("height: ",height);
                    telemetry.addData("width: ",width);
                    telemetry.addData("color int: ", color);
                    telemetry.addData("red: ", Color.red(color));
                    telemetry.addData("green: ", Color.green(color));
                    telemetry.addData("blue: ", Color.blue(color));
                    telemetry.update();
                    boolean done = false;
                    int x = width / 2;
                    int y = height / 2;
                    int lastRed = Color.red(bitmap.getPixel(x,y));
                    while (!done) {
                        int pixel = bitmap.getPixel(x,y);
                        int red = Color.red(pixel);
                        if (red < 50) {
                            if (lastRed > 50) {
                                done = true;
                            } else {
                                x++;
                            }
                        } else {
                            if (lastRed < 50) {
                                done = true;
                            } else {
                                x--;
                            }

                        }
                        if (x < 0 || x > width)
                            done = true;
                    }
                    telemetry.addData("border pos",x);
                } else {
                    telemetry.addData("no image","");
                    telemetry.update();

                }
                awaitingCapture = false;
            }
        }));
    };
}
