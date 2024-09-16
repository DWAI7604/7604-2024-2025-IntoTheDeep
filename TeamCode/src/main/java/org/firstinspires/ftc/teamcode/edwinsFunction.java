package it.polito.elite.teaching.cv;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;

class edWinFunctions {
    static final int HSV_MAX = 180;
    int[][] RANGES = {{170, 10}, {10, 60}, {105, 135}};
    // int[][] RANGES = new int[3][2];
    // RANGES[0][0] = 170;
    // RANGES[0][1] = 10;
    // RANGES[1][0] = 10;
    // RANGES[1][1] = 60;
    // RANGES[2][0] = 105;
    // RANGES[2][1] = 135;
    /*
     * 0: Red
     * 1: Yellow
     * 2: Blue
     */

    public edWinFunctions(){
        
    }

    public static int[][] get_ivals(int[] colorRange){
        int[][] ivals = new int[2][2];
        if (colorRange[0] > colorRange[1]){
            ivals[0][0] = colorRange[0];
            ivals[0][1] = HSV_MAX;

            ivals[1][0] = 0;
            ivals[1][1] = colorRange[1];
        }
        else {
            ivals[0][0] = colorRange[0];
            ivals[0][1] = colorRange[1];
        }
        return ivals;
    }

    public static int[][] transform_to_edwinArr(int[] ival){
        int[][] mn_and_mx = new int[2][3];
        int[] mn = {ival[0], 25, 25};
        int[] mx = {ival[1], 255, 255};
        mn_and_mx[0] = mn;
        mn_and_mx[1] = mx;

        return mn_and_mx;
    }

    
}