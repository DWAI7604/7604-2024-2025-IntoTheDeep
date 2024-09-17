package org.firstinspires.ftc.teamcode;

class ColorSense {
    static final int HSV_MAX = 180;
    static final int COLOR_RECOGNITION_THRESHOLD = 80;
    static final int[][] RANGES = {{170, 10}, {10, 60}, {105, 135}};

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

            for (int ivalIterator = 0; ivalIterator < ival.length; ivalIterator++){
                if (ival[ivalIterator][0] != 0 || ival[ivalIterator][1] != 0){
                    // inside color range
                    if (ival[ivalIterator][0] <= HSV[0] && HSV[0] <= ival[ivalIterator][1]){
                        return color;
                    }
                }
            }
        }
        // none were in range
        return 3;
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
    
}