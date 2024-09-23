package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

@Autonomous
public class WiringHarness extends RobotLinearOpMode{

    //Declaration of drive motors
    DcMotor leftMotor;
    DcMotor rightMotor;
    Servo leftServo;
    Servo rightServo;
    NormalizedColorSensor colorSensor;
    View relativeLayout;
    TouchSensor touchSensor;
    private DistanceSensor sensorDistance;

    public void runOpMode() {
        //intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");

        leftMotor.setDirection(DcMotorEx.Direction.FORWARD);
        rightMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

        touchSensor = hardwareMap.get(TouchSensor.class, "sensor_touch");

        sensorDistance = hardwareMap.get(DistanceSensor.class, "sensor_distance");

        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) sensorDistance;

//        telemetry.addData(">>", "Press start to continue");
//        telemetry.update();
//
//        waitForStart();
//
//        // while the OpMode is active, loop and read whether the sensor is being pressed.
//        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
//        while (opModeIsActive()) {
//
//            // send the info back to driver station using telemetry function.
//            if (touchSensor.isPressed()) {
//                telemetry.addData("Touch Sensor", "Is Pressed");
//            } else {
//                telemetry.addData("Touch Sensor", "Is Not Pressed");
//            }
//
//            // generic DistanceSensor methods.
//            telemetry.addData("deviceName", sensorDistance.getDeviceName() );
//            telemetry.addData("range", String.format("%.01f mm", sensorDistance.getDistance(DistanceUnit.MM)));
//            telemetry.addData("range", String.format("%.01f cm", sensorDistance.getDistance(DistanceUnit.CM)));
//            telemetry.addData("range", String.format("%.01f m", sensorDistance.getDistance(DistanceUnit.METER)));
//            telemetry.addData("range", String.format("%.01f in", sensorDistance.getDistance(DistanceUnit.INCH)));
//
//            // Rev2mDistanceSensor specific methods.
//            telemetry.addData("ID", String.format("%x", sensorTimeOfFlight.getModelID()));
//            telemetry.addData("did time out", Boolean.toString(sensorTimeOfFlight.didTimeoutOccur()));
//
//            telemetry.update();
//        }

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

//        try {
//            runSample(); // actually execute the sample
//        } finally {
//            // On the way out, *guarantee* that the background is reasonable. It doesn't actually start off
//            // as pure white, but it's too much work to dig out what actually was used, and this is good
//            // enough to at least make the screen reasonable again.
//            // Set the panel back to the default color
//            relativeLayout.post(new Runnable() {
//                public void run() {
//                    relativeLayout.setBackgroundColor(Color.WHITE);
//                }
//            });
//        }

