package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class WeaponCustomDamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWeaponDamage(WeaponDamageEntityEvent e){

        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getWeaponTitle());

        if(data != null && !(e.getDamager() instanceof TNTPrimed)){

            if(data.type.contains("sr")){
                e.setDamage(data.damage);
            }else {
                e.setDamage(data.damage + WeaponDamageRandomizer.getRandomDamage());
            }

            if(e.isHeadshot()){
                e.setDamage(e.getDamage() + data.headshotBonusDamage);
            }
            if(e.isCritical()){
                e.setDamage(e.getDamage() + data.criticalBonusDamage);
            }
            if(e.isBackstab()){
                e.setDamage(e.getDamage() + data.backstabBonusDamage);
            }

            if(e.getDamager() instanceof Projectile){

                int reduceTicks = e.getDamager().getTicksLived();
                reduceTicks = reduceTicks - data.reduceStartTick;
                if(e.getDamager().getTicksLived() > data.reduceEndTick){
                    reduceTicks = data.reduceEndTick - data.reduceStartTick;
                }

                e.setDamage(e.getDamage() + data.reduceDamage * reduceTicks);
            }

            if(e.getVictim() instanceof Player){
                Player victim = (Player) e.getVictim();
                ItemStack hand = victim.getInventory().getItemInMainHand();
                String weaponTitle = new CSUtility().getWeaponTitle(hand);
                if(weaponTitle != null){
                    WeaponConfigData victimData = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(weaponTitle);
                    if(victimData != null) {
                        e.setDamage(e.getDamage() * victimData.guardMult);
                    }
                }
            }

        }

    }

}
