package net.azisaba.leoncsaddon.Command;

import net.azisaba.leoncsaddon.LeonCSAddon;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Reload implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        LeonCSAddon.INSTANCE.getWeaponConfig().init();
        Bukkit.broadcast(Component.text("LCAを再読み込みしました"));
        return true;
    }
}
