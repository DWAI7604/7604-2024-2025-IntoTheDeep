package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TestTele", group = "Linear OpMode")
public class TestTele extends RobotLinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Drivetrain motors
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;

    // Slides motors
    private DcMotor horizontalDrive = null;
    private DcMotor leftVertDrive = null;
    private DcMotor rightVertDrive = null;

    int clamp(int x, int lower, int higher) {
        x = Math.max(x, lower);
        x = Math.min(x, higher);
        return x;
    }

    AutoControl ac;

    @Override
    public void runOpMode() {
        // Initialize hardware
        leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        horizontalDrive = hardwareMap.get(DcMotor.class, "horizontal_slide_drive");
        leftVertDrive = hardwareMap.get(DcMotor.class, "left_Vslide_drive");
        rightVertDrive = hardwareMap.get(DcMotor.class, "right_Vslide_drive");

        boolean Debounce = false;

        ac = new AutoControl(leftFrontDrive, leftBackDrive,  rightFrontDrive,  rightBackDrive,
                horizontalDrive, leftVertDrive, rightVertDrive);

        // Set drive motor directions
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        // Set slides to brake when power is zero


        // Initialize target positions
        int targetLeftPosition = leftVertDrive.getCurrentPosition();
        int targetHorizontalPosition = horizontalDrive.getCurrentPosition();
        int targetRightPosition = rightVertDrive.getCurrentPosition();

        int initialLeftPosition = leftVertDrive.getCurrentPosition();
        int initialRightPosition = rightVertDrive.getCurrentPosition();
        int initialHorizontalPosition = horizontalDrive.getCurrentPosition();


        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            // Drivetrain control
            double axial = -gamepad1.left_stick_y;  // Forward/backward
            double lateral = gamepad1.left_stick_x;  // Strafing
            double yaw = gamepad1.right_stick_x;  // Rotation

            double leftFrontPower = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower = axial - lateral + yaw;
            double rightBackPower = axial + lateral - yaw;

            // Normalize drivetrain power
            double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));
            if (max > 1.0) {
                leftFrontPower /= max;
                rightFrontPower /= max;
                leftBackPower /= max;
                rightBackPower /= max;
            }

            leftFrontDrive.setPower(leftFrontPower);
            rightFrontDrive.setPower(rightFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightBackDrive.setPower(rightBackPower);


            if(gamepad1.left_bumper){
                ac.autoGo();
            }
            if(gamepad1.y && !Debounce) {
                Debounce = true;
                ac.scoreSpecimen();
            } else if (!gamepad1.y && Debounce) {
                Debounce = false;
            }


            // Debugging telemetry
            telemetry.addData("Run Time", runtime.toString());
            telemetry.addData("Drivetrain FL/FR", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Drivetrain BL/BR", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.addData("Vertical Slide Target", "Left: %d, Right: %d", targetLeftPosition, targetRightPosition);
            telemetry.addData("Horizontal Slide Target", targetHorizontalPosition);
            telemetry.addData("Vertical Slide Current", "Left: %d, Right: %d",
                    leftVertDrive.getCurrentPosition(), rightVertDrive.getCurrentPosition());
            telemetry.addData("Horizontal Slide Current", horizontalDrive.getCurrentPosition());
            telemetry.update();
        }
    }
}

