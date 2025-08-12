package net.azisaba.leoncsaddon;

import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.math.BigDecimal;

/**
 * 射撃時の干渉を行う
 * @author AburaAgeTarou
 */
public class WeaponPreShootListener implements Listener {

	/**
	 * 射撃時処理
	 * @param event 射撃時イベント
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWeaponPreShoot(WeaponPreShootEvent event) {
		// 拡散倍率にキャップを適用する
		double spread = event.getBulletSpread();
		spread = capSpread(event.getPlayer(), spread);
		event.setBulletSpread(spread);
	}

	/**
	 * 拡散倍率にキャップを適用する
	 * @param player 射撃者
	 * @param spread 拡散倍率
	 * @return キャップ適用後の拡散倍率
	 */
	protected double capSpread(Player player, double spread) {
		double cap;
		// スコープを覗いている場合
		if(player.hasMetadata("ironsights")) {
			cap = LeonCSAddon.INSTANCE.getConfig().getDouble("scoped_spread_cap", -1.0d);
		}
		else {
			cap = LeonCSAddon.INSTANCE.getConfig().getDouble("un_scoped_spread_cap", -1.0d);
		}

		// キャップが負の場合はキャップを適用しない
		BigDecimal capBD = new BigDecimal(cap);
		if(capBD.compareTo(BigDecimal.ZERO) < 0) {
			return spread;
		}

		// 拡散倍率とキャップの小さい方を採用する
		BigDecimal spreadBD = new BigDecimal(spread);
		return spreadBD.min(capBD).doubleValue();
	}
}
