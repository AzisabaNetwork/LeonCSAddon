package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.CSUtility;
import me.DeeCaaD.CrackShotPlus.Events.WeaponHeldEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DualWieldWeaponListener implements Listener {

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChangeItem(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        String weaponTitle = new CSUtility().getWeaponTitle(item);
        if(weaponTitle != null){
            WeaponConfigData weaponData = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(weaponTitle);
            if(weaponData != null && weaponData.dualWieldWeaponCMD != null){
                Bukkit.getScheduler().runTaskLater(LeonCSAddon.INSTANCE, () -> {
                    ItemStack offhandItem = player.getInventory().getItemInOffHand();
                    if(offhandItem.getType() == Material.AIR){
                        Bukkit.getLogger().warning("オフハンドのCMD更新を試みましたが、オフハンドが空です");
                        return;
                    }
                    ItemMeta meta = offhandItem.getItemMeta();
                    meta.setCustomModelData(weaponData.dualWieldWeaponCMD);
                    offhandItem.setItemMeta(meta);
                },20);

            }
        }
    }

}
