package org.firstinspires.ftc.teamcode;

import java.lang.Math;
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

class ColorSense {
    static final int HSV_MAX = 180;
    static final int COLOR_RECOGNITION_THRESHOLD = 80;
    static final int[][] RANGES = {{170, 10}, {10, 60}, {105, 135}};

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

    public static int get_color(int[] HSV){
        /*
         * I intend HSV[0] to be HUE, [1] to be SAT, and [2] to be VAL.
         * 
         * If returned 0, then blue
         * If returned 1, then red
         * If returned 2, then yellow
         * If none found, return 3
         */

        // too dark / too unsaturated
        if (!(50 <= HSV[1] || HSV[2] <= 255)){
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

    public static double max(double[] values){
        // Given that all values
        double maxVal = Double.MIN_VALUE;

        for (double val : values) {
            if (val > maxVal){
                maxVal = val;
            }
        }
        return maxVal;
    }

    public static double min(double[] values){
        // Given that all values
        double minVal = Double.MAX_VALUE;

        for (double val : values) {
            if (val < minVal){
                minVal = val;
            }
        }
        return minVal;
    }

    public static int[] convert_rgb_to_hsv(int[] RGB){
        /* In this function, we assume that RGB is an int array with ranges 0-255,
        * where index 0 is red, 1 is green, 2 is blue.
        * We first convert the RGB value to a percentage between 0 and 1.
        */
        double[] RGBPercents = {RGB[0] / 255.0, RGB[1] / 255.0, RGB[2] / 255.0};
        double CMAX = max(RGBPercents);
        double CMIN = min(RGBPercents);

        double delta = CMAX - CMIN;

        int[] HSV = new int[3];

        // This first chunk of code is to get the Hue value.
        if (delta == 0){
            HSV[0] = 0;
        }
        else if (CMAX == RGBPercents[0]){
            HSV[0] = Math.toIntExact(Math.round(60 * (((RGBPercents[1] - RGBPercents[2]) / delta) % 6)));
        }
        else if (CMAX == RGBPercents[1]){
            HSV[0] = Math.toIntExact(Math.round(60 * ((RGBPercents[2] - RGBPercents[0]) / delta + 2)));
        }
        else if (CMAX == RGBPercents[2]){
            HSV[0] = Math.toIntExact(Math.round(60 * ((RGBPercents[0] - RGBPercents[1]) / delta + 4)));
        }

        // This code gets the Saturation.
        if (CMAX == 0){
            HSV[1] = 0;
        }
        else {
            HSV[1] = Math.toIntExact(Math.round(delta / CMAX));
        }

        // Value is simply CMAX
        HSV[2] = Math.toIntExact(Math.round(CMAX));

        return HSV;
    }

    public static int get_color_of_brick(Pixel[] frame){
        // get count of each pixel
        int[] count = new int[4];
        /* 
         * Edwin uses a dictionary here. Java has no such dictionary as I'm aware
         * of, so I'm going to instead omit the "key" part of the array.
         * count[0] is for blue, count[1] is for red, count[2] is for yellow
         * similarly to how the get_color function output is coded.
         * count[4] is for no color found
         */
        double total_pixels = frame.length;
        for (Pixel pixel : frame){
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

    public static Pixel[] convertMat(Mat mat){
        int rows = mat.rows();
        int cols = mat.cols();

        mat.get(0, 0);

//        for (int row = 0; row < rows; row++){
//            for (int col = 0; col < cols; col++){
//                /* Finish this function. the mat is going to be called firstFrame and is in init.
//                * The type inside of firstFrame is  */
//
//            }
//        }
    }
}