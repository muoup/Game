package com.Game.Projectile;

import com.Game.Entity.Enemy;
import com.Game.Entity.Player;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Networking.PlayerObject;
import com.Game.World.World;
import com.Util.Math.Vector2;
import com.Util.Other.Render;
import com.Util.Other.Sprite;

import java.awt.image.BufferedImage;

public class HomingProjectile extends Projectile {
    enum HomingStatus {
        targetEnemy, targetOtherPlayer, targetSelf
    }

    public Enemy etarget;
    public PlayerObject ptarget;
    private HomingStatus status;

    public HomingProjectile(Vector2 position, float speed, BufferedImage image, String targetString) {
        super(position, targetString.substring(0, 1).equals("e_"), speed, image);
        this.image = image;

        interpretTarget(targetString.substring(2));

        MethodHandler.projectiles.add(this);
    }

    public static void spawn(String message) {
        // 3636.864 657.5846;sfProjectiles/web.png<-->8.0 8.0,0.0;618.6342;p_Admin;1343
        String[] contents = message.split(";");

        Vector2 position = Vector2.fromString(contents[0]);

        Sprite image = Sprite.identifierSprite(contents[1]);

        float speed = Float.parseFloat(contents[2]);

        String targetString = contents[3];

        new HomingProjectile(position, speed, image.getImage(), targetString).randomToken = Integer.parseInt(contents[4]);
    }

    public void renderProjectile() {
        Vector2 movement = Vector2.zero();

        switch (status) {
            case targetSelf:
                movement = Vector2.magnitudeDirection(position, Player.position);
                break;
            case targetOtherPlayer:
                movement = Vector2.magnitudeDirection(position, ptarget.getPos());
                break;
            case targetEnemy:
                movement = Vector2.magnitudeDirection(position, etarget.position);
                break;
        }

        position.add(movement.scaleClone(speed * (float) Main.dTime()));

        speed += speed * 0.05 * Main.dTime();

        Render.drawImage(image, adjustedPosition().subtractClone(World.offset));
    }

    public void interpretTarget(String target) {
        if (friendly) {
            status = HomingStatus.targetEnemy;

            etarget = World.findEnemy(Integer.parseInt(target));
        } else {
            if (target.equalsIgnoreCase(Player.name)) {
                status = HomingStatus.targetSelf;
            } else {
                status = HomingStatus.targetOtherPlayer;

                for (PlayerObject object : MethodHandler.playerConnections) {
                    if (object.getUsername().equalsIgnoreCase(target)) {
                        ptarget = object;
                        return;
                    }
                }
            }
        }
    }
}