        OpenCvInternalCamera phoneCam;
        SkystoneDeterminationPipelineRedFar pipeline;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        pipeline = new SkystoneDeterminationPipelineRedFar();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(320,240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });


        while (!isStarted() && !isStopRequested())
        {
            telemetry.addData("Realtime analysis", pipeline.getColor());
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        /*
         * The START command just came in: snapshot the current analysis now
         * for later use. We must do this because the analysis will continue
         * to change as the camera view changes once the robot starts moving!
         */
        if (pipeline.getAnalysis() == SkystoneDeterminationPipelineRedFar.SkystonePosition.CENTER) {
//            encoderDrive(.7, 37, MOVEMENT_DIRECTION.FORWARD);
//            purplePixelPlace();
//            sleep(450);
//
//            encoderDrive(.5, 6, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderTurn(.5, 130, TURN_DIRECTION.TURN_LEFT);
//
//
//
//            encoderDrive(.4, 25, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(.7, 2, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////
////            encoderDrive(.4, 9, MOVEMENT_DIRECTION.FORWARD);
////            encoderDrive(.4, 15, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            encoderDrive(.4, 10, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            sleep(12000);
////            encoderDrive(.7, 15, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            encoderDrive(.7, 17, MOVEMENT_DIRECTION.REVERSE);
//
//            encoderDrive(.4, 30, MOVEMENT_DIRECTION.REVERSE);
//            encoderDrive(.7, 63, MOVEMENT_DIRECTION.REVERSE);
//            sleep(1000); //Make longer as needed
//            encoderDrive(1, 12, MOVEMENT_DIRECTION.STRAFE_RIGHT);
//            encoderDrive(.7, 15, MOVEMENT_DIRECTION.REVERSE);
//            distSensorDrive(1, 3, MOVEMENT_DIRECTION.REVERSE);
//            yellowPixelPlace();
//            sleep(300);
//
//            encoderDrive(1, 3, MOVEMENT_DIRECTION.FORWARD);
//            encoderDrive(1, 13, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(.7, 10, MOVEMENT_DIRECTION.REVERSE);
//
//
//
//
//            motorKill();
        } else if (pipeline.getAnalysis() == SkystoneDeterminationPipelineRedFar.SkystonePosition.LEFT) {

//            encoderDrive(.7, 30, MOVEMENT_DIRECTION.FORWARD);
//            encoderDrive(.5, 8, MOVEMENT_DIRECTION.STRAFE_LEFT);
//
//
//            purplePixelPlace();
//            sleep(450);
//
//            encoderTurn(.5, 140, TURN_DIRECTION.TURN_LEFT);
//            encoderDrive(.5, 20, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(.5, 2, MOVEMENT_DIRECTION.STRAFE_RIGHT);
//            encoderDrive(.5, 48, MOVEMENT_DIRECTION.REVERSE);
//
//            encoderDrive(.7, 38, MOVEMENT_DIRECTION.REVERSE);
//            sleep(2000); //Make longer as needed
//            encoderDrive(1, 16, MOVEMENT_DIRECTION.STRAFE_RIGHT);
//            distSensorDrive(1, 3, MOVEMENT_DIRECTION.REVERSE);
//            yellowPixelPlace();
//            sleep(300);
////            encoderDrive(.3, 2, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            encoderDrive(.4, 18, MOVEMENT_DIRECTION.FORWARD);
////            encoderDrive(.4, 15, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            encoderDrive(.4, 18, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            sleep(9000);
////            encoderDrive(.7, 15, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            encoderDrive(.7, 15, MOVEMENT_DIRECTION.REVERSE);
////
//
////            distSensorDrive(.7, 3, MOVEMENT_DIRECTION.REVERSE);
////            encoderDrive(.5, 1.3, MOVEMENT_DIRECTION.STRAFE_RIGHT);
////            yellowPixelPlace();
////            sleep(400);
//
//            encoderDrive(1, 5, MOVEMENT_DIRECTION.FORWARD);
//            encoderDrive(1, 15, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(1, 15, MOVEMENT_DIRECTION.REVERSE);
//
//
//            motorKill();
        } else if (pipeline.getAnalysis() == SkystoneDeterminationPipelineRedFar.SkystonePosition.RIGHT) {

//            encoderDrive(.7, 25, MOVEMENT_DIRECTION.FORWARD);
//            encoderTurn(.5, 135, TURN_DIRECTION.TURN_LEFT);
//            encoderDrive(.7, 8, MOVEMENT_DIRECTION.REVERSE);
//
//            purplePixelPlace();
//            sleep(450);
//
//            encoderDrive(.7, 15, MOVEMENT_DIRECTION.FORWARD);
//            encoderDrive(.4, 20, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(.4, 2, MOVEMENT_DIRECTION.STRAFE_RIGHT);
//            encoderDrive(.4, 40, MOVEMENT_DIRECTION.REVERSE);
//
//            encoderDrive(.7, 40, MOVEMENT_DIRECTION.REVERSE);
//            sleep(1800);
//            encoderDrive(1, 10, MOVEMENT_DIRECTION.STRAFE_RIGHT);
//
//            distSensorDrive(1, 3, MOVEMENT_DIRECTION.REVERSE);
//            yellowPixelPlace();
//            sleep(300);
//            encoderDrive(1, 5, MOVEMENT_DIRECTION.FORWARD);
//            encoderDrive(1, 9, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(.7, 10, MOVEMENT_DIRECTION.REVERSE);
//
//            motorKill();

        } else {
//            encoderDrive(.6, 50, MOVEMENT_DIRECTION.FORWARD);
//            encoderDrive(.6, 60, MOVEMENT_DIRECTION.STRAFE_RIGHT);
//
//
//            motorKill();

        }
        stop();

    }

    public void runSample() {
        // You can give the sensor a gain value, will be multiplied by the sensor's raw value before the
        // normalized color values are calculated. Color sensors (especially the REV Color Sensor V3)
        // can give very low values (depending on the lighting conditions), which only use a small part
        // of the 0-1 range that is available for the red, green, and blue values. In brighter conditions,
        // you should use a smaller gain than in dark conditions. If your gain is too high, all of the
        // colors will report at or near 1, and you won't be able to determine what color you are
        // actually looking at. For this reason, it's better to err on the side of a lower gain
        // (but always greater than  or equal to 1).
        float gain = 2;

        // Once per loop, we will update this hsvValues array. The first element (0) will contain the
        // hue, the second element (1) will contain the saturation, and the third element (2) will
        // contain the value. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
        // for an explanation of HSV color.
        final float[] hsvValues = new float[3];

        // xButtonPreviouslyPressed and xButtonCurrentlyPressed keep track of the previous and current
        // state of the X button on the gamepad
        boolean xButtonPreviouslyPressed = false;
        boolean xButtonCurrentlyPressed = false;

        // Get a reference to our sensor object. It's recommended to use NormalizedColorSensor over
        // ColorSensor, because NormalizedColorSensor consistently gives values between 0 and 1, while
        // the values you get from ColorSensor are dependent on the specific sensor you're using.

        // If possible, turn the light on in the beginning (it might already be on anyway,
        // we just make sure it is if we can).
        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(true);
        }

        // Wait for the start button to be pressed.
        waitForStart();

        // Loop until we are asked to stop
        while (opModeIsActive()) {
            // Explain basic gain information via telemetry
            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

            // Update the gain value if either of the A or B gamepad buttons is being held
            if (gamepad1.a) {
                // Only increase the gain by a small amount, since this loop will occur multiple times per second.
                gain += 0.005;
            } else if (gamepad1.b && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
                gain -= 0.005;
            }

            // Show the gain value via telemetry
            telemetry.addData("Gain", gain);

            // Tell the sensor our desired gain value (normally you would do this during initialization,
            // not during the loop)
            colorSensor.setGain(gain);

            // Check the status of the X button on the gamepad
            xButtonCurrentlyPressed = gamepad1.x;

            // If the button state is different than what it was, then act
            if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
                // If the button is (now) down, then toggle the light
                if (xButtonCurrentlyPressed) {
                    if (colorSensor instanceof SwitchableLight) {
                        SwitchableLight light = (SwitchableLight)colorSensor;
                        light.enableLight(!light.isLightOn());
                    }
                }
            }
            xButtonPreviouslyPressed = xButtonCurrentlyPressed;

            // Get the normalized colors from the sensor
            NormalizedRGBA colors = colorSensor.getNormalizedColors();

            /* Use telemetry to display feedback on the driver station. We show the red, green, and blue
             * normalized values from the sensor (in the range of 0 to 1), as well as the equivalent
             * HSV (hue, saturation and value) values. See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html
             * for an explanation of HSV color. */

            // Update the hsvValues array by passing it to Color.colorToHSV()
            Color.colorToHSV(colors.toColor(), hsvValues);

            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", colors.alpha);

            /* If this color sensor also has a distance sensor, display the measured distance.
             * Note that the reported distance is only useful at very close range, and is impacted by
             * ambient light and surface reflectivity. */
            if (colorSensor instanceof DistanceSensor) {
                telemetry.addData("Distance (cm)", "%.3f", ((DistanceSensor) colorSensor).getDistance(DistanceUnit.CM));
            }

            telemetry.update();

            // Change the Robot Controller's background color to match the color detected by the color sensor.
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(hsvValues));
                }
            });
        }
    }
}


