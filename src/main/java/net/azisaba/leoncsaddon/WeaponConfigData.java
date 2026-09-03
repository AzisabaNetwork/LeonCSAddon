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

    public final Integer dualWieldWeaponCMD;
    public final String dualWieldWeaponModel;

    public WeaponConfigData(String name, List<String> type, boolean isMain, List<String> requirements, double damage, double headshotBonusDamage, double criticalBonusDamage, double backstabBonusDamage, double guardMult, float walkSpeed, boolean canSprint, int reduceStartTick, int reduceEndTick, double reduceDamage, double projectileSizeXZ, double projectileSizeY, Integer dualWieldWeaponCMD, String dualWieldWeaponModel, List<PotionEffect> potionEffectList){
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
        this.dualWieldWeaponCMD = dualWieldWeaponCMD;
        this.dualWieldWeaponModel = dualWieldWeaponModel;
        this.allyPotion = potionEffectList;
    }

    public WeaponConfigData(String name, List<String> type, boolean isMain, List<String> requirements, double damage, double headshotBonusDamage, double criticalBonusDamage, double backstabBonusDamage, double guardMult, float walkSpeed, boolean canSprint, int reduceStartTick, int reduceEndTick, double reduceDamage, double projectileSizeXZ, double projectileSizeY, Integer dualWieldWeaponCMD, List<PotionEffect> potionEffectList){
        this(name, type, isMain, requirements, damage, headshotBonusDamage, criticalBonusDamage, backstabBonusDamage, guardMult, walkSpeed, canSprint, reduceStartTick, reduceEndTick, reduceDamage, projectileSizeXZ, projectileSizeY, dualWieldWeaponCMD, null, potionEffectList);
    }
}