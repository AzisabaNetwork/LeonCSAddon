package net.azisaba.leoncsaddon;

import net.azisaba.leoncsaddon.IO.SharedRandomDamage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WeaponDamageRandomizer {
    public static double randomDamage = 0;

    public void damageLobbyUpdaterTaskStarter(){
        //2025 2月14日以降は停止
        LocalDate today = LocalDate.now();
        LocalDate targetDate = LocalDate.of(2025, 2, 14);
        if(!today.isAfter(targetDate)){
            for(int hour = 0; hour <= 24; hour++){
                scheduleDailyTask(hour, 0, this::randomLobbyDamageUpdaterDefaultTask);
            }
        }
    }

    public void damageUpdaterTaskStarter(){
        //2025 2月14日以降は停止
        LocalDate today = LocalDate.now();
        LocalDate targetDate = LocalDate.of(2025, 2, 14);
        if(!today.isAfter(targetDate)){
            for(int hour = 0; hour <= 24; hour++){
                scheduleDailyTask(hour, 1, this::randomDamageUpdaterDefaultTask);
            }
        }
    }

    //1日の指定された時間、分に送られてきたタスクを実行
    private void scheduleDailyTask(int hour, int minute, Runnable task) {
        long currentMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long targetMillis = calendar.getTimeInMillis();
        if (targetMillis < currentMillis) {
            // 翌日のスケジュールに変更
            targetMillis += TimeUnit.DAYS.toMillis(1);
        }

        long initialDelay = targetMillis - currentMillis;
        long oneDayInTicks = 20L * 60 * 60 * 24; // 1日の長さ (20 ticks/秒)

        Bukkit.getScheduler().runTaskTimer(LeonCSAddon.INSTANCE, task, initialDelay / 50 + 2, oneDayInTicks);
    }

    private static void getRandomValueInRange() {
        Random random = new Random();
        double randomValue = (-0.5) + (0 - (-0.5)) * random.nextDouble();
        randomDamage = Math.round(randomValue * 10) / 10.0;
        SharedRandomDamage.saveRD();
    }

    public void randomLobbyDamageUpdaterDefaultTask(){
        getRandomValueInRange();
        Bukkit.getServer().broadcast(Component.text("ランダムダメージ値が更新されました").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        Bukkit.getServer().broadcast(Component.text("現在の値は" + randomDamage + "です").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
    }

    public void randomDamageUpdaterDefaultTask(){
        randomDamage = SharedRandomDamage.loadRD();
        Bukkit.getServer().broadcast(Component.text("ランダムダメージ値が更新されました").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
        Bukkit.getServer().broadcast(Component.text("現在の値は" + randomDamage + "です").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD));
    }

    public double getRandomDamage(){
        return randomDamage;
    }

    public void setRandomDamage(double number){
        randomDamage = number;
    }
}