class SkystoneDeterminationPipelineRedFar extends OpenCvPipeline {
    /*
     * An enum to define the skystone position
     */
    public enum SkystonePosition {
        LEFT,
        CENTER,
        RIGHT
    }

    /*
     * Some color constants
     */
    static final Scalar BLUE = new Scalar(0, 0, 255);
    static final Scalar GREEN = new Scalar(0, 255, 0);

    /*
     * The core values which define the location and size of the sample regions
     */
    static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(0, 60);
    static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(107, 60);
    static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(213, 60);
    static final int REGION_WIDTH = 106;
    static final int REGION_HEIGHT = 160;

    /*
     * Points which actually define the sample region rectangles, derived from above values
     *
     * Example of how points A and B work to define a rectangle
     *
     *   ------------------------------------
     *   | (0,0) Point A                    |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                                  |
     *   |                  Point B (70,50) |
     *   ------------------------------------
     *
     */
    Point region1_pointA = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y);
    Point region1_pointB = new Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
    Point region2_pointA = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x,
            REGION2_TOPLEFT_ANCHOR_POINT.y);
    Point region2_pointB = new Point(
            REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
    Point region3_pointA = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x,
            REGION3_TOPLEFT_ANCHOR_POINT.y);
    Point region3_pointB = new Point(
            REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    /*
     * Working variables
     */
    Mat region1_Cb, region2_Cb, region3_Cb;
    Mat YCrCb = new Mat();
    Mat Cb = new Mat();
    Mat Cr = new Mat();

    int avg1, avg2, avg3;

    // Volatile since accessed by OpMode thread w/o synchronization
    private volatile SkystonePosition position = SkystonePosition.RIGHT;
    private volatile int colorLookingAt = -1;

    /*
     * This function takes the RGB frame, converts to YCrCb,
     * and extracts the Cb channel to the 'Cb' variable
     */
    void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 1);
    }

    @Override
    public void init(Mat firstFrame) {

        /*
         * We need to call this in order to make sure the 'Cb'
         * object is initialized, so that the submats we make
         * will still be linked to it on subsequent frames. (If
         * the object were to only be initialized in processFrame,
         * then the submats would become delinked because the backing
         * buffer would be re-allocated the first time a real frame
         * was crunched)
         */
        inputToCb(firstFrame);

        /*
         * Submats are a persistent reference to a region of the parent
         * buffer. Any changes to the child affect the parent, and the
         * reverse also holds true.
         */
        region1_Cb = Cr.submat(new Rect(region1_pointA, region1_pointB));
        region2_Cb = Cr.submat(new Rect(region2_pointA, region2_pointB));
        region3_Cb = Cr.submat(new Rect(region3_pointA, region3_pointB));
    }

    @Override
    public Mat processFrame(Mat input) {

        /* This is the code for color detection */
        colorLookingAt = ColorSense.get_color_of_brick(input);
        return input;
    }

    /*
     * Call this from the OpMode thread to obtain the latest analysis
     */
    public SkystonePosition getAnalysis() {
        return position;
    }
    public int getColor() {return colorLookingAt;}
}
class ColorSense {
    static final int HSV_MAX = 180;
    static final int COLOR_RECOGNITION_THRESHOLD = 50;
    static final int[][] RANGES = {{0, 360}, {10, 60}, {105, 135}};
    /*
     * 0: Red
     * 1: Yellow
     * 2: Blue
     */

