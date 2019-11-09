package com.Game.World;

import com.Game.Entity.Enemy.ChessDungeon.Pawn;
import com.Game.Object.AreaTeleporter.InvisibleTeleporter;

public class ChessDungeon extends World {
    public ChessDungeon() {
        initImage("chess_dungeon.png");
    }

    public void initWorld() {
        // Teleporter to the Outside
        new InvisibleTeleporter(135, 125,5507, 1643,  0);

        // Chessboard Pieces
        new Pawn(502, 122, true);
        new Pawn(598, 122, false);
    }
}
