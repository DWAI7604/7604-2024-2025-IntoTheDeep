package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp(name="SlideTele", group="Linear OpMode")
public class SlideTestTeleOP extends RobotLinearOpMode{
    private ElapsedTime runtime = new ElapsedTime();
    //private DcMotor slide;
    private boolean aPressed = false;
    private boolean bPressed = false;
    private boolean xPressed = false;
    private boolean yPressed = false;


    @Override
    public void runOpMode() {
        declareSlideProperty();
        //slide  = hardwareMap.get(DcMotor.class, "slide");

        waitForStart();

        while(opModeIsActive()){
            if (gamepad1.b && !bPressed) {
                bPressed = true;
            }
            else if (!gamepad1.b && bPressed){
                bPressed = false;
            }

            if (bPressed){
                bPressed = false;
                encoderSlide(0.3, 10, MOVEMENT_DIRECTION.FORWARD);
                sleep(1000);
            }

            if (gamepad1.a && !aPressed) {
                aPressed = true;
            }
            else if (!gamepad1.a && aPressed){
                aPressed = false;
            }

            if (aPressed){
                aPressed = false;
                encoderSlide(0.3, 10, MOVEMENT_DIRECTION.REVERSE);
                sleep(1000);
            }

            if (gamepad1.x && !xPressed) {
                xPressed = true;
            }
            else if (!gamepad1.x && xPressed){
                xPressed = false;
            }

            if (xPressed){
                xPressed = false;
                encoderSlide(0.3, 20, MOVEMENT_DIRECTION.REVERSE);
                sleep(1000);
            }

            if (gamepad1.y && !yPressed) {
                yPressed = true;
            }
            else if (!gamepad1.y && yPressed){
                yPressed = false;
            }

            if (yPressed){
                yPressed = false;
                encoderSlide(0.3, 20, MOVEMENT_DIRECTION.FORWARD);
                sleep(1000);
            }
        }
    }
}
