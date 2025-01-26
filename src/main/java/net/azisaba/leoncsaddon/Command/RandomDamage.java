package net.azisaba.leoncsaddon.Command;

import net.azisaba.leoncsaddon.LeonCSAddon;
import net.azisaba.leoncsaddon.WeaponDamageRandomizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RandomDamage implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        commandSender.sendMessage("現在のランダムダメージ:" + String.valueOf(WeaponDamageRandomizer.getRandomDamage()));
        if(args.length == 1){
            if(args[0].equals("set")){
                LeonCSAddon.weaponDamageRandomizer.randomDamageUpdaterDefaultTask();
            }
        }
        return true;
    }
}
