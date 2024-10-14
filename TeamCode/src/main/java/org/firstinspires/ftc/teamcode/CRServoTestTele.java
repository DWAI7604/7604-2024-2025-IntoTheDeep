package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp(name="CRServoTele", group="Linear OpMode")
public class CRServoTestTele extends RobotLinearOpMode {
    private CRServo intake = null;

    @Override
    public void runOpMode() {
        intake = hardwareMap.get(CRServo.class, "intake");
        intake.setPower(0);
        waitForStart();

        while(opModeIsActive()){
            if (gamepad1.a){
                intake.setPower(1);
            }
            if (gamepad1.b){
                intake.setPower(-1);
            }
            if (gamepad1.y){
                intake.setPower(0);
            }
        }
    }
}
