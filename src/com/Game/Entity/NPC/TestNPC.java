package com.Game.Entity.NPC;

import com.Game.GUI.TextBox;
import com.Game.Main.Main;
import com.Util.Math.Vector2;

public class TestNPC extends NPC {

    Vector2 initPosition;

    public TestNPC(int x, int y) {
        super(0, x, y);

        initPosition = new Vector2(x, y);

        image = Main.main.getImageFromRoot("testNPC.png");
    }

    public void onInteract() {
        TextBox.setText(new String[]{
                "Hello traveller! How may IntBoxSize treat you?",
                "Here is a second text box to prove it works."
        });
    }

    public void move() {

    }
}
