package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class AutoControl {

    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;

    private DcMotor horizontalDrive;
    private DcMotor leftVertDrive;
    private DcMotor rightVertDrive;
    private int tick2cm;
    int targetLeftPosition;
    int targetHorizontalPosition;

    int targetRightPosition;

    int initialLeftPosition;
    int initialRightPosition;
    int initialHorizontalPosition;

    private ElapsedTime runtime;

    int clamp(int x, int lower, int higher) {
        x = Math.max(x, lower);
        x = Math.min(x, higher);
        return x;
    }

    // Constructor to initialize motors and elapsed time
    public AutoControl(DcMotor leftFrontDrive, DcMotor leftBackDrive, DcMotor rightFrontDrive, DcMotor rightBackDrive,
                       DcMotor horizontalDrive, DcMotor leftVertDrive, DcMotor rightVertDrive) {
        this.leftFrontDrive = leftFrontDrive;
        this.leftBackDrive = leftBackDrive;
        this.rightFrontDrive = rightFrontDrive;
        this.rightBackDrive = rightBackDrive;
        this.horizontalDrive = horizontalDrive;
        this.leftVertDrive = leftVertDrive;
        this.rightVertDrive = rightVertDrive;
        tick2cm = 15;
        targetLeftPosition = leftVertDrive.getCurrentPosition();
        targetHorizontalPosition = horizontalDrive.getCurrentPosition();
        targetRightPosition = rightVertDrive.getCurrentPosition();

        initialLeftPosition = leftVertDrive.getCurrentPosition();
        initialRightPosition = rightVertDrive.getCurrentPosition();
        initialHorizontalPosition = horizontalDrive.getCurrentPosition();

        this.runtime = new ElapsedTime();
    }





    // Method to drive forward by a specific number of ticks
    public void autoDriveForward(int ticks) {

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + ticks);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() + ticks);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + ticks);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() + ticks);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power to move
        leftFrontDrive.setPower(0.6);
        rightFrontDrive.setPower(0.6);
        leftBackDrive.setPower(0.6);
        rightBackDrive.setPower(0.6);

        // Wait until robot reaches target position
        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() || leftBackDrive.isBusy() || rightBackDrive.isBusy()) {
            // Optionally, update telemetry or handle other logic here
        }

        // Stop motors after movement
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    // Method to drive backward by a specific number of ticks
    public void autoDriveBackward(int ticks) {
        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() - ticks);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() - ticks);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() - ticks);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() - ticks);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power to move backward
        leftFrontDrive.setPower(0.6);
        rightFrontDrive.setPower(0.6);
        leftBackDrive.setPower(0.6);
        rightBackDrive.setPower(0.6);

        // Wait until robot reaches target position
        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() || leftBackDrive.isBusy() || rightBackDrive.isBusy()) {
            // Optionally, update telemetry or handle other logic here
        }

        // Stop motors after movement
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    public void moveSliderUp(int ticks) {

        targetLeftPosition = clamp(targetLeftPosition, initialLeftPosition-5000, initialLeftPosition+5000);
        targetRightPosition = clamp(targetRightPosition, initialRightPosition-5000, initialRightPosition+5000);
        targetHorizontalPosition = clamp(targetHorizontalPosition, initialHorizontalPosition-5000, initialHorizontalPosition+5000);
        // Reset encoders for consistent behavior
        leftVertDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightVertDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Set target position
        targetRightPosition+=ticks;
        targetLeftPosition-=ticks;

        leftVertDrive.setTargetPosition(targetLeftPosition);
        rightVertDrive.setTargetPosition(targetRightPosition);

        leftVertDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightVertDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power to move the slider
        leftVertDrive.setPower(0.6);
        rightVertDrive.setPower(0.6);

        leftVertDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightVertDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait until the slider reaches the target position
        while (leftVertDrive.isBusy() || rightVertDrive.isBusy()) {
            // Optionally add telemetry or other feedback
        }

        // Stop motors after movement
        //leftVertDrive.setPower(0);
        //rightVertDrive.setPower(0);
    }


    // Method to spin the robot 180 degrees
    public void autoSpin180() {
        // Assuming that 180 degrees is equivalent to a certain number of ticks
        // (Adjust the number of ticks as per your robot's drive system and wheel size)
        int ticksFor180 = 1120; // Example value, adjust this as necessary for your robot

        leftFrontDrive.setTargetPosition(leftFrontDrive.getCurrentPosition() + ticksFor180);
        rightFrontDrive.setTargetPosition(rightFrontDrive.getCurrentPosition() - ticksFor180);
        leftBackDrive.setTargetPosition(leftBackDrive.getCurrentPosition() + ticksFor180);
        rightBackDrive.setTargetPosition(rightBackDrive.getCurrentPosition() - ticksFor180);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power to spin
        leftFrontDrive.setPower(0.6);
        rightFrontDrive.setPower(0.6);
        leftBackDrive.setPower(0.6);
        rightBackDrive.setPower(0.6);

        // Wait until robot reaches target position
        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() || leftBackDrive.isBusy() || rightBackDrive.isBusy()) {
            // Optionally, update telemetry or handle other logic here
        }

        // Stop motors after spin
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    // Method to extend the vertical slide to a specified height in inches (approximated with ticks)
    public void extendVerticalSlides(int ticks) {
        int ticksPerInch = 10; // Example conversion, adjust based on robot mechanics

        leftVertDrive.setTargetPosition(+ ticks);
        rightVertDrive.setTargetPosition(- ticks); // Opposing movement

        leftVertDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightVertDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set power to extend slides
        leftVertDrive.setPower(0.6);
        rightVertDrive.setPower(0.6);

        // Wait until slides reach target position
        while (leftVertDrive.isBusy() || rightVertDrive.isBusy()) {
            // Optionally, update telemetry or handle other logic here
        }

        // Stop motors after extension
        //leftVertDrive.setPower(0);
        //rightVertDrive.setPower(0);
    }

    // Method to drive forward, backward, spin, and raise the vertical extender in a sequence
    public void autoGo() {
        
        //autoDriveBackward(60 * tick2cm);  // drive forward
        moveSliderUp(20*tick2cm);




        //autoDriveBackward(12 * tick2cm); //drive back

        // Step 3: Spin 180 degrees
        //autoSpin180();

        // Step 4: Raise the vertical extender
        //extendVerticalSlides(24 * tick2cm);

        // Step 5: Drive forward
        //autoDriveForward(12 * tick2cm);
    }
}
