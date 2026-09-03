package net.azisaba.leoncsaddon;

import net.azisaba.crackshot.CSUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

/* メインクラスでの呼び出しがコメントアウトされているので読み込まれません */

@SuppressWarnings("UnstableApiUsage")
public class DualWieldWeaponListener implements Listener {

    private final LeonCSAddon plugin;

    public DualWieldWeaponListener(LeonCSAddon plugin) {
        this.plugin = plugin;
    }

    public DualWieldWeaponListener() {
        this(LeonCSAddon.INSTANCE);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onChangeItem(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        String weaponTitle = new CSUtility().getWeaponTitle(item);
        if(weaponTitle != null){
            WeaponConfigData weaponData = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(weaponTitle);
            if(weaponData != null && (weaponData.dualWieldWeaponModel != null || weaponData.dualWieldWeaponCMD != null)){
                Bukkit.getScheduler().runTaskLater(LeonCSAddon.INSTANCE, () -> {
                    ItemStack offhandItem = player.getInventory().getItemInOffHand();
                    if(offhandItem.getType() == Material.AIR){
                        plugin.getLogger().warning("オフハンドのモデル/CMD更新を試みましたが、オフハンドが空です");
                        return;
                    }
                    ItemMeta meta = offhandItem.getItemMeta();
                    if (meta == null) {
                        return;
                    }
                    if (weaponData.dualWieldWeaponModel != null) {
                        org.bukkit.NamespacedKey modelKey = org.bukkit.NamespacedKey.fromString(weaponData.dualWieldWeaponModel);
                        if (modelKey != null) {
                            meta.setItemModel(modelKey);
                        }
                    } else if (weaponData.dualWieldWeaponCMD != null) {
                        applyCustomModelData(meta, weaponData.dualWieldWeaponCMD);
                    }
                    offhandItem.setItemMeta(meta);
                },20);
            }
        }
    }

    private void applyCustomModelData(ItemMeta meta, int cmd) {
        CustomModelDataComponent component = meta.getCustomModelDataComponent();
        component.setFloats(java.util.List.of((float) cmd));
        meta.setCustomModelDataComponent(component);
    }
}