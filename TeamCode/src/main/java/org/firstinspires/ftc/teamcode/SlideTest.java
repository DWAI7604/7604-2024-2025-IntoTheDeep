package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class SlideTest extends RobotLinearOpMode{
    DcMotor slide;

    public void runOpMode() {
        //declareSlideProperty();

        waitForStart();

        while (opModeIsActive()) {
            //encoderSlide(0.3, 5, MOVEMENT_DIRECTION.FORWARD);
            sleep(1000);
            //encoderSlide(0.3, 5, MOVEMENT_DIRECTION.REVERSE);
            sleep(1000);
            //encoderSlide(0.3, 10, MOVEMENT_DIRECTION.FORWARD);
            sleep(1000);
            //encoderSlide(0.3, 10, MOVEMENT_DIRECTION.REVERSE);
            sleep(1000);
            //encoderSlide(0.3, 15, MOVEMENT_DIRECTION.FORWARD);
            sleep(1000);
            //encoderSlide(0.3, 15, MOVEMENT_DIRECTION.REVERSE);
        }
    }
}
