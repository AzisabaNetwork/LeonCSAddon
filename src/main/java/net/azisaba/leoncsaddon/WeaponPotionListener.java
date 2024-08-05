package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

public class WeaponPotionListener implements Listener {

    @EventHandler
    public void onDamage(WeaponDamageEntityEvent e){
        if(!(e.getVictim() instanceof Player)) return;

        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getWeaponTitle());

        if(data == null) return;

        Player victimPlayer = ((Player) e.getVictim());

        if(e.getPlayer().getScoreboard().getEntryTeam(e.getPlayer().getName())
                == victimPlayer.getScoreboard().getEntryTeam(e.getVictim().getName())){
            for(PotionEffect effect: data.allyPotion){
                victimPlayer.removePotionEffect(effect.getType());
                victimPlayer.addPotionEffect(effect);
            }
        }

    }

}
