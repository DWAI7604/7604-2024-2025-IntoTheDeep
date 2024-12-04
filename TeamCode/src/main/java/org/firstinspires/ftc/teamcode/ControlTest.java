package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name = "Control Test", group = "Linear OpMode")

public class ControlTest extends RobotLinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor slideUp = null;

    private boolean Press = false;

    private double Kp = 0.04;
    private double Ki = 0.4;
    private double Kd = 2;

    @Override
    public void runOpMode()
    {
        slideUp = hardwareMap.get(DcMotor.class, "slideUp");

        declareSlideProperty();

        waitForStart();


        while (opModeIsActive())
        {
            telemetry.addData("Kp", Kp);//Linear gain, reacts to the current state of the motor.
            telemetry.addData("Ki", Ki);//Integral gain, corrects for error over time.
            telemetry.addData("Kd", Kd);//Derivative gain, predicts future location and adjusts to avoid overshoot.
            telemetry.addData("Busy", slideUp.isBusy());

            telemetry.update();

            if (!slideUp.isBusy()) {
                if (gamepad1.y) {
                    EncoderSlide(12, 5000, 1, Kp, Kd); //Do not put target above 20
                } else if (gamepad1.a) {
                    EncoderSlide(5, 5000, 1, Kp, Kd);
                }

                if (!Press)
                {
                    if (gamepad1.left_bumper)
                    {
                        Press = true;
                        Kp += 0.01;
                    }
                    else if (gamepad1.left_trigger > 0.1)
                    {
                        Press = true;
                        Kp -= 0.01;
                    }
                    else if (gamepad1.right_bumper)
                    {
                        Press = true;
                        Kd += 0.01;
                    }
                    else if (gamepad1.right_trigger > 0.1)
                    {
                        Press = true;
                        Kd -= 0.01;
                    }
                    else if(gamepad1.dpad_up)
                    {
                        Press = true;
                        Ki += 0.01;
                    }
                    else if (gamepad1.dpad_down)
                    {
                        Press = true;
                        Ki -= 0.01;
                    }
                }
                else if (!(gamepad1.left_bumper || gamepad1.left_trigger > 0.1 || gamepad1.right_bumper || gamepad1.right_trigger > 0.1 || gamepad1.dpad_up || gamepad1.dpad_down))
                {
                    Press = false;
                }
            }
        }
    }

    public ControlTest()
    {

    }
}
