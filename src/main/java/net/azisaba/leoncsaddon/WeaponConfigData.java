package net.azisaba.leoncsaddon;

import org.bukkit.potion.PotionEffect;

import java.util.List;

public class WeaponConfigData {

    public final String name;
    public final List<String> type;
    public final boolean isMain;
    public final List<String> requirements;
    public final double damage;
    public final double headshotBonusDamage;
    public final double criticalBonusDamage;
    public final double backstabBonusDamage;
    public final double projectileSizeXZ;
    public final double projectileSizeY;

    public final List<PotionEffect> allyPotion;

    public final double guardMult;
    public final float walkSpeed;
    public final boolean canSprint;

    public final int reduceStartTick;
    public final int reduceEndTick;
    public final double reduceDamage;

    public WeaponConfigData(String name, List<String> type, boolean isMain, List<String> requirements, double damage, double headshotBonusDamage, double criticalBonusDamage, double backstabBonusDamage, double guardMult, float walkSpeed, boolean canSprint, int reduceStartTick, int reduceEndTick, double reduceDamage, double projectileSizeXZ, double projectileSizeY, List<PotionEffect> potionEffectList){
        this.type = type;
        this.isMain = isMain;
        this.requirements = requirements;
        this.name = name;
        this.damage = damage;
        this.headshotBonusDamage = headshotBonusDamage;
        this.criticalBonusDamage = criticalBonusDamage;
        this.backstabBonusDamage = backstabBonusDamage;
        this.guardMult = guardMult;
        this.walkSpeed = walkSpeed;
        this.canSprint = canSprint;
        this.reduceStartTick = reduceStartTick;
        this.reduceEndTick = reduceEndTick;
        this.reduceDamage = reduceDamage;
        this.projectileSizeXZ = projectileSizeXZ;
        this.projectileSizeY = projectileSizeY;
        this.allyPotion = potionEffectList;
    }

}
