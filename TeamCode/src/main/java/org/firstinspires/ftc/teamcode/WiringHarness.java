package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.LED;
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
            telemetry.addData("Realtime analysis", pipeline.getAnalysis());
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


class SkystoneDeterminationPipelineRedFar extends OpenCvPipeline
{
    /*
     * An enum to define the skystone position
     */
    public enum SkystonePosition
    {
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
    static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(0,60);
    static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(107,60);
    static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(213,60);
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

    /*
     * This function takes the RGB frame, converts to YCrCb,
     * and extracts the Cb channel to the 'Cb' variable
     */
    void inputToCb(Mat input)
    {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        Core.extractChannel(YCrCb, Cr, 1);
    }

    @Override
    public void init(Mat firstFrame)
    {
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
    public Mat processFrame(Mat input)
    {
        /*
         * Overview of what we're doing:
         *
         * We first convert to YCrCb color space, from RGB color space.
         * Why do we do this? Well, in the RGB color space, chroma and
         * luma are intertwined. In YCrCb, chroma and luma are separated.
         * YCrCb is a 3-channel color space, just like RGB. YCrCb's 3 channels
         * are Y, the luma channel (which essentially just a B&W image), the
         * Cr channel, which records the difference from red, and the Cb channel,
         * which records the difference from blue. Because chroma and luma are
         * not related in YCrCb, vision code written to look for certain values
         * in the Cr/Cb channels will not be severely affected by differing
         * light intensity, since that difference would most likely just be
         * reflected in the Y channel.
         *
         * After we've converted to YCrCb, we extract just the 2nd channel, the
         * Cb channel. We do this because stones are bright yellow and contrast
         * STRONGLY on the Cb channel against everything else, including SkyStones
         * (because SkyStones have a black label).
         *
         * We then take the average pixel value of 3 different regions on that Cb
         * channel, one positioned over each stone. The brightest of the 3 regions
         * is where we assume the SkyStone to be, since the normal stones show up
         * extremely darkly.
         *
         * We also draw rectangles on the screen showing where the sample regions
         * are, as well as drawing a solid rectangle over top the sample region
         * we believe is on top of the SkyStone.
         *
         * In order for this whole process to work correctly, each sample region
         * should be positioned in the center of each of the first 3 stones, and
         * be small enough such that only the stone is sampled, and not any of the
         * surroundings.
         */

        /*
         * Get the Cb channel of the input frame after conversion to YCrCb
         */
        inputToCb(input);

        /*
         * Compute the average pixel value of each submat region. We're
         * taking the average of a single channel buffer, so the value
         * we need is at index 0. We could have also taken the average
         * pixel value of the 3-channel image, and referenced the value
         * at index 2 here.
         */
        avg1 = (int) Core.mean(region1_Cb).val[0];
        avg2 = (int) Core.mean(region2_Cb).val[0];
        avg3 = (int) Core.mean(region3_Cb).val[0];

        /*
         * Draw a rectangle showing sample region 1 on the screen.
         * Simply a visual aid. Serves no functional purpose.
         */
        Imgproc.rectangle(
                input, // Buffer to draw on
                region1_pointA, // First point which defines the rectangle
                region1_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines

        /*
         * Draw a rectangle showing sample region 2 on the screen.
         * Simply a visual aid. Serves no functional purpose.
         */
        Imgproc.rectangle(
                input, // Buffer to draw on
                region2_pointA, // First point which defines the rectangle
                region2_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines

        /*
         * Draw a rectangle showing sample region 3 on the screen.
         * Simply a visual aid. Serves no functional purpose.
         */
        Imgproc.rectangle(
                input, // Buffer to draw on
                region3_pointA, // First point which defines the rectangle
                region3_pointB, // Second point which defines the rectangle
                BLUE, // The color the rectangle is drawn in
                2); // Thickness of the rectangle lines


        /*
         * Find the max of the 3 averages
         */
        int minOneTwo = Math.max(avg1, avg2);
        int min = Math.max(minOneTwo, avg3);

        /*
         * Now that we found the max, we actually need to go and
         * figure out which sample region that value was from
         */
        if(min == avg1) // Was it from region 1?
        {
            position = SkystonePosition.LEFT; // Record our analysis

            /*
             * Draw a solid rectangle on top of the chosen region.
             * Simply a visual aid. Serves no functional purpose.
             */
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        }
        else if(min == avg2) // Was it from region 2?
        {
            position = SkystonePosition.CENTER; // Record our analysis

            /*
             * Draw a solid rectangle on top of the chosen region.
             * Simply a visual aid. Serves no functional purpose.
             */
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        }
        else if(min == avg3) // Was it from region 3?
        {
            position = SkystonePosition.RIGHT; // Record our analysis

            /*
             * Draw a solid rectangle on top of the chosen region.
             * Simply a visual aid. Serves no functional purpose.
             */
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region3_pointA, // First point which defines the rectangle
                    region3_pointB, // Second point which defines the rectangle
                    GREEN, // The color the rectangle is drawn in
                    -1); // Negative thickness means solid fill
        }

        /*
         * Render the 'input' buffer to the viewport. But note this is not
         * simply rendering the raw camera feed, because we called functions
         * to add some annotations to this buffer earlier up.
         */
        return input;
    }

    /*
     * Call this from the OpMode thread to obtain the latest analysis
     */
    public SkystonePosition getAnalysis()
    {
        return position;
    }
}


