package com.Game.Items;

import com.Game.Items.Ammo.ArrowItem;
import com.Game.Items.Ammo.RockArrowItem;
import com.Game.Items.Armor.Rock.ArmorPiece;
import com.Game.Items.Armor.Rock.ArmorType;
import com.Game.Items.Consumables.Food.BlueFishFood;
import com.Game.Items.Consumables.Food.ClownFishFood;
import com.Game.Items.Consumables.Food.Seaweed;
import com.Game.Items.Misc.QuestItem;
import com.Game.Items.RawResource.*;
import com.Game.Items.RawResource.Log.Log;
import com.Game.Items.Tool.WoodHarp;
import com.Game.Items.Weapon.Bows.CrystalBow;
import com.Game.Items.Weapon.Superclasses.BowWeapon;
import com.Game.Items.Weapon.Superclasses.DaggerWeapon;

import java.awt.image.BufferedImage;

// TODO: Organize Item IDS eventually. Change player saves.
public enum ItemList {
    empty(new Item(0, "/", "/", "/", 0, 0)),
    bow(new BowWeapon(1, 0, 1, "Bow", "Get ready for the power of my bow!", 1, 170)),
    log(new Log(2, 1, ItemList.bow, "Log", "The remnants of a tree.", 1, 150)),
    arrow(new ArrowItem(3, "arrow.png", "Arrow", "Some sharp arrows!", 100000, 10)),
    mapleBow(new BowWeapon(4, 5, 50, "Maple Bow", "This is really gonna hurt.", 1, 375)),
    clownfish(new ClownFishFood(5, "clownfish.png", "Clownfish", "This looks quite tasty.", 1, 100)),
    bluefish(new BlueFishFood(6, "bluefish.png", "Bluefish", "This looks very tasty.", 1, 250)),
    mapleLog(new Log(7, 50, ItemList.mapleBow, "Maple Log", "A sticky log, sounds useful to me.", 1, 350)),
    gold(new Coins(8, "gold_coin.png", "Coins", "[amt] shiny coins, good for trading..?", 100000000, -1)),
    woodHarp(new WoodHarp(9, "wood_harp.png", "Wooden Harp", "I can get some coins from this. (35% effective)", 1, -1)),
    rockHelmet(new ArmorPiece(10, ArmorType.helmet, 1, "Rock Helmet", "Someone went under a dock.", 1, 1000)),
    rockChestplate(new ArmorPiece(11, ArmorType.chestplate, 1, "Rock Chestplate", "And there they saw a rock.", 1, 5000)),
    rockLeggings(new ArmorPiece(12, ArmorType.leggings, 1, "Rock Leggings", "It wasn't a rock.", 1, 4000)),
    rockBoots(new ArmorPiece(13, ArmorType.boots, 1, "Rock Boots", "It was a rock lobster.", 1, 2000)),
    rockArrow(new RockArrowItem(14, "rock_arrow.png", "Rock Arrow", "It was a rock lobster.", 100000, 25)),
    feather(new Feather(15, "feather.png", "Feather", "Because steel is heavier than feathers.", 100000, 20)),
    arrowShaft(new ArrowShaft(16, "arrow_shaft.png", "Arrow Shaft", "Not quite the orange kind.", 100000, 10)),
    stringItem(new StringItem(17, "string.png", "String", "Where does the string come from?", 100000, 5)),
    bowString(new BowString(18, "bow_string.png", "Bow String", "I can combine this with a bow stock.", 1, 20)),
    ashBow(new BowWeapon(19, 1, 10, "Ash Bow", "Tier 10", 1, 100)),
    pineBow(new BowWeapon(20, 2, 20, "Pine Bow", "Tier 20", 1, 100)),
    oakBow(new BowWeapon(21, 3, 30, "Oak Bow", "Tier 30", 1, 100)),
    spruceBow(new BowWeapon(22, 4, 40, "Spruce Bow", "Tier 40", 1, 100)),
    oakLog(new Log(23, 30, ItemList.oakBow, "Oak Wood", "Tier 30", 1, 100)),
    pineLog(new Log(24, 20, ItemList.pineBow, "Pine Wood", "Tier 20", 1, 100)),
    spruceLog(new Log(25, 40, ItemList.spruceBow, "Spruce Wood", "Tier 40", 1, 100)),
    ashLog(new Log(26, 10, ItemList.ashBow, "Ash Wood", "Tier 10", 1, 100)),
    seaWeed(new Seaweed(27, "sea_weed.png", "Sea Weed", "Tasty", 1, 100)),
    stone(new Ore(28, 0, "Stone", "Tier 1", 1, 100)),
    copperOre(new Ore(29, 2, "Copper Ore", "Tier 10", 1, 100)),
    tinOre(new Ore(30, 1, "Tin Ore", "Tier 10", 1, 100)),
    ironOre(new Ore(31, 3, "Iron Ore", "Tier 20", 1, 100)),
    goldOre(new Ore(32, 4, "Gold Ore", "Tier 30", 1, 100)),
    crystalBow(new CrystalBow(33, "Crystal Bow", "It's shines when I hold it sometimes.", 1, 100)),
    stoneDagger(new DaggerWeapon(34, 0, 1, "Stone Dagger", "Dull, but effective.", 1, 100)),
    bronzeDagger(new DaggerWeapon(35, 1, 10, "Bronze Dagger", "Dull, but effective.", 1, 100)),
    ironDagger(new DaggerWeapon(36, 2, 20, "Iron Dagger", "Dull, but effective.", 1, 100)),
    goldDagger(new DaggerWeapon(37, 3, 30, "Gold Dagger", "Dull, but effective.", 1, 100)),
    skyriteDagger(new DaggerWeapon(38, 4, 40, "Skyrite Dagger", "Dull, but effective.", 1, 100)),
    parrotBird(new QuestItem(39, 0, "Bird", "I bet the Bird Watcher would like one of these.", 1, -1)),
    bronzeHelmet(new ArmorPiece(40, ArmorType.helmet, 10, "Bronze Helmet", "Someone went under a hopper.", 1, 1000)),
    bronzeChestplate(new ArmorPiece(41, ArmorType.chestplate, 10, "Bronze Chestplate", "And there they saw a copper.", 1, 5000)),
    bronzeLeggings(new ArmorPiece(42, ArmorType.leggings, 10, "Bronze Leggings", "It wasn't a copper.", 1, 4000)),
    bronzeBoots(new ArmorPiece(43, ArmorType.boots, 10, "Bronze Boots", "It was a copper lobster.", 1, 2000)),
    birdNest(new BirdNest(44, "bird_nest.png", "Bird Nest", "It appears that the bird left some goodies.", 1, -1)),
    fishBait(new Item(45, "fish_bait.png", "Fish Bait", "This will make me fish faster.", 25000000, 55));

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

    public BufferedImage getImage() {
        return item.image;
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

    public int getPrice() {
        return item.worth;
    }

    public int getMaxAmount() {
        return item.maxStack;
    }
}
