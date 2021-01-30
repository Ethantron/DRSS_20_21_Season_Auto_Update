package org.firstinspires.ftc.teamcode.TeleDrive_Code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.net.DatagramSocket;

@TeleOp(name = "TESTESTEST", group = "")
public class TELEDRIVE_Test extends LinearOpMode {

    TELEDRIVE_Hardware Tele = new TELEDRIVE_Hardware();

    public DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackRight;
    public DcMotor motorBackLeft;
    public Servo test;
    double Speed = 1;
    double Frontleft;
    double Frontright;
    double Backleft;
    double Backright;

    private DatagramSocket socket;
    private boolean canRunGamepadThread;
    private Thread gamepadHandler;

    public void runOpMode() {

        Tele.Init();

        String address = "192.168.43.1"; //Check "Program and Manage" tab on the Driver Station and verify the IP address
        int port = 12345; //Change as needed
        canRunGamepadThread = false;

        try {
            this.socket = new DatagramSocket(port);
        } catch (Exception ex) {
            telemetry.addData("Exception: ", ex.getMessage());
        }

        /** MY CODE **/

        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");
        //These work without reversing (Tetrix motors).
        //AndyMark motors may be opposite, in which case uncomment these lines:

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        test = hardwareMap.servo.get("Servo");
        test.setPosition(0);

        /** MY CODE **/

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Connect your server to " + address + ":" + port, "");
        telemetry.update();

        waitForStart();

        canRunGamepadThread = true;

        Tele.startGamepadHandlerThread();


        //CUSTOM CODE GOES HERE

        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                /** MY CODE **/

                Frontright = Range.clip(Math.pow(gamepad1.right_stick_y, 3), -Speed, Speed);
                Frontleft = Range.clip(Math.pow(-gamepad1.left_stick_y, 3), -Speed, Speed);
                Backright = Range.clip(Math.pow(gamepad1.right_stick_y, 3), -Speed, Speed);
                Backleft = Range.clip(Math.pow(-gamepad1.left_stick_y, 3), -Speed, Speed);

                // write the values to the motors
                motorFrontRight.setPower(Frontright);
                motorFrontLeft.setPower(Frontleft);
                motorBackLeft.setPower(Backleft);
                motorBackRight.setPower(Backright);

                if (gamepad1.a){
                    Speed = 1;
                    //pattern = RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_RAINBOW_PALETTE;
                    //	displayPattern();
                }

                if (gamepad1.b){
                    Speed = .75;
                    //pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                    //	displayPattern();
                }

                if (gamepad1.x){
                    Speed = .50;
                    //pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                    //	displayPattern();
                }

                if (gamepad1.y){
                    Speed = .25;
                    //pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
                    //	displayPattern();
                }

                if (gamepad1.dpad_up){
                    test.setPosition(1.0);
                } else if (gamepad1.dpad_down){
                    test.setPosition(-1.0);
                }else {
                    test.setPosition(0.0);
                }

                telemetry.addData("speed", Speed);
                telemetry.update();
                /** MY CODE **/
            }
            canRunGamepadThread = false;
            socket.close();


        }
    }
}