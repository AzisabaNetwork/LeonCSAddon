package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.events.WeaponShootEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class CustomProjectileSizeListener implements Listener {

    private static final String NMS_PACKAGE = "net.minecraft.server.v1_16_R3";

    @EventHandler
    public void onShoot(WeaponShootEvent e) {
        WeaponConfigData data = LeonCSAddon.INSTANCE.getWeaponConfig().getWeaponConfigData(e.getWeaponTitle());
        if (e.getProjectile() == null || data == null
                || data.projectileSizeXZ <= 0 || data.projectileSizeY <= 0) {
            return;
        }

        try {
            Entity projectile = e.getProjectile();
            Object nmsProjectile = findMethod(projectile.getClass(), "getHandle").invoke(projectile);

            Class<?> nmsEntityClass = Class.forName(NMS_PACKAGE + ".Entity");
            Class<?> entitySizeClass = Class.forName(NMS_PACKAGE + ".EntitySize");
            Field sizeField = findSizeField(nmsEntityClass, entitySizeClass);
            sizeField.set(nmsProjectile, entitySizeClass
                    .getConstructor(float.class, float.class, boolean.class)
                    .newInstance((float) data.projectileSizeXZ, (float) data.projectileSizeY, true));

            Location location = projectile.getLocation();
            double halfWidth = data.projectileSizeXZ / 2.0D;
            Class<?> boundingBoxClass = Class.forName(NMS_PACKAGE + ".AxisAlignedBB");
            Constructor<?> boundingBoxConstructor = boundingBoxClass.getConstructor(
                    double.class, double.class, double.class,
                    double.class, double.class, double.class);
            Object boundingBox = boundingBoxConstructor.newInstance(
                    location.getX() - halfWidth,
                    location.getY(),
                    location.getZ() - halfWidth,
                    location.getX() + halfWidth,
                    location.getY() + data.projectileSizeY,
                    location.getZ() + halfWidth);

            findBoundingBoxSetter(nmsEntityClass, boundingBoxClass)
                    .invoke(nmsProjectile, boundingBox);
//            LeonCSAddon.INSTANCE.getLogger().info(
//                    "[ProjectileSizeDebug] weapon=" + e.getWeaponTitle()
//                            + " width=" + projectile.getWidth()
//                            + " height=" + projectile.getHeight()
//            );
        } catch (ReflectiveOperationException | RuntimeException ex) {
            LeonCSAddon.INSTANCE.getLogger().log(Level.WARNING,
                    "Failed to apply projectileSize to " + e.getWeaponTitle(), ex);
        }
    }

    private static Field findSizeField(Class<?> entityClass, Class<?> entitySizeClass)
            throws NoSuchFieldException {
        Class<?> current = entityClass;
        while (current != null) {
            try {
                Field field = current.getDeclaredField("size");
                if (field.getType().equals(entitySizeClass)) {
                    field.setAccessible(true);
                    return field;
                }
            } catch (NoSuchFieldException ignored) {
                // Search the superclass below.
            }
            current = current.getSuperclass();
        }
        throw new NoSuchFieldException("EntitySize field not found");
    }

    private static Method findBoundingBoxSetter(Class<?> entityClass, Class<?> boundingBoxClass)
            throws NoSuchMethodException {
        Class<?> current = entityClass;
        while (current != null) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.getParameterCount() == 1
                        && method.getParameterTypes()[0].equals(boundingBoxClass)
                        && (method.getName().equals("a") || method.getName().equals("setBoundingBox"))) {
                    method.setAccessible(true);
                    return method;
                }
            }
            current = current.getSuperclass();
        }
        throw new NoSuchMethodException("AxisAlignedBB setter not found");
    }

    private static Method findMethod(Class<?> type, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Class<?> current = type;
        while (current != null) {
            try {
                Method method = current.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
                // Search the superclass below.
            }
            current = current.getSuperclass();
        }
        throw new NoSuchMethodException(name);
    }
}
