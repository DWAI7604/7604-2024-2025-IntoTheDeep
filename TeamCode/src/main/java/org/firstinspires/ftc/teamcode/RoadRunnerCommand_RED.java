package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.roadRunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadRunner.trajectorysequence.TrajectorySequenceBuilder;

public class RoadRunnerCommand_RED extends RoadRunnerSubsystem_RED {
    Telemetry telemetry;
    RoadRunnerCommand_RED(
            SampleMecanumDrive sampleDrive, HardwareMap hardwareMap, Pose2d HomePose,
            StartingPosition startingPosition, Path path, PixelStack pixelStack,
            ParkingPosition parkingPosition, Telemetry telemetry
    ) {
        super(sampleDrive, hardwareMap, HomePose, startingPosition, path, pixelStack, parkingPosition);
        this.telemetry = telemetry;
    }

    public void spikeRandomizationPath(RoadRunnerSubsystem_RED.Randomization randomization){

        if (startingPosition == RoadRunnerSubsystem_RED.StartingPosition.SHORT){
            if (randomization == RoadRunnerSubsystem_RED.Randomization.LEFT){
                randomizedBackdrop = backdropLeft;
                leftPixelSpike = leftPixel_SHORT;
                pixel_cycle_PoseTransfer = leftPixel_SHORT;
                leftSpikeStartingTangetValue = 0;
                leftSpikeFinalTangetValue = 0;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.CENTER){
                randomizedBackdrop = backdropCenter;
                centerPixelSpike = centerPixel_SHORT;
                pixel_cycle_PoseTransfer = centerPixel_SHORT;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.RIGHT){
                randomizedBackdrop = backdropRight;
                rightPixelSpike = rightPixel_SHORT;
                pixel_cycle_PoseTransfer = rightPixel_SHORT;
            }
        }
        else if (startingPosition == RoadRunnerSubsystem_RED.StartingPosition.LONG){
            if (randomization == RoadRunnerSubsystem_RED.Randomization.LEFT){
                randomizedBackdrop = backdropLeft;
                rightPixelSpike = leftPixel_LONG;
                pixel_cycle_PoseTransfer = leftPixel_LONG;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.CENTER){
                randomizedBackdrop = backdropCenter;
                centerPixelSpike = centerPixel_LONG;
                pixel_cycle_PoseTransfer = centerPixel_LONG;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.RIGHT){
                randomizedBackdrop = backdropRight;
                leftPixelSpike = rightPixel_LONG;
                pixel_cycle_PoseTransfer = rightPixel_LONG;
                leftSpikeStartingTangetValue = 1;
                leftSpikeFinalTangetValue = 1;
            }
        }
    }

    public void cycle(){

        if (path == Path.INNER){
            stationClose = stationClose_Inner;
            stationFar = stationFar_Inner;
            backdrop_Unload = backdropLeft;
            stackStation = stationInner;
            stackStationTangetValue = 0;
        }
        else if (path == Path.OUTER){
            stationClose = stationClose_Outer;
            stationFar = stationFar_Outer;
            backdrop_Unload = backdropRight;
            stackStation = stationOuter;
            stackStationTangetValue = 2;
        }
    }

    public void parking(){
        if (parkingPosition == RoadRunnerSubsystem_RED.ParkingPosition.INNER){
            parkingTangetValue = 0;
            parkingPose = parkingInner;
        }
        else if (parkingPosition == RoadRunnerSubsystem_RED.ParkingPosition.MID){
            parkingTangetValue = 1;
            parkingPose = parkingMiddle;
        }
        else if (parkingPosition == RoadRunnerSubsystem_RED.ParkingPosition.OUTER){
            parkingTangetValue = 1;
            parkingPose = parkingOuter;
        }
    }

    public TrajectorySequenceBuilder getSpike(RoadRunnerSubsystem_RED.Randomization randomization){
        if (startingPosition == RoadRunnerSubsystem_RED.StartingPosition.SHORT){
            if (randomization == RoadRunnerSubsystem_RED.Randomization.LEFT){
                return leftSpike;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.CENTER){
                return centerSpike;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.RIGHT){
                return rightSpike;
            }
        }
        else if (startingPosition == RoadRunnerSubsystem_RED.StartingPosition.LONG){
            if (randomization == RoadRunnerSubsystem_RED.Randomization.LEFT){
                return rightSpike;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.CENTER){
                return centerSpike;
            }
            else if (randomization == RoadRunnerSubsystem_RED.Randomization.RIGHT){
                return leftSpike;
            }
        }

        return leftSpike;
    }

    public TrajectorySequenceBuilder getCycle(){
        if (startingPosition == StartingPosition.SHORT){
            return pixel_backdrop_Short;
        }
        else if (startingPosition == StartingPosition.LONG){
            return pixel_backdrop_Long;
        }

        return pixel_backdrop_Short;
    }

    public TrajectorySequenceBuilder getParking(){
        return parking;
    }
}