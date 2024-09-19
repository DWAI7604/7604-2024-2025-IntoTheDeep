package org.firstinspires.ftc.teamcode;

public class Pixel {
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
