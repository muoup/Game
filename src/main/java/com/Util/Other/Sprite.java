package com.Util.Other;

import com.Game.Main.Main;
import com.Util.Math.Vector2;

import java.awt.image.BufferedImage;

public class Sprite {
    public BufferedImage image;
    public Vector2 scale;
    public double radians;

    protected Sprite() {}

    public Sprite(BufferedImage image) {
        this.image = image;
        this.scale = new Vector2(image.getWidth(), image.getHeight());
    }

    public BufferedImage getImage() {
        return Render.rotateImage(Render.getScaledImage(image, scale), radians);
    }

    public void render(Vector2 screenPosition) {
        Render.drawImage(image, screenPosition);
    }

    public static Sprite identifierSprite(String token) {
        try {
            String params = token.substring(2);
            String[] split = params.split("<-->");

            String finalParamsString = split[1];
            String[] finalParams = finalParamsString.split(",");

            params = split[0];

            Sprite returnSprite;

            switch (token.substring(0, 2)) {
                case "em":
                    returnSprite = new Sprite(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                    break;
                case "sf":
                    returnSprite = new Sprite(Main.getImage(params));
                    break;
                case "ss":
                    returnSprite = getSubImage(params);
                    break;
                case "an":
                    returnSprite = new AnimatedSprite(params);
                    break;
                default:
                    return null;
            }

            if (!finalParams[0].equals("null")) {
                returnSprite.scale = Vector2.fromString(finalParams[0]);
            }

            returnSprite.radians = Double.parseDouble(finalParams[1]);

            return returnSprite;
        } catch (Exception e) {
            System.err.println("Uh oh!");
            System.err.println(token);

            e.printStackTrace();
            return null;
        }
    }

    private static Sprite getSubImage(String params) {
        String root = params.split("<->")[0];
        String other = params.split("<->")[1];

        String[] pos = other.split(">-<")[0].split(",");
        String[] size = other.split(">-<")[1].split(",");

        BufferedImage image = Main.getImage(root)
                .getSubimage(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));;

        return new Sprite(image);
    }
}
