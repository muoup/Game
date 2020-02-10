package com.Game.Object.AreaTeleporter;

import com.Game.GUI.Chatbox.ChatBox;
import com.Game.Questing.QuestManager;

public class TribalTeleport extends InvisibleTeleporter {
    public TribalTeleport(int x, int y, int tx, int ty, int sub) {
        super(x, y, tx, ty, sub);
    }

    public boolean isSuccessful() {
        if (QuestManager.getData(1) == 3)
            QuestManager.setData(1, 4);

        return QuestManager.isComplete(1);
    }

    public void unSuccessful() {
        ChatBox.sendMessage("Maybe you should ask the captain before you just ride his ship.");
    }
}
