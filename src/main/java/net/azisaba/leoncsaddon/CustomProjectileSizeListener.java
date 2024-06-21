package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.events.WeaponDamageEntityEvent;
import com.shampaggon.crackshot.events.WeaponShootEvent;
import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.Entity;
import net.minecraft.server.v1_15_R1.EntityProjectile;
import net.minecraft.server.v1_15_R1.EntitySize;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftProjectile;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class CustomProjectileSizeListener implements Listener {

    @EventHandler
    public void onShoot(WeaponShootEvent e){
        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getWeaponTitle());
        if(e.getProjectile() != null && data != null){
            Entity pj = ((CraftEntity)e.getProjectile()).getHandle();
            try {
                Class<Entity> clazz = Entity.class;
                Field f = clazz.getDeclaredField("size");
                f.setAccessible(true);
                f.set(pj, new EntitySize((float) data.projectileSizeXZ, (float) data.projectileSizeY, true));
                double d0 = data.projectileSizeXZ / 2.0D;
                pj.a(new AxisAlignedBB(pj.locX() - d0, pj.locY(), pj.locZ() - d0, pj.locX() + d0, pj.locY() + data.projectileSizeY, pj.locZ() + d0));
                e.getPlayer().sendMessage(pj.getBoundingBox().toString());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onDamage(WeaponDamageEntityEvent e){
        if(e.getDamager() instanceof Projectile){
            Projectile pj = (Projectile) e.getDamager();
            EntityProjectile ep = ((CraftProjectile)pj).getHandle();
            e.getPlayer().sendMessage(ep.getBoundingBox().toString());
        }
    }

}
