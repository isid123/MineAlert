package me.isi.minealert;

import lombok.Getter;
import me.isi.minealert.listeners.BlockBreakListener;
import me.isi.minealert.managers.AlertManager;
import me.isi.minealert.managers.DatabaseManager;
import me.isi.minealert.managers.PlayerManager;
import me.isi.minealert.services.BlockTrackingService;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SpigotUpdater;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

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
	public SpigotUpdater getUpdateCheck() {
		int resourceId = 1234; //TODO to change
		boolean autoDownload = false;

		return new SpigotUpdater(resourceId, autoDownload) {
			@Override
			public void run() {
				super.run();

				if (this.isNewVersionAvailable()) {
					String latest = this.getNewVersion();

					Common.log("New version avaiable: &e" + latest);

					for (Player player : Remain.getOnlinePlayers()) {
						if (player.hasPermission("plugin.update.notify")) {
							player.sendMessage(Common.colorize("New version avaiable: &e" + latest));
						}
					}
				}
			}
		};
	}


	@Override
	public int getMetricsPluginId() {
		return 27570;
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