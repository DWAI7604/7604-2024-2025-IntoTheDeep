package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class BasicAuto extends RobotLinearOpMode{
    @Override
    public void runOpMode() {

        declareHardwareProperties();

        while (!isStarted() && !isStopRequested()) {

            declareAutoVariables();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        while(opModeIsActive()){
            sleep(waitTime);
            encoderDrive(0.4, 25, MOVEMENT_DIRECTION.REVERSE);
            encoderSlideUp(0.7, 18, MOVEMENT_DIRECTION.FORWARD);
            encoderDrive(0.2, 4, MOVEMENT_DIRECTION.REVERSE);
            encoderSlideUp(0.7, 3, MOVEMENT_DIRECTION.REVERSE);
            encoderDrive(0.4, 15, MOVEMENT_DIRECTION.FORWARD);
            encoderDrive(0.4, 16, MOVEMENT_DIRECTION.STRAFE_LEFT);
            encoderTurn(0.8, 220, TURN_DIRECTION.TURN_RIGHT);
            encoderDrive(0.4, 20, MOVEMENT_DIRECTION.STRAFE_RIGHT);
            encoderDrive(0.4, 3, MOVEMENT_DIRECTION.STRAFE_LEFT);
            encoderSlideUp(0.7, 9.5, MOVEMENT_DIRECTION.REVERSE);
            encoderDrive(0.4, 18, MOVEMENT_DIRECTION.REVERSE);
            sleep(200);
            encoderSlideUp(0.7, 5, MOVEMENT_DIRECTION.FORWARD);
            encoderDrive(0.4, 15, MOVEMENT_DIRECTION.FORWARD);
            encoderTurn(0.8, 220, TURN_DIRECTION.TURN_RIGHT);
            encoderDrive(0.4, 5, MOVEMENT_DIRECTION.STRAFE_LEFT);
            encoderDrive(0.4, 35, MOVEMENT_DIRECTION.STRAFE_RIGHT);

            sleep(30000);
        }

    }


}