    // HSV values are 1 to 180, so if you have an interval like (170, 10)
    // you have to change it to (170, 180), (0, 10)
    // otherwise just return the interval
    public static int[][] get_ivals(int[] colorRange){
        int[][] ivals = new int[2][2];
        if (colorRange[0] > colorRange[1]){
            // case like (170, 10)
            ivals[0][0] = colorRange[0];
            ivals[0][1] = HSV_MAX;

            ivals[1][0] = 0;
            ivals[1][1] = colorRange[1];
        }
        else {
            // normal case
            ivals[0][0] = colorRange[0];
            ivals[0][1] = colorRange[1];
        }
        return ivals;
    }

    private static int get_color(double[] HSV){
        /*
         * I intend HSV[0] to be HUE, [1] to be SAT, and [2] to be VAL.
         *
         * If returned 0, then blue
         * If returned 1, then red
         * If returned 2, then yellow
         * If none found, return 3
         */

        // too dark / too unsaturated
        if (!(50 <= HSV[1] && HSV[1] <= 255 && 50 <= HSV[2] && HSV[2] <= 255)){
            return 3;
        }
        // iterate through all colors
        for (int color = 0; color < 3; color++){
            int[][] ival = get_ivals(RANGES[color]);

            for (int[] ints : ival) {
                if (ints[0] != 0 || ints[1] != 0) {
                    // inside color range
                    if (ints[0] <= HSV[0] && HSV[0] <= ints[1]) {
                        return color;
                    }
                }
            }
        }
        // none were in range
        return 3;
    }

    private static double max(double[] values){
        // Given that all values
        double maxVal = Double.MIN_VALUE;

        for (double val : values) {
            if (val > maxVal){
                maxVal = val;
            }
        }
        return maxVal;
    }

    private static double min(double[] values){
        // Given that all values
        double minVal = Double.MAX_VALUE;

        for (double val : values) {
            if (val < minVal){
                minVal = val;
            }
        }
        return minVal;
    }

