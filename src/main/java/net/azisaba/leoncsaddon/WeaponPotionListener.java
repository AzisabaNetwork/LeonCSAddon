package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;

public class WeaponPotionListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(ProjectileHitEvent e){
        if(!(e.getHitEntity() instanceof Player)) return;
        if(!(e.getEntity().getShooter() instanceof Player)) return;
        if(!(e.getEntity() instanceof Snowball) && !(e.getEntity() instanceof Egg) && !(e.getEntity() instanceof Arrow)) return;
        if(!e.getEntity().hasMetadata("projParentNode")) return;

        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(((MetadataValue)e.getEntity().getMetadata("projParentNode").get(0)).asString());

        if(data == null) return;

        Player shooter = ((Player)e.getEntity().getShooter());
        Player victimPlayer = ((Player) e.getHitEntity());

        if(shooter.getScoreboard().getEntryTeam(shooter.getName())
                == victimPlayer.getScoreboard().getEntryTeam(e.getHitEntity().getName())){
            for(PotionEffect effect: data.allyPotion){
                victimPlayer.removePotionEffect(effect.getType());
                victimPlayer.addPotionEffect(effect);
            }
        }

    }

}
