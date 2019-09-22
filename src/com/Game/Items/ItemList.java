package com.Game.Items;

import com.Game.Items.Ammo.ArrowItem;
import com.Game.Items.Ammo.RockArrowItem;
import com.Game.Items.Armor.Rock.RockBoots;
import com.Game.Items.Armor.Rock.RockChestplate;
import com.Game.Items.Armor.Rock.RockHelmet;
import com.Game.Items.Armor.Rock.RockLeggings;
import com.Game.Items.Consumables.Food.BlueFishFood;
import com.Game.Items.Consumables.Food.ClownFishFood;
import com.Game.Items.RawResource.Coins;
import com.Game.Items.RawResource.Log;
import com.Game.Items.RawResource.MapleLog;
import com.Game.Items.Tool.WoodHarp;
import com.Game.Items.Weapon.Bow;
import com.Game.Items.Weapon.CrystalBow;

public enum ItemList {
    empty(new Item(0, "/", "/", "/", 0, 0)),
    wood(new Log(1, "wood.png", "Log", "The remnants of a tree.", 1, 150)),
    bow(new Bow(2, "bow.png", "Bow","Get ready for the power of my bow!", 1, 170)),
    arrow(new ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000, 10)),
    crystalBow(new CrystalBow(4, "crystalBow.png", "Crystal Bow", "This is really gonna hurt.", 1,450)),
    clownfish(new ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1,100)),
    bluefish(new BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.",1, 250)),
    mapleLog(new MapleLog(7, "maplewood.png", "Maple Log", "A sticky log, sounds useful to me.",1, 350)),
    gold(new Coins(8, "goldCoin.png", "Coin", "Shiny coins, good for trading..?",100000000, -1)),
    woodharp(new WoodHarp(9, "wood_harp.png", "Wooden Harp", "I can get some coins from this. (50% effective)", 1, -1)),
    rockhelmet(new RockHelmet(10, "rock_helmet.png", "Rock Helmet", "Someone went under a dock.", 1, 1000)),
    rockchestplate(new RockChestplate(11, "rock_chestplate.png", "Rock Chestplate", "And there they saw a rock.", 1, 5000)),
    rockleggings(new RockLeggings(12, "rock_leggings.png", "Rock Leggings", "It wasn't a rock.", 1, 4000)),
    rockboots(new RockBoots(13, "rock_boots.png", "Rock Boots", "It was a rock lobster.", 1, 2000)),
    rockarrow(new RockArrowItem(14, "rock_arrow.png", "Rock Arrow", "It was a rock lobster.", 100000, 25));

    public Item item;

    ItemList(Item item) {
        this.item = item;
    }

    public int maxStack() {
        return item.maxStack;
    }

    public int getID() {
        return item.id;
    }
}
