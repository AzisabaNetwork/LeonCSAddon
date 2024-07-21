package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponPlaceMineEvent;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import com.shampaggon.crackshot.events.WeaponPrepareShootEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class WeaponShootLimitListener implements Listener {

    @EventHandler
    public void onPreShoot(WeaponPrepareShootEvent e){
        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getWeaponTitle());
        if(data != null && !data.isMain){
            for(int i = 0; i < 9; i++){
                ItemStack item = e.getPlayer().getInventory().getItem(i);
                String weaponTitle = new CSUtility().getWeaponTitle(item);
                if(weaponTitle != null){
                    WeaponConfigData otherData = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(weaponTitle);
                    if(otherData != null && otherData.isMain){
                        if(data.requirements != null && otherData.type != null && Collections.disjoint(data.requirements, otherData.type)){
                            e.setCancelled(true);
                        }else {
                            e.setCancelled(false);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop(ItemSpawnEvent e){
        if(e.getEntity().getPickupDelay() != 24000) return;

        for(Player player: e.getEntity().getWorld().getPlayers()){
            if(e.getEntity().getLocation().distanceSquared(player.getEyeLocation()) < 1){
                WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(new CSUtility().getWeaponTitle(player.getInventory().getItemInMainHand()));
                if(data != null && !data.isMain){
                    for(int i = 0; i < 9; i++){
                        ItemStack item = player.getInventory().getItem(i);
                        String weaponTitle = new CSUtility().getWeaponTitle(item);
                        if(weaponTitle != null){
                            WeaponConfigData otherData = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(weaponTitle);
                            if(otherData != null && otherData.isMain){
                                if(data.requirements != null && otherData.type != null && Collections.disjoint(data.requirements, otherData.type)){
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }


    }

}
