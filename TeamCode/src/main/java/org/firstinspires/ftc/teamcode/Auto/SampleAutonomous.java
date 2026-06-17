package org.firstinspires.ftc.teamcode.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Auto.RRFiles.MecanumDrive;

@Config
@Autonomous(name = "SampleAutonomous", group = "Auto")
public class SampleAutonomous extends LinearOpMode {

    DcMotorEx leftFrontMotor;
    DcMotorEx rightFrontMotor;
    DcMotorEx leftBackMotor;
    DcMotorEx rightBackMotor;

    CRServo intake;

    public static double MAX_VEL      = 60;
    public static double MIN_ACCEL    = -50;
    public static double MAX_ACCEL    =  50;
    public static double INTAKE_SPEED =  1.0;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Pose2d startPose = new Pose2d(14, -60, Math.toRadians(90));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPose);

        leftFrontMotor  = hardwareMap.get(DcMotorEx.class, "LF");
        rightFrontMotor = hardwareMap.get(DcMotorEx.class, "RF");
        leftBackMotor   = hardwareMap.get(DcMotorEx.class, "LB");
        rightBackMotor  = hardwareMap.get(DcMotorEx.class, "RB");
        intake          = hardwareMap.get(CRServo.class, "intake");

        leftFrontMotor.setDirection(DcMotorEx.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotorEx.Direction.REVERSE);

        leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized — ready to start");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        Actions.runBlocking(new SequentialAction(
            new ParallelAction(
                drive.actionBuilder(startPose)
                    .splineTo(new Vector2d(14, -26), Math.toRadians(90),
                        new TranslationalVelConstraint(MAX_VEL),
                        new ProfileAccelConstraint(MIN_ACCEL, MAX_ACCEL))
                    .splineTo(new Vector2d(56, -26), Math.toRadians(270),
                        new TranslationalVelConstraint(MAX_VEL),
                        new ProfileAccelConstraint(MIN_ACCEL, MAX_ACCEL))
                    .splineTo(new Vector2d(56, -38), Math.toRadians(270),
                        new TranslationalVelConstraint(MAX_VEL),
                        new ProfileAccelConstraint(MIN_ACCEL, MAX_ACCEL))
                    .build(),
                new IntakeAction(INTAKE_SPEED)
            ),
            new IntakeAction(-INTAKE_SPEED),
            new SleepAction(2),
            new IntakeAction(0)
        ));

    }

    public class IntakeAction implements Action {

        private final double speed;

        public IntakeAction(double speed) {
            this.speed = speed;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            intake.setPower(speed);
            return false;
        }
    }

}