package com.Util.Other;

import com.Game.Main.Main;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    public static final SpriteSheet bowSheet = new SpriteSheet("Items/bow_sheet.png", 48, 48);
    public static final SpriteSheet meleeSheet = new SpriteSheet("Items/melee_sheet.png", 48, 48);

    BufferedImage image;
    int width, height;

    /**
     * @param path Path of Image
     * @param sW Width of each sprite cell
     * @param sH Height of each sprite cell
     */
    public SpriteSheet(String path, int sW, int sH) {
        this.image = Main.getImage(path);
        this.width = sW;
        this.height = sH;
    }

    public BufferedImage getCell(int x, int y) {
        return image.getSubimage(x * width, y * height, width, height);
    }
}
