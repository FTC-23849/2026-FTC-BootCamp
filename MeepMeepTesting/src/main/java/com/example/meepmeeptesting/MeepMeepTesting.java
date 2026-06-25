package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60, 50, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18, 18)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(14, -60, Math.toRadians(90)))
                // TEST PATHS HERE:
                .splineTo(new Vector2d(14, -35), Math.toRadians(90))
                .splineTo(new Vector2d(56, -35), Math.toRadians(270))
                .splineTo(new Vector2d(56, -38), Math.toRadians(270))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
