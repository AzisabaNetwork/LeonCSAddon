package net.azisaba.leoncsaddon;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.shampaggon.crackshot.events.WeaponPreShootEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.azisaba.leoncsaddon.LeonCSAddon.INSTANCE;

/**
 * 射撃時の干渉を行う
 * @author AburaAgeTarou
 */
public class WeaponPreShootListener implements Listener {
	private final Map<UUID, Long> jumpTimestamps = new HashMap<>();
	/**
	 * 射撃時処理
	 * @param event 射撃時イベント
	 */
	@EventHandler
	public void onWeaponPreShoot(WeaponPreShootEvent event) {
		// 拡散倍率にキャップを適用する
		double spread = event.getBulletSpread();
		WeaponConfigData data = INSTANCE.getWeaponConfig().getWeaponConfigData(event.getWeaponTitle());
		if(data.type.contains("sr") && jumpTimestamps.containsKey(event.getPlayer().getUniqueId())){
			long elapsedMillis = System.currentTimeMillis() - jumpTimestamps.get(event.getPlayer().getUniqueId());
			spread += BigDecimal.valueOf((double) elapsedMillis / 1000).setScale(3, RoundingMode.HALF_UP).doubleValue();
		}
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
	/**
	 * ジャンプした判定
	 */
	@EventHandler
	public void onJump(PlayerJumpEvent event) {
		Player player = event.getPlayer();
		jumpTimestamps.put(player.getUniqueId(), System.currentTimeMillis());
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		// 追跡中のプレイヤーのみチェック
		if (!jumpTimestamps.containsKey(uuid)) return;

		// 地面に着いたら
		if (player.isOnGround()) {
			jumpTimestamps.remove(uuid);
		}
	}
}
