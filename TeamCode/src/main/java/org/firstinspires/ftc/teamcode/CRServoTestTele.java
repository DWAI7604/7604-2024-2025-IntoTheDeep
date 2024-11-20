package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
@TeleOp(name="CRServoTele", group="Linear OpMode")
public class CRServoTestTele extends RobotLinearOpMode {
    //private CRServo intakeLeft = null;
    private DcMotor intakeRight = null;

    @Override
    public void runOpMode() {
        //intakeLeft = hardwareMap.get(CRServo.class, "intakeLeft");
        intakeRight = hardwareMap.get(DcMotor.class, "intakeRight");
        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);
        //intakeLeft.setPower(0);
        intakeRight.setPower(0);
        waitForStart();

        while(opModeIsActive()){
            if (gamepad1.a){
                //intakeLeft.setPower(1);
                intakeRight.setPower(1);
            }
            if (gamepad1.b){
                //intakeLeft.setPower(-1);
                intakeRight.setPower(-1);
            }
            if (gamepad1.y){
                //intakeLeft.setPower(0);
                intakeRight.setPower(0);
            }
        }
    }
}
