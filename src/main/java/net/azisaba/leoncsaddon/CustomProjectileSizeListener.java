package net.azisaba.leoncsaddon;

import net.azisaba.crackshot.events.WeaponShootEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CustomProjectileSizeListener implements Listener {

    @EventHandler
    public void onShoot(WeaponShootEvent e) {
        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getWeaponTitle());
        if (e.getProjectile() != null && data != null) {
            if (data.projectileSizeXZ <= 0 && data.projectileSizeY <= 0) {
                return;
            }
            try {
                Entity projectile = e.getProjectile();
                Method getHandle = projectile.getClass().getMethod("getHandle");
                Object nmsEntity = getHandle.invoke(projectile);

                Class<?> entityClass = nmsEntity.getClass();
                Class<?> entityDimensionsClass = Class.forName("net.minecraft.world.entity.EntityDimensions");
                Class<?> aabbClass = Class.forName("net.minecraft.world.phys.AABB");

                // Create EntityDimensions (fixed/scalable)
                Object dimensions = null;
                try {
                    Method scalableMethod = entityDimensionsClass.getMethod("scalable", float.class, float.class);
                    dimensions = scalableMethod.invoke(null, (float) data.projectileSizeXZ, (float) data.projectileSizeY);
                } catch (NoSuchMethodException ignored) {
                    try {
                        Method fixedMethod = entityDimensionsClass.getMethod("fixed", float.class, float.class);
                        dimensions = fixedMethod.invoke(null, (float) data.projectileSizeXZ, (float) data.projectileSizeY);
                    } catch (NoSuchMethodException ignored2) {
                        for (Constructor<?> c : entityDimensionsClass.getConstructors()) {
                            if (c.getParameterCount() >= 2) {
                                dimensions = c.newInstance((float) data.projectileSizeXZ, (float) data.projectileSizeY);
                                break;
                            }
                        }
                    }
                }

                // Set dimensions field on NMS Entity
                if (dimensions != null) {
                    Class<?> curr = entityClass;
                    while (curr != null && curr != Object.class) {
                        Field dimField = null;
                        for (Field f : curr.getDeclaredFields()) {
                            if (f.getType().equals(entityDimensionsClass) || f.getName().equals("dimensions")) {
                                dimField = f;
                                break;
                            }
                        }
                        if (dimField != null) {
                            dimField.setAccessible(true);
                            dimField.set(nmsEntity, dimensions);
                            break;
                        }
                        curr = curr.getSuperclass();
                    }
                }

                // Set AABB bounding box
                Location loc = projectile.getLocation();
                double d0 = data.projectileSizeXZ / 2.0D;
                Constructor<?> aabbConstructor = aabbClass.getConstructor(
                        double.class, double.class, double.class,
                        double.class, double.class, double.class
                );
                Object aabb = aabbConstructor.newInstance(
                        loc.getX() - d0, loc.getY(), loc.getZ() - d0,
                        loc.getX() + d0, loc.getY() + data.projectileSizeY, loc.getZ() + d0
                );

                Method setBoundingBox = null;
                Class<?> curr = entityClass;
                while (curr != null && curr != Object.class) {
                    try {
                        setBoundingBox = curr.getDeclaredMethod("setBoundingBox", aabbClass);
                        break;
                    } catch (NoSuchMethodException ignored) {
                        for (Method m : curr.getDeclaredMethods()) {
                            if (m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(aabbClass)) {
                                setBoundingBox = m;
                                break;
                            }
                        }
                        if (setBoundingBox != null) break;
                    }
                    curr = curr.getSuperclass();
                }

                if (setBoundingBox != null) {
                    setBoundingBox.setAccessible(true);
                    setBoundingBox.invoke(nmsEntity, aabb);
                }
            } catch (Throwable ex) {
                // Ignore failure if running in an environment where NMS structure differs
            }
        }
    }
}