/** Currently used controls:
 * Gamepad 1:
 *      A, B, X, Y
 *      Left stick: X axis, Y axis
 *      Right stick: x axis
 *
 * Gamepad 2:
 *      A
 *      Back, Start
 *      Left trigger, Right trigger
 *      DPad up, DPad down
 **/

package org.firstinspires.ftc.teamcode.Test_Code;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

@TeleOp(name = "Ethan_Concept_Test", group= "Test_Code")
public class Ethan_Concept_Test extends LinearOpMode {

    public DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackRight;
    public DcMotor motorBackLeft;

    public DcMotor motorIntake;
    public DcMotor motorConveyor;
    public DcMotor motorLauncher;

    public Servo servoLaunchBar;
    public Servo servoLaunchAngle;

    double frontLeft;
    double frontRight;
    double backLeft;
    double backRight;

    double driveSpeed = 1;
    double launcherSpeed = 1;
    double conveyorSpeed = 1;

    private final static int LED_PERIOD = 10;
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    Telemetry.Item patternName;
    Telemetry.Item display;
    Deadline ledCycleDeadline;
    Deadline gamepadRateLimit;
    SampleRevBlinkinLedDriver.DisplayKind displayKind;


    @Override
    public void runOpMode() {       // Begins running the initialization code when the "int" button is pressed

        displayKind = SampleRevBlinkinLedDriver.DisplayKind.MANUAL;
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_LAVA_PALETTE;
        blinkinLedDriver.setPattern(pattern);


        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */

        motorFrontRight = hardwareMap.dcMotor.get("FR");
        motorFrontLeft = hardwareMap.dcMotor.get("FL");
        motorBackLeft = hardwareMap.dcMotor.get("BL");
        motorBackRight = hardwareMap.dcMotor.get("BR");

        motorIntake = hardwareMap.dcMotor.get("IT");
        motorConveyor = hardwareMap.dcMotor.get("CV");
        motorLauncher = hardwareMap.dcMotor.get("LN");

        servoLaunchBar = hardwareMap.servo.get("LB");
        servoLaunchBar.setPosition(0);

        servoLaunchAngle = hardwareMap.servo.get("LA");
        servoLaunchAngle.setPosition(0);

        //These work without reversing (Tetrix motors).
        //AndyMark motors may be opposite, in which case uncomment these lines:

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        motorIntake.setDirection(DcMotor.Direction.FORWARD);
        motorConveyor.setDirection(DcMotor.Direction.FORWARD);
        motorLauncher.setDirection(DcMotor.Direction.FORWARD);

        pattern = RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_PARTY_PALETTE;
        displayPattern();

        //Initialization Telemetry
        telemetry.addData("Drive Train: ", "Initialized");      // Adds telemetry to the screen to show that the drive train is initialized
        telemetry.addData("Payload: ", "Initialized");          // Adds telemetry to the screen to show that the payload is initialized
        telemetry.addData("Status: ", "Ready");                 // Adds telemetry to the screen to show that the robot is ready
        telemetry.addData("Press Play to Start ", "TeleOp");    // Adds telemetry to the screen to tell the drivers that the code is ready to start
        telemetry.update();                                                   // Tells the telemetry to display on the phone

        waitForStart();     // Tells the code to wait here until the drivers have pressed the start button

        /** Gamepad 1 controls (drive train) ==> **/

        /**Mechanum drive controls ==> **/
        float gamepad1LeftY = gamepad1.left_stick_y;        // Sets the gamepads left sticks y position to a float so that we can easily track the stick
        float gamepad1LeftX = -gamepad1.left_stick_x;       // Sets the gamepads left sticks x position to a float so that we can easily track the stick
        float gamepad1RightX = -gamepad1.right_stick_x;     // Sets the gamepads right sticks x position to a float so that we can easily track the stick

        // Mechanum formulas
        double FrontRight = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;     // Combines the inputs of the sticks to clip their output to a value between 1 and -1
        double FrontLeft = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;     // Combines the inputs of the sticks to clip their output to a value between 1 and -1
        double BackRight = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;      // Combines the inputs of the sticks to clip their output to a value between 1 and -1
        double BackLeft = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;      // Combines the inputs of the sticks to clip their output to a value between 1 and -1

        // sets speed
        frontRight = Range.clip(Math.pow(FrontRight, 3), -driveSpeed, driveSpeed);    // Slows down the motor and sets its max/min speed to the double "speed"
        frontLeft = Range.clip(Math.pow(FrontLeft, 3), -driveSpeed, driveSpeed);      // Slows down the motor and sets its max/min speed to the double "speed"
        backRight = Range.clip(Math.pow(BackRight, 3), -driveSpeed, driveSpeed);      // Slows down the motor and sets its max/min speed to the double "speed"
        backLeft = Range.clip(Math.pow(BackLeft, 3), -driveSpeed, driveSpeed);        // Slows down the motor and sets its max/min speed to the double "speed"


        motorFrontRight.setPower(frontRight);   // Sets the front right motors speed to the previous double
        motorFrontLeft.setPower(frontLeft);     // Sets the front left motors speed to the previous double
        motorBackRight.setPower(backRight);     // Sets the back right motors speed to the previous double
        motorBackLeft.setPower(backLeft);       // Sets the back left motors speed to the previous double

        if (gamepad1.a){
            driveSpeed = 1;
            pattern = RevBlinkinLedDriver.BlinkinPattern.CP1_LIGHT_CHASE;
            displayPattern();
        }

        if (gamepad1.b){
            driveSpeed = .75;
            pattern = RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_FOREST_PALETTE;
            displayPattern();
        }

        if (gamepad1.x){
            driveSpeed = .50;
            pattern = RevBlinkinLedDriver.BlinkinPattern.CP2_LIGHT_CHASE;
            displayPattern();
        }

        if (gamepad1.y){
            driveSpeed = .25;
            pattern = RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_OCEAN_PALETTE;
            displayPattern();
        }

        //End of speed Controls
        /** <== End of mechanum drive controls **/
        /** <== End of gamepad 1 controls (drive train) **/

        /** Gamepad 2 controls (payload) ==> **/

        /** Launcher controls ==> **/
        // Turns on and off the launcher
        if (gamepad2.back && launcherSpeed == 1){
            launcherSpeed = 0;
            pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD;
            displayPattern();
        } else if (gamepad2.back && launcherSpeed == 0){
            launcherSpeed = 1;
            pattern = RevBlinkinLedDriver.BlinkinPattern.CONFETTI;
            displayPattern();
        }
        /** <== End of launcher controls **/

        /** Conveyor controls ==> **/
        if (gamepad2.start && conveyorSpeed == 1){
            conveyorSpeed = 0;
            pattern = RevBlinkinLedDriver.BlinkinPattern.STROBE_RED;
            displayPattern();
        } else if (gamepad2.start && conveyorSpeed == 0){
            conveyorSpeed = 1;
            pattern = RevBlinkinLedDriver.BlinkinPattern.CONFETTI;
            displayPattern();
        }
        /** <== End of conveyor controls **/

        /** Intake controls ==> **/
        if (gamepad2.left_trigger > 0.1 && gamepad2.right_trigger < 0.1){
            motorIntake.setPower(-0.75);
        } else if (gamepad2.right_trigger > 0.1 && gamepad2.left_trigger < 0.1){
            motorIntake.setPower(0.75);
        } else{
            motorIntake.setPower(0);
        }
        /** <== End of intake controls **/

        /** Launchbar controls ==> **/
        if (gamepad2.a && servoLaunchBar.getPosition() == 1){
            servoLaunchBar.setPosition(0);
            pattern = RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_RAINBOW_PALETTE;
            displayPattern();
        } else if (gamepad2.a && servoLaunchBar.getPosition() == 0){
            servoLaunchBar.setPosition(1);
            pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
            displayPattern();
        }
        /** <== End of launchbar controls **/

        /** Launch angle controls ==> **/
        if (gamepad2.dpad_up){
            servoLaunchAngle.setPosition(servoLaunchAngle.getPosition() + 0.1);
            pattern = RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_BLUE;
            displayPattern();
        } else if (gamepad2.dpad_down){
            servoLaunchAngle.setPosition(servoLaunchAngle.getPosition() - 0.1);
            pattern = RevBlinkinLedDriver.BlinkinPattern.LIGHT_CHASE_RED;
            displayPattern();
        }
        /** <== End of launch angle controls **/

        /** <== End of gamepad 2 controls (payload) **/


        /** Telemetry code ==> **/
        telemetry.addData("driveSpeed", driveSpeed);
        telemetry.addData("launcherSpeed", launcherSpeed);
        telemetry.addData("conveyorSpeed", conveyorSpeed);
        telemetry.addData("Current Launch Angle", servoLaunchAngle.getPosition());
        telemetry.update();
        /** <== End of telemetry code **/
    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    public double scaleInput(double dVal){
        double[] scaleArray ={ 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0){
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16){
            index = 16;
        }

        // get value from the array.
        double dScale;
        if (dVal < 0){
            dScale = -scaleArray[index];
        } else{
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

    protected void setDisplayKind(SampleRevBlinkinLedDriver.DisplayKind displayKind)
    {
        this.displayKind = displayKind;
        //display.setValue(displayKind.toString());
    }

    protected void displayPattern()
    {
        blinkinLedDriver.setPattern(pattern);
        //patternName.setValue(pattern.toString());
    }

}