package org.firstinspires.ftc.teamcode;

public class Pixel {
    private int[] HSV = new int[3];

    public Pixel(){

    }
    public Pixel(int hue, int sat, int val){
        HSV[0] = hue;
        HSV[1] = sat;
        HSV[2] = val;
    }

    public int[] getHSV(){
        return HSV;
    }
}
