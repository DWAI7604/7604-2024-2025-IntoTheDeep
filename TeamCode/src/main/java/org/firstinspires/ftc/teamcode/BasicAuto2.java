package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "BasicAuto", group = "Linear Opmode")
public class BasicAuto2 extends LinearOpMode {

    private DcMotor leftFrontMotor;
    private DcMotor rightFrontMotor;
    private DcMotor leftBackMotor;
    private DcMotor rightBackMotor;

    @Override
    public void runOpMode() {
        // Initialize hardware
        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBackMotor");

        // Set motor directions (adjust if needed based on your robot's configuration)
        leftFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (opModeIsActive()) {
            // Drive in a square
            driveInSquare(0.5, 1000);

            // Drive in a circle
            driveInCircle(0.5, 3000);
        }
    }

    /**
     * Drive in a square pattern.
     * @param power The power level to set for the motors (0 to 1).
     * @param duration The duration for each side of the square (in milliseconds).
     */
    public void driveInSquare(double power, int duration) {
        for (int i = 0; i < 4; i++) {
            driveStraight(power, duration);
            turn90Degrees(power);
        }
    }

    /**
     * Drive in a circle pattern.
     * @param power The power level to set for the motors (0 to 1).
     * @param duration The duration to drive in the circle (in milliseconds).
     */
    public void driveInCircle(double power, int duration) {
        leftFrontMotor.setPower(power);
        rightFrontMotor.setPower(power * 0.5);
        leftBackMotor.setPower(power);
        rightBackMotor.setPower(power * 0.5);

        sleep(duration);

        stopMotors();
    }

    /**
     * Drive straight for a specified duration.
     * @param power The power level to set for the motors (0 to 1).
     * @param duration The duration to drive straight (in milliseconds).
     */
    private void driveStraight(double power, int duration) {
        leftFrontMotor.setPower(power);
        rightFrontMotor.setPower(power);
        leftBackMotor.setPower(power);
        rightBackMotor.setPower(power);

        sleep(duration);

        stopMotors();
    }

    /**
     * Turn 90 degrees by running the motors in opposite directions for a short duration.
     * @param power The power level to set for the motors (0 to 1).
     */
    private void turn90Degrees(double power) {
        leftFrontMotor.setPower(power);
        rightFrontMotor.setPower(-power);
        leftBackMotor.setPower(power);
        rightBackMotor.setPower(-power);

        sleep(500); // Adjust duration based on your robot's turning characteristics

        stopMotors();
    }

    /**
     * Stop all motors.
     */
    private void stopMotors() {
        leftFrontMotor.setPower(0);
        rightFrontMotor.setPower(0);
        leftBackMotor.setPower(0);
        rightBackMotor.setPower(0);
    }
}
