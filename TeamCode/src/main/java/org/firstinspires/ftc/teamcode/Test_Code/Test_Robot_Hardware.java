package org.firstinspires.ftc.teamcode.Test_Code;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class Test_Robot_Hardware {
    //Drive train definitions
    public DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackRight;
    public DcMotor motorBackLeft;

    //Headlight definitions
    public DcMotor headlight;

    //Gyro Definitions
    // Defines the gyro
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    //Generic Auto Definitions
    //Drive Train Encoder Definitions
    public static final double     COUNTS_PER_MOTOR_REV    = 560 ;    // eg: REV 20:1 Motor Encoder
    public static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    public static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV) / (WHEEL_DIAMETER_INCHES * 3.1415);

    //Lift Encoder Definitions
    static final double COUNTS_PER_LIFT_INCH = 23;  // Sets the double "COUNTS_PER_LEVEL" to 300    | Defines how long the lift needs to run to go up one level | About 55  counts per inch

    //Slide Encoder Definitions
    static final double COUNTS_PER_SLIDE_INCH = 49.23;
    //Gyro Turning Definitions
    static final double     HEADING_THRESHOLD       = 1 ;      // As tight as we can make it with an integer gyro
    static final double     P_TURN_COEFF            = 0.15;     // Larger is more responsive, but also less stable

    //Vuforia Definitions
    //Scanning Timing and Position Definitions
    public double pos = 0;

    // Object detection definitions
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    public static final String VUFORIA_KEY = "ASHBoLr/////AAABmbIUXlLiiEgrjVbqu8Iavlg6iPFigYso/+BCZ9uMzyAZFoo9CIzpV818SAqrjzuygz3hCeLW/ImK3xMH7DalGMwavqetwXS9Jw4I+rff2naxgV7n+EtYFvdCkUJDHfHVq1A4mhxDHgrjWZEqnLmZk25ppnIizQ0Ozcq4h6UmrWndEVEz8eKcCgn+IuglCEoEswvNBRAaKm/TAlpxLRNC6jQkZdJUh/TGYT05g9YCZo4+1ugmx01jrPCyHQVPVoeXm6VebLIuP7sNPw7njYzmVi2ffV5bYc4vf5kc5l5JwhBdPqnxuMfDLnHWaCkAO1UlVWqy2eY7/4b6iUYI2yN16ZKswSzLMmMNtPBu7e9HhKxA";

    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;

    //Local OpMode Members
    HardwareMap hwMap =  null;

    //Constructor
    public Test_Robot_Hardware() {
        //Purposefully Left Empty
    }

    //Initialization Void
    public void autoInit(HardwareMap ahwMap) {
        //Save Reference to Hardware Map

        hwMap = ahwMap;

        //Drive Train Initialization
        motorFrontLeft = hwMap.dcMotor.get("FL");
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight = hwMap.dcMotor.get("FR");
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackLeft = hwMap.dcMotor.get("BL");
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight = hwMap.dcMotor.get("BR");
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);

        //Headlight Initialization
        headlight = hwMap.dcMotor.get("headlight");
        headlight.setDirection(DcMotor.Direction.FORWARD);
        headlight.setPower(1);

        //Encoder Initialization

        //Stop and Reset Encoders
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Run Using Encoders
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Gyro Initialization

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;

        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode

        parameters.loggingEnabled = true;

        parameters.loggingTag = "IMU";

        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hwMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);
    }

    //Initialization Void
    public void telyOpInit(HardwareMap ahwMap) {
        //Save Reference to Hardware Map

        hwMap = ahwMap;

        //Drive Train Initialization
        motorFrontLeft = hwMap.dcMotor.get("FL");
        motorFrontRight = hwMap.dcMotor.get("FR");
        motorBackLeft = hwMap.dcMotor.get("BL");
        motorBackRight = hwMap.dcMotor.get("BR");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);


    }

    public void loop(HardwareMap ahwMap) {

    }
}
