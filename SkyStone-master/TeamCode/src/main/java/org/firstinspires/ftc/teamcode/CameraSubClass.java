package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ThreadPool;
import com.vuforia.Frame;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class CameraSubClass {
    final boolean diaplayMonitor = true;
    final boolean useWebcam = true;
    boolean awaitingCapture = false;
    int targetX = 380;
    int borderX = 0;

    VuforiaLocalizer vuforia;

    WebcamName webcamName;

    void init(HardwareMap hardwareMap) {
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
    }

    void loop() {
        if (!awaitingCapture) {
            findBorder();
            awaitingCapture = true;
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
                                x--;
                            }
                        } else {
                            if (lastRed < 50) {
                                done = true;
                            } else {
                                x++;
                            }

                        }
                        if (x <= 0 || x >= width)
                            done = true;
                    }
                    borderX = x;
                }
                awaitingCapture = false;
            }
        }));
    };

    int getBorderX() {
        return borderX;
    }

}
