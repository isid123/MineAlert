package me.isi.minealert.services;

import me.isi.minealert.database.Database;
import me.isi.minealert.managers.AlertManager;
import me.isi.minealert.managers.PlayerManager;
import me.isi.minealert.settings.PlayerData;
import me.isi.minealert.settings.Settings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockTrackingService {

	private final PlayerManager playerManager;
	private final AlertManager alertManager;

	public BlockTrackingService(PlayerManager playerManager, AlertManager alertManager) {
		this.playerManager = playerManager;
		this.alertManager = alertManager;
	}

	public void HandleBreakBlock(Player player, Material blockType, Location location){

		Database.getInstance().saveNewLog(player, location, blockType);

		PlayerData data = playerManager.getOrCreate(player.getUniqueId(), player.getName());
		long now = System.currentTimeMillis();

		if (now - data.getLastBreakTime() <= Settings.TIME_WINDOW_MS) {
			data.setBreakCount(data.getBreakCount() + 1);
		} else {
			data.setBreakCount(1);
		}

		data.setLastBreakTime(now);
		data.setLastBlockType(blockType);
		data.setLastBlockLocation(location);

		if (data.getBreakCount() >= Settings.MAX_BLOCKS) {
			alertManager.sendAlert(player, blockType, location);
			data.setBreakCount(0);
		}
	}
}
