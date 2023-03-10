package org.firstinspires.ftc.teamcode.autonomousmovements;

import org.firstinspires.ftc.teamcode.hardware.AllMotorsAndSensorsTeamHardwareMap;

public abstract class AutonomousMovement {
    public AllMotorsAndSensorsTeamHardwareMap teamHardwareMap;

    public AutonomousMovement(AllMotorsAndSensorsTeamHardwareMap _teamHardwareMap) {
        teamHardwareMap = _teamHardwareMap;
    }
}