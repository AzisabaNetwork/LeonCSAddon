package net.azisaba.leoncsaddon;

import org.bukkit.event.Listener;

public class CustomProjectileSizeListener implements Listener {
/*
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
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
*/
}
