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
import com.Game.Items.Weapon.Bows.CrystalBow;
import com.Game.Items.Weapon.Superclasses.BowWeapon;
import com.Game.Items.Weapon.Superclasses.DaggerWeapon;

// TODO: Organize Item IDS eventually. Change player saves.
public enum ItemList {
    empty(new Item(0, "/", "/", "/", 0, 0)),
    log(new Log(1, "wood.png", "Log", "The remnants of a tree.", 1, 150)),
    bow(new BowWeapon(2, 0, 1,"Bow","Get ready for the power of my bow!", 1, 170)),
    arrow(new ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000, 10)),
    mapleBow(new BowWeapon(4, 5, 50, "Maple Bow", "This is really gonna hurt.", 1,375)),
    clownfish(new ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1,100)),
    bluefish(new BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.",1, 250)),
    mapleLog(new MapleLog(7, "maple_wood.png", "Maple Log", "A sticky log, sounds useful to me.",1, 350)),
    gold(new Coins(8, "gold_coin.png", "Coins", "[amt] shiny coins, good for trading..?",100000000, -1)),
    woodHarp(new WoodHarp(9, "wood_harp.png", "Wooden Harp", "I can get some coins from this. (35% effective)", 1, -1)),
    rockHelmet(new RockHelmet(10, "rock_helmet.png", "Rock Helmet", "Someone went under a dock.", 1, 1000)),
    rockChestplate(new RockChestplate(11, "rock_chestplate.png", "Rock Chestplate", "And there they saw a rock.", 1, 5000)),
    rockLeggings(new RockLeggings(12, "rock_leggings.png", "Rock Leggings", "It wasn't a rock.", 1, 4000)),
    rockBoots(new RockBoots(13, "rock_boots.png", "Rock Boots", "It was a rock lobster.", 1, 2000)),
    rockArrow(new RockArrowItem(14, "rock_arrow.png", "Rock Arrow", "It was a rock lobster.", 100000, 25)),
    feather(new Feather(15, "feather.png", "Feather", "Because steel is heavier than feathers.", 100000, 20)),
    arrowShaft(new ArrowShaft(16, "arrow_shaft.png", "Arrow Shaft", "Not quite the orange kind.", 100000, 10)),
    stringItem(new StringItem(17, "string.png", "String", "Where does the string come from?", 100000, 5)),
    bowString(new BowString(18, "bow_string.png", "Bow String", "I can combine this with a bow stock.", 1, 20)),
    ashBow(new BowWeapon(19, 1, 10, "Ash Bow", "Tier 10", 1, 1)),
    pineBow(new BowWeapon(20, 2, 20, "Pine Bow", "Tier 20", 1, 1)),
    oakBow(new BowWeapon(21, 3, 30, "Oak Bow", "Tier 30", 1, 1)),
    spruceBow(new BowWeapon(22, 4, 40, "Spruce Bow", "Tier 40", 1, 1)),
    oakLog(new OakLog(23, "oak_wood.png", "Oak Wood", "Tier 30", 1, 1)),
    pineLog(new PineLog(24, "pine_wood.png", "Pine Wood", "Tier 20", 1, 1)),
    spruceLog(new SpruceLog(25, "spruce_wood.png", "Spruce Wood", "Tier 40", 1, 1)),
    ashLog(new AshLog(26, "ash_wood.png", "Ash Wood", "Tier 10", 1, 1)),
    seaWeed(new Seaweed(27, "sea_weed.png", "Sea Weed", "Tasty", 1, 1)),
    stone(new Ore(28, "stone.png", "Stone", "Tier 1", 1, 1)),
    copperOre(new Ore(29, "copper_ore.png", "Copper Ore", "Tier 10", 1, 1)),
    tinOre(new Ore(30, "tin_ore.png", "Tin Ore", "Tier 10", 1, 1)),
    ironOre(new Ore(31, "iron_ore.png", "Iron Ore", "Tier 20", 1, 1)),
    goldOre(new Ore(32, "gold_ore.png", "Gold Ore", "Tier 30", 1, 1)),
    crystalBow(new CrystalBow(33, "Crystal Bow", "It's shines when I hold it sometimes.", 1, 1)),
    stoneDagger(new DaggerWeapon(34, 0, 1, "Stone Dagger", "Dull, but effective.", 1, 1)),
    bronzeDagger(new DaggerWeapon(35, 1, 10, "Bronze Dagger", "Dull, but effective.", 1, 1)),
    ironDagger(new DaggerWeapon(36, 2, 20, "Iron Dagger", "Dull, but effective.", 1, 1)),
    goldDagger(new DaggerWeapon(37, 3, 30, "Gold Dagger", "Dull, but effective.", 1, 1)),
    skyriteDagger(new DaggerWeapon(38, 4, 40, "Skyrite Dagger", "Dull, but effective.", 1, 1));

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

    public static ItemList getByName(String name) {
        for (ItemList item : values()) {
            if (item.name().equals(name))
                return item;
        }

        return null;
    }

    public ItemStack stackData(int data) {
        return new ItemStack(this, 1, data);
    }

    public ItemStack singleStack() {
        return new ItemStack(this, 1);
    }
}
