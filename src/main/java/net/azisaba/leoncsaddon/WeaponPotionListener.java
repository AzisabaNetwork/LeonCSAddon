package net.azisaba.leoncsaddon;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;

public class WeaponPotionListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(ProjectileHitEvent e){
        if(!(e.getHitEntity() instanceof Player victimPlayer)) return;
        if(!(e.getEntity().getShooter() instanceof Player shooter)) return;
        if(!(e.getEntity() instanceof Snowball) && !(e.getEntity() instanceof Egg) && !(e.getEntity() instanceof Arrow)) return;
        if(!e.getEntity().hasMetadata("projParentNode")) return;

        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getEntity().getMetadata("projParentNode").getFirst().asString());

        if(data == null) return;

        if(shooter.getScoreboard().getEntryTeam(shooter.getName()) != null && shooter.getScoreboard().getEntryTeam(shooter.getName()).hasEntry(victimPlayer.getName())){
            for(PotionEffect effect: data.allyPotion){
                victimPlayer.removePotionEffect(effect.getType());
                victimPlayer.addPotionEffect(effect);
            }
        }
    }
}