    // public static int[] convert_rgb_to_hsv(int[] RGB){
    //     /* In this function, we assume that RGB is an int array with ranges 0-255,
    //     * where index 0 is red, 1 is green, 2 is blue.
    //     * We first convert the RGB value to a percentage between 0 and 1.
    //     */
    //     double[] RGBPercents = {RGB[0] / 255.0, RGB[1] / 255.0, RGB[2] / 255.0};
    //     double CMAX = max(RGBPercents);
    //     double CMIN = min(RGBPercents);

    //     double delta = CMAX - CMIN;

    //     int[] HSV = new int[3];

    //     // This first chunk of code is to get the Hue value.
    //     if (delta == 0){
    //         HSV[0] = 0;
    //     }
    //     else if (CMAX == RGBPercents[0]){
    //         HSV[0] = Math.toIntExact(Math.round(60 * (((RGBPercents[1] - RGBPercents[2]) / delta) % 6)));
    //     }
    //     else if (CMAX == RGBPercents[1]){
    //         HSV[0] = Math.toIntExact(Math.round(60 * ((RGBPercents[2] - RGBPercents[0]) / delta + 2)));
    //     }
    //     else if (CMAX == RGBPercents[2]){
    //         HSV[0] = Math.toIntExact(Math.round(60 * ((RGBPercents[0] - RGBPercents[1]) / delta + 4)));
    //     }

    //     // This code gets the Saturation.
    //     if (CMAX == 0){
    //         HSV[1] = 0;
    //     }
    //     else {
    //         HSV[1] = Math.toIntExact(Math.round(delta / CMAX));
    //     }

    //     // Value is simply CMAX
    //     HSV[2] = Math.toIntExact(Math.round(CMAX));

    //     return HSV;
    // }

    public static int get_color_of_brick(Pixel[] frame){
        // get count of each pixel
        int[] count = new int[4];
        /*
         * Edwin uses a dictionary here. Java has no such dictionary as I'm aware
         * of, so I'm going to instead omit the "key" part of the array.
         * count[0] is for blue, count[1] is for red, count[2] is for yellow
         * similarly to how the get_color function output is coded.
         * count[4] is for no color found
         * If returned 0, then blue
         * If returned 1, then red
         * If returned 2, then yellow
         * If none found, return 3
         */
        double total_pixels = frame.length;
        for (Pixel pixel : frame){
            for(double x: pixel.getHSV()) {
                System.out.print(x);
            }
            System.out.println();

            count[get_color(pixel.getHSV())]++;
        }

        // if a color passes the threshold we are confident in the fact that there is a correct color brick underneath us

        for (int color = 0; color < 3; color++){
            if (count[color] / total_pixels > COLOR_RECOGNITION_THRESHOLD){
                return color;
            }
        }
        /* This return statement might be unnecessary.
         * Because, in the previous for loop, if the majority of pixels have no color
         * found, then it will pass the recognition threshold and return no color.
         * Leaving it in though, because I am a faithful translator
         */
        return 3;
    }

    public static int get_color_of_brick(Mat input){
        return get_color_of_brick(convertMatToPixel(input));
    }

    private static Pixel[] convertMatToPixel(Mat mat){
        int rows = mat.rows();
        int cols = mat.cols();

        Mat HSVs = new Mat();

        Imgproc.cvtColor(mat, HSVs, Imgproc.COLOR_RGB2HSV);

        Pixel[] pixels = new Pixel[rows * cols];

        int counter = 0;
        for (int row = 0; row < rows; row++){
            for (int col = 0; col < cols; col++){
                /* the mat is going to be called firstFrame and is in init.
                 * Assume type inside of HSVs array is double[] */
                pixels[counter++] = new Pixel(mat.get(row, col));
            }
        }
        return pixels;
    }

}
class Pixel {
    private double[] HSV = new double[3];

    public Pixel(){

    }
    public Pixel(double hue, double sat, double val){
        HSV[0] = hue;
        HSV[1] = sat;
        HSV[2] = val;
    }

    public Pixel(double[] HSV){
        if (HSV.length == 3){
            this.HSV = HSV;
        }
        else{
            this.HSV[0] = HSV[0];
            this.HSV[1] = HSV[1];
            this.HSV[2] = HSV[2];
        }
    }


    public double[] getHSV(){
        return HSV;
    }
}