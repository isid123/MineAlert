package me.isi.minealert;

import lombok.Getter;
import me.isi.minealert.listeners.BlockBreakListener;
import me.isi.minealert.managers.AlertManager;
import me.isi.minealert.managers.DatabaseManager;
import me.isi.minealert.managers.PlayerManager;
import me.isi.minealert.services.BlockTrackingService;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

@Getter
public final class MineAlert extends SimplePlugin {

	@Getter
	private static BlockTrackingService blockTrackingService;
	private static PlayerManager playerManager;
	private static AlertManager alertManager;

	@Override
	protected void onPluginStart() {

		Common.setLogPrefix("&8[&6MineAlert&8]&f");
		Common.setTellPrefix("&dMineAlert &7");

		// connect to db
		DatabaseManager.getInstance().connect(this);


		playerManager = new PlayerManager();
		alertManager = new AlertManager();
		blockTrackingService = new BlockTrackingService(playerManager, alertManager);

		registerEvents(new BlockBreakListener(blockTrackingService, playerManager));
	}

	@Override
	protected void onPluginStop() {
		DatabaseManager.getInstance().disconnect();
	}

	@Override
	protected void onReloadablesStart() {
	}

	@Override
	protected void onPluginPreReload() {
	}

	public static MineAlert getInstance() {
		return (MineAlert) SimplePlugin.getInstance();
	}
}