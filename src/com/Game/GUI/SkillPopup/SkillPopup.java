package com.Game.GUI.SkillPopup;

import com.Game.GUI.GUI;
import com.Game.GUI.Skills.SkillsManager;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Util.Math.DeltaMath;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Settings;

import java.awt.*;

/**
 * The intentions of this is to create a little popup
 * in the top right corner of the screen to show how much
 * experience the player just got. This is still WIP and
 * probably will be changed in the future.
 */
public class SkillPopup {
    private Image skillImage;
    private String skillMessage;
    private float duration;
    private float speed;
    private Vector2 position;

    public SkillPopup(int skillID, float amount) {
        this.skillImage = Render.getScaledImage
                (SkillsManager.skillImages[skillID], Vector2.identity(64));
        this.skillMessage = "+  " + (int) amount;
        this.duration = DeltaMath.range(1.25f, 1.5f);
        this.speed = DeltaMath.range(45.5f, 47.5f);
        this.position = new Vector2(GUI.invSize.x * 4 / 2f - 16 + DeltaMath.range(-64, 96), 150);

        MethodHandler.skillPopups.add(this);
    }

    public void update() {
        duration -= Main.dTime();
        position.y -= speed * Main.dTime();

        if (duration < 0) {
            MethodHandler.skillPopups.remove(this);
        }
    }

    public void render() {
        Color dColor = Main.graphics.getColor();
        Render.setFont(Settings.npcFont);
        Render.setColor(Color.BLACK);
        Render.drawImage(skillImage, position);
        Render.drawText(skillMessage, position.addClone(-Render.getStringWidth(skillMessage) - 12.5f, 28));
        Render.drawRectangle(Vector2.zero(), Vector2.zero());
        Render.setColor(dColor);
    }
}
