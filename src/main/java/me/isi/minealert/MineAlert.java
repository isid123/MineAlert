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

		Common.chatLineSmooth();
		Common.log("[MineAlert] Loaded");
		Common.chatLineSmooth();

		// connect to db
		DatabaseManager.getInstance().connect(this);


		playerManager = new PlayerManager();
		alertManager = new AlertManager();
		blockTrackingService = new BlockTrackingService(playerManager, alertManager);

		registerEvents(new BlockBreakListener(blockTrackingService, playerManager));
	}


//	@Override
//	public SpigotUpdater getUpdateCheck() {
//		int resourceId = 00000; //TODO to change
//		boolean autoDownload = false;
//
//		return new SpigotUpdater(resourceId, autoDownload) {
//			@Override
//			public void run() {
//				super.run();
//
//				if (this.isNewVersionAvailable()) {
//					String latest = this.getNewVersion();
//
//					Common.log("New version avaiable: &e" + latest);
//
//					for (Player player : Remain.getOnlinePlayers()) {
//						if (player.hasPermission("plugin.update.notify")) {
//							player.sendMessage(Common.colorize("New version avaiable: &e" + latest));
//						}
//					}
//				}
//			}
//		};
//	}


	@Override
	public int getMetricsPluginId() {
		return 27570;
	}


	@Override
	protected void onPluginStop() {
		Common.chatLineSmooth();
		Common.log("[MineAlert] Disabled");
		Common.chatLineSmooth();
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