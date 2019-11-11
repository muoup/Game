package com.Game.Items;

import com.Game.Items.Ammo.ArrowItem;
import com.Game.Items.Ammo.RockArrowItem;
import com.Game.Items.Armor.Rock.RockBoots;
import com.Game.Items.Armor.Rock.RockChestplate;
import com.Game.Items.Armor.Rock.RockHelmet;
import com.Game.Items.Armor.Rock.RockLeggings;
import com.Game.Items.Consumables.Food.BlueFishFood;
import com.Game.Items.Consumables.Food.ClownFishFood;
import com.Game.Items.Consumables.Food.Seaweed;
import com.Game.Items.RawResource.*;
import com.Game.Items.RawResource.Log.*;
import com.Game.Items.Tool.WoodHarp;
import com.Game.Items.Weapon.Bows.*;

// TODO: Organize Item IDS eventually. Change player saves.
public enum ItemList {
    empty(new Item(0, "/", "/", "/", 0, 0)),
    log(new Log(1, "wood.png", "Log", "The remnants of a tree.", 1, 150)),
    bow(new Bow(2, "bow.png", "Bow","Get ready for the power of my bow!", 1, 170)),
    arrow(new ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000, 10)),
    mapleBow(new MapleBow(4, "maple_bow.png", "Maple Bow", "This is really gonna hurt.", 1,375)),
    clownfish(new ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1,100)),
    bluefish(new BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.",1, 250)),
    mapleLog(new MapleLog(7, "maple_wood.png", "Maple Log", "A sticky log, sounds useful to me.",1, 350)),
    gold(new Coins(8, "gold_coin.png", "Coins", "Shiny coins, good for trading..?",100000000, -1)),
    woodHarp(new WoodHarp(9, "wood_harp.png", "Wooden Harp", "I can get some coins from this. (50% effective)", 1, -1)),
    rockHelmet(new RockHelmet(10, "rock_helmet.png", "Rock Helmet", "Someone went under a dock.", 1, 1000)),
    rockChestplate(new RockChestplate(11, "rock_chestplate.png", "Rock Chestplate", "And there they saw a rock.", 1, 5000)),
    rockLeggings(new RockLeggings(12, "rock_leggings.png", "Rock Leggings", "It wasn't a rock.", 1, 4000)),
    rockBoots(new RockBoots(13, "rock_boots.png", "Rock Boots", "It was a rock lobster.", 1, 2000)),
    rockArrow(new RockArrowItem(14, "rock_arrow.png", "Rock Arrow", "It was a rock lobster.", 100000, 25)),
    feather(new Feather(15, "feather.png", "Feather", "Because steel is heavier than feathers.", 100000, 20)),
    arrowShaft(new ArrowShaft(16, "arrow_shaft.png", "Arrow Shaft", "Not quite the orange kind.", 100000, 10)),
    stringItem(new StringItem(17, "string.png", "String", "Where does the string come from?", 100000, 5)),
    bowString(new StringItem(18, "bow_string.png", "Bow String", "I can combine this with a bow stock.", 1, 20)),
    ashBow(new AshBow(19, "ash_bow.png", "Ash Bow", "Tier 10", 1, 1)),
    pineBow(new PineBow(20, "pine_bow.png", "Pine Bow", "Tier 20", 1, 1)),
    oakBow(new OakBow(21, "oak_bow.png", "Oak Bow", "Tier 30", 1, 1)),
    spruceBow(new SpruceBow(22, "spruce_bow.png", "Spruce Bow", "Tier 40", 1, 1)),
    oakLog(new OakLog(23, "oak_wood.png", "Oak Wood", "Tier 30", 1, 1)),
    pineLog(new PineLog(24, "pine_wood.png", "Pine Wood", "Tier 20", 1, 1)),
    spruceLog(new SpruceLog(25, "spruce_wood.png", "Spruce Wood", "Tier 40", 1, 1)),
    ashLog(new AshLog(26, "ash_wood.png", "Ash Wood", "Tier 10", 1, 1)),
    seaWeed(new Seaweed(27, "sea_weed.png", "Sea Weed", "Tasty", 1, 1)),
    stone(new Ore(28, "stone.png", "Stone", "Tier 1", 1, 1)),
    copperOre(new Ore(29, "copper_ore.png", "Copper Ore", "Tier 10", 1, 1)),
    tinOre(new Ore(30, "tin_ore.png", "Tin Ore", "Tier 10", 1, 1)),
    ironOre(new Ore(31, "iron_ore.png", "Iron Ore", "Tier 20", 1, 1)),
    goldOre(new Ore(32, "gold_ore.png", "Gold Ore", "Tier 30", 1, 1));

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

    public ItemStack singleStack() {
        return new ItemStack(this, 1);
    }
}
