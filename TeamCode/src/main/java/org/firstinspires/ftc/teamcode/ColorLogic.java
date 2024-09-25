package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ColorLogic
{
    private float FoV;
    private float CamHeight;
    private float CamAngle;

    private Vector2d ScreenSize;
    private Vector2d SampleCount;
    private Vector2d MinPos;
    private Vector2d PosRange;
    private Vector2d MaxPos;

    public ColorLogic(float FoV, float CamHeight, float CamAngle, Vector2d ScreenSize, Vector2d SampleCount)
    {
        this.FoV = FoV;
        this.CamHeight = CamHeight;
        this.CamAngle = CamAngle;
        this.ScreenSize = ScreenSize;
        this.SampleCount = SampleCount;

        float MinAngle = (float)Math.tan(CamAngle - FoV / 2) * CamHeight;
        float MaxAngle = (float)Math.tan(CamAngle + FoV / 2) * CamHeight;

        MinPos = new Vector2d(MinAngle, MinAngle);
        MaxPos = new Vector2d(MaxAngle, MaxAngle);
        PosRange = new Vector2d(MaxPos.x - MinPos.x, MaxPos.y - MinPos.y);

    }

    public List<Vector2d> GetSamplePositions(Pixel[] Screen, int TargetColor)
    {
        List<Vector2d> PosList = new ArrayList<Vector2d>();
        List<Vector2d> ScreenPosList = new ArrayList<Vector2d>();

        for (int x = 0; x < ScreenSize.x; x += (int)SampleCount.x)
        {
            for (int y = (int)ScreenSize.y - 1; y >= 0; y -= (int)SampleCount.y)
            {
                if (ColorSense.get_color(Screen[x + y].getHSV()) == TargetColor)
                {
                    ScreenPosList.add(new Vector2d(x, y));
                }
            }
        }

        for (Vector2d Pos : ScreenPosList)
        {
            Vector2d Uv = new Vector2d(Pos.x / (float)ScreenSize.x, Pos.y / (float)ScreenSize.y);
            PosList.add(new Vector2d(Uv.x * PosRange.x + MinPos.x, Uv.y + PosRange.y + MinPos.y));
        }

        return PosList;
    }
}
