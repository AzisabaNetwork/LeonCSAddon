package net.azisaba.leoncsaddon.Command;

import net.azisaba.leoncsaddon.IO.SharedRandomDamage;
import net.azisaba.leoncsaddon.LeonCSAddon;
import net.azisaba.leoncsaddon.WeaponDamageRandomizer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RandomDamage implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        commandSender.sendMessage("更新前のランダムダメージ:" + String.valueOf(WeaponDamageRandomizer.randomDamage));
        if(args.length == 1){
            if(args[0].equals("load")){
                LeonCSAddon.INSTANCE.weaponDamageRandomizer.setRandomDamage(SharedRandomDamage.loadRD());
            }
        }
        if(args.length == 2){
            if(args[0].equals("set")){
                try {
                    // args[1] を数値に変換
                    WeaponDamageRandomizer.randomDamage = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    // パースに失敗した場合は 0 を代入
                    System.out.println("数値のパースに失敗しました。デフォルト値 0 を代入します。");
                    WeaponDamageRandomizer.randomDamage = 0.0;
                }
                SharedRandomDamage.saveRD();
            }
        }
            commandSender.sendMessage("現在のランダムダメージ:" + String.valueOf(WeaponDamageRandomizer.randomDamage));
        return true;
    }
}
