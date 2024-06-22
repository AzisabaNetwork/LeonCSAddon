package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import com.shampaggon.crackshot.events.WeaponPrepareShootEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
                        if(Collections.disjoint(data.requirements, otherData.type)){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}
