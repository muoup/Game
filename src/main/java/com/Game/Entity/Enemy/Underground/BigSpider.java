package com.Game.Entity.Enemy.Underground;

import com.Game.Entity.Enemy.Enemy;
import com.Game.Items.DropTable;
import com.Game.Items.ItemList;
import com.Game.Main.Main;
import com.Game.Main.MethodHandler;
import com.Game.Projectile.WebSkillShot;
import com.Game.World.GroundItem;

import java.util.ArrayList;

public class BigSpider extends Enemy {
    public ArrayList<Enemy> minions = new ArrayList<Enemy>();

    public BigSpider(int x, int y) {
        super(x, y);
        this.image = getImage("bigSpider.png");
        this.name = "Big Spider";
        this.passive = false;
        this.speed = 1.45f;
        this.maxTarget = 7.5f;
        this.respawnTimer = 6.0f;

        timer2 = 2;

        setScale(64, 64);
        setBounds(663, 1808, 1594, 2805);
        setMaxHealth(35);
    }

    public void update() {
        if (!withinBounds()) {
            targetTimer = 0;
            setMoveTo();
        }
    }

    public void loseTarget() {
        MethodHandler.enemies.removeAll(minions);
        minions.clear();
    }

    public void AI() {
        timer += Main.dTime();
        timer2 += Main.dTime();

        if (range() > 256f)
            moveToPlayer();

        if (timer > 1f) {
            new WebSkillShot(position, Main.player.position).multiShotEnemy(20, 256, 3);
            timer = 0;
        }

        if (timer2 > 4f) {
            Enemy enemy = new BabySpider((int) position.x, (int) position.y);

            enemy.health = enemy.maxHealth;
            enemy.enabled = true;
            enemy.target();

            minions.add(enemy);
            createTemporary(enemy);
            timer2 = 0;
        }
    }

    public void passiveAI() {
        moveToAI();
    }

    public void handleDrops() {
        DropTable table = new DropTable();
        table.add(ItemList.stringItem, 5, 1);
        table.add(ItemList.gold, 750, 0.5);
        table.add(ItemList.copperOre, 1, 0.5);
        table.add(ItemList.skyriteDagger, 1, 0.05);
        GroundItem.createGroundItem(position, table.determineOutput());

        minions.forEach(Enemy::handleDrops);
        minions.clear();
    }
}
