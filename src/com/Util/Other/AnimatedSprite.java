package com.Util.Other;

import com.Util.Math.Vector2;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedSprite {
    public static float timer = 0;
    private SpriteSheet spriteSheet;
    private Vector2[] cellList;
    private BufferedImage image;
    private float fps;
    public int row = -1;

    public AnimatedSprite(SpriteSheet sheet, int fps) {
        this.spriteSheet = sheet;
        this.fps = fps;
        calculateCells();
    }

    public AnimatedSprite(SpriteSheet sheet, Vector2[] cellList, int fps) {
        this.fps = fps;
        this.spriteSheet = sheet;
        this.cellList = cellList;
    }

    public AnimatedSprite(SpriteSheet sheet, int fps, int column) {
        this(sheet, fps);
        this.row = column;
    }

    public BufferedImage getImage() {
        if (cellList.length == 0)
            return null;

        if (row >= 0)
            return getColumnImage();

        int cellNum = (int) ((timer * fps) % cellList.length);

        Vector2 cell = cellList[cellNum];
        return spriteSheet.getCell((int) cell.x, (int) cell.y);
    }

    private BufferedImage getColumnImage() {
        int cellNum = (int) ((timer * fps) % spriteSheet.columns);
        return spriteSheet.getCell(cellNum, row);
    }

    public BufferedImage getImage(Vector2 scale) {
        return Render.getScaledImage(getImage(), scale);
    }

    public void calculateCells() {
        ArrayList<Vector2> cells = new ArrayList<>();

        for (int y = 0; y < spriteSheet.rows; y++) {
            for (int x = 0; x < spriteSheet.columns; x++) {
                cells.add(new Vector2(x, y));
            }
        }

        cellList = cells.toArray(new Vector2[cells.size()]);
    }
}